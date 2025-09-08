package com.javarush.NWA51.Poltavets.island.controller;

import com.javarush.NWA51.Poltavets.island.entity.Cell;
import com.javarush.NWA51.Poltavets.island.entity.Animals;
import com.javarush.NWA51.Poltavets.island.service.IslandConfigLoader;
import com.javarush.NWA51.Poltavets.island.service.IslandInitialization;
import com.javarush.NWA51.Poltavets.island.service.dto.IslandConfigDTO;
import com.javarush.NWA51.Poltavets.island.view.View;
import javafx.application.Platform;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class MainController {

    private final View view;
    private Cell[][] island;
    private static final int THREADS = 7;

    private final String defaultIslandPath = "src/main/resources/repository/islandDefault.prm";
    private final String defaultAnimalsPath = "src/main/resources/repository/animalsDefault.prm";
    private final String userIslandPath = "src/main/resources/repository/island.prm";
    private final String userAnimalsPath = "src/main/resources/repository/animals.prm";

    public MainController(View view) {
        this.view = view;
    }

    public void initIsland() {
        try {
            // Если пользовательские файлы существуют — используем их
            if (!Files.exists(Paths.get(userIslandPath))) {
                Files.copy(Paths.get(defaultIslandPath), Paths.get(userIslandPath));
            }
            if (!Files.exists(Paths.get(userAnimalsPath))) {
                Files.copy(Paths.get(defaultAnimalsPath), Paths.get(userAnimalsPath));
            }

            // Загружаем настройки острова
            IslandConfigDTO configDTO = IslandConfigLoader.loadConfig(userIslandPath);

            island = new IslandInitialization(configDTO).getIsland();

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Не удалось инициализировать остров");
        }
    }

    public void showIsland() {
        Platform.runLater(() -> view.show(island));
    }

    public void nextDay() {
        long start = System.currentTimeMillis();

        // Сброс флагов движения животных
        for (Cell[] row : island) {
            for (Cell cell : row) {
                cell.resetRunFlags();
            }
        }

        ExecutorService executor = Executors.newFixedThreadPool(THREADS);
        int rowsPerThread = (int) Math.ceil((double) island.length / THREADS);

        for (int t = 0; t < THREADS; t++) {
            int startRow = t * rowsPerThread;
            int endRow = Math.min(startRow + rowsPerThread, island.length);

            executor.submit(() -> {
                for (int i = startRow; i < endRow; i++) {
                    for (Cell cell : island[i]) {
                        try {
                            cell.runAnimals(island);
                            cell.nextDayCell();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            });
        }

        executor.shutdown();
        try {
            executor.awaitTermination(2, TimeUnit.MINUTES);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        int totalAnimals = 0;
        double totalGrass = 0;
        int totalBorn = 0;
        int totalDead = 0;

        for (Cell[] row : island) {
            for (Cell cell : row) {
                try {
                    int[] stats = cell.nextDayCell();
                    totalBorn += stats[0];
                    totalDead += stats[1];

                    for (List<Animals> list : cell.getAnimalsMap().values()) {
                        totalAnimals += list.size();
                    }
                    totalGrass += cell.getGrass();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        long end = System.currentTimeMillis();
        System.out.println("Весь день обработан за: " + (end - start) + " мс");
        System.out.println("Общая статистика по острову: " +
                "Всего животных=" + totalAnimals +
                ", Трава=" + totalGrass +
                ", Родилось=" + totalBorn +
                ", Умерло=" + totalDead);

        showIsland();
    }

    public void saveSettings(String islandContent, String animalsContent) {
        try {
            Files.writeString(Paths.get(userIslandPath), islandContent);
            Files.writeString(Paths.get(userAnimalsPath), animalsContent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public View getView() {
        return view;
    }
}
