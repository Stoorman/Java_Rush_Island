package com.javarush.NWA51.Poltavets.island.controller;

import com.javarush.NWA51.Poltavets.island.entity.Cell;
import com.javarush.NWA51.Poltavets.island.service.IslandInitialization;
import com.javarush.NWA51.Poltavets.island.service.dto.IslandConfigDTO;
import com.javarush.NWA51.Poltavets.island.view.View;
import javafx.application.Platform;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class MainController {

    private final View view;
    private Cell[][] island;
    private static final int THREADS = 4;

    public MainController(View view) {
        this.view = view;
    }

    // Инициализация острова
    public void initIsland() {
        IslandConfigDTO configDTO = new IslandConfigDTO.Builder()
                .nameIsland("Ява")
                .xAxisSize(60)
                .yAxisSize(40)
                .soilType(4)
                .grassDesert(50, 200)
                .grassForest(150, 400)
                .grassJungle(600, 1000)
                .grassSavvanna(300, 600)
                .build();

        island = new IslandInitialization(configDTO).getIsland();
    }

    // Отображение острова в GUI
    public void showIsland() {
        Platform.runLater(() -> view.show(island));
    }

    // Переход на следующий день с измерением времени
    public void nextDay() {
        long startTime = System.currentTimeMillis(); // старт общего таймера

        ExecutorService executor = Executors.newFixedThreadPool(THREADS);

        // Поток для перемещения животных по всем ячейкам
        executor.submit(() -> {
            long threadStart = System.currentTimeMillis();
            for (Cell[] row : island) {
                for (Cell cell : row) {
                    cell.runAnimals(island);
                }
            }
            long threadEnd = System.currentTimeMillis();
            System.out.println("Перемещение животных выполнено за: " + (threadEnd - threadStart) + " мс");
        });

        // Потоки для остальных действий (рост травы, питание, рождение, смерть)
        int rowsPerThread = island.length / (THREADS - 1);
        for (int t = 0; t < THREADS - 1; t++) {
            int startRow = t * rowsPerThread;
            int endRow = (t == THREADS - 2) ? island.length : startRow + rowsPerThread;

            final int threadIndex = t + 1; // для отображения номера потока
            executor.submit(() -> {
                long threadStart = System.currentTimeMillis();
                for (int i = startRow; i < endRow; i++) {
                    for (Cell cell : island[i]) {
                        try {
                            cell.nextDayCell();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
                long threadEnd = System.currentTimeMillis();
                System.out.println("Поток " + threadIndex + " обработал строки " + startRow + "-" + (endRow-1)
                        + " за: " + (threadEnd - threadStart) + " мс");
            });
        }

        // Ожидание завершения всех потоков
        executor.shutdown();
        try {
            executor.awaitTermination(1, TimeUnit.MINUTES);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        long endTime = System.currentTimeMillis();
        System.out.println("Весь день обработан за: " + (endTime - startTime) + " мс");

        // Обновление GUI после всех действий
        showIsland();
    }

    public View getView() {
        return view;
    }
}
