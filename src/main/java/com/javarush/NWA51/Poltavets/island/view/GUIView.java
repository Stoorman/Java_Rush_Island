package com.javarush.NWA51.Poltavets.island.view;

import com.javarush.NWA51.Poltavets.island.controller.MainController;
import com.javarush.NWA51.Poltavets.island.entity.Cell;
import com.javarush.NWA51.Poltavets.island.entity.Result;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public class GUIView implements View {

    private final Stage stage;
    private final GridRenderer gridRenderer;
    private MainController mainController;

    public GUIView(Stage stage) {
        this.stage = stage;
        this.gridRenderer = new GridRenderer();
    }

    public void setMainController(MainController mainController) {
        this.mainController = mainController;
    }

    @Override
    public void show(Cell[][] island) {
        StackPane overlayRoot = new StackPane();

        Node islandNode = gridRenderer.render(island, "Ява", island.length, island[0].length, () -> {
            // Создаем плашку с анимированными эмодзи
            HBox newDayBox = new HBox(10);
            newDayBox.setAlignment(Pos.CENTER);
            newDayBox.setStyle(
                    "-fx-background-color: rgba(255,255,224,0.9);" +
                            "-fx-padding: 15;" +
                            "-fx-font-size: 18;" +
                            "-fx-font-weight: bold;"
            );

            Label label = new Label("☀ Новый день наступает ☾");
            newDayBox.getChildren().add(label);
            StackPane.setAlignment(newDayBox, Pos.TOP_CENTER);
            overlayRoot.getChildren().add(newDayBox);

            // Анимация: меняем эмодзи каждую 0.3 секунды
            String[] spinnerEmojis = {"🌞", "🌅", "🌄", "☀️"};
            final int[] index = {0};
            Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(0.3), event -> {
                label.setText(spinnerEmojis[index[0]] + " Новый день наступает ");
                index[0] = (index[0] + 1) % spinnerEmojis.length;
            }));
            timeline.setCycleCount(Timeline.INDEFINITE);
            timeline.play();

            // Запускаем пересчёт дня
            Platform.runLater(() -> {
                if (mainController != null) mainController.nextDay();

                // После пересчёта останавливаем анимацию и скрываем плашку
                timeline.stop();
                overlayRoot.getChildren().remove(newDayBox);
            });
        });

        overlayRoot.getChildren().add(islandNode);

        BorderPane root = new BorderPane();
        root.setCenter(overlayRoot);

        Scene scene = new Scene(root, 1200, 650); // увеличенная высота
        stage.setScene(scene);
        stage.setTitle("Симуляция острова");
        stage.show();
    }

    public void showSettingsWindow(Runnable onSettingsConfirmed) {
        try {
            InputStream islandStream = getClass().getResourceAsStream("/repository/islandDefault.prm");
            InputStream animalsStream = getClass().getResourceAsStream("/repository/animalsDefault.prm");

            if (islandStream == null || animalsStream == null) {
                throw new RuntimeException("Файлы настроек не найдены в ресурсах!");
            }

            String defaultIsland = new String(islandStream.readAllBytes(), StandardCharsets.UTF_8);
            String defaultAnimals = new String(animalsStream.readAllBytes(), StandardCharsets.UTF_8);

            javafx.scene.control.TextArea islandArea = new javafx.scene.control.TextArea(defaultIsland);
            islandArea.setPrefRowCount(20);
            javafx.scene.control.TextArea animalsArea = new javafx.scene.control.TextArea(defaultAnimals);
            animalsArea.setPrefRowCount(20);

            javafx.scene.control.Button saveButton = new javafx.scene.control.Button("Сохранить и начать");
            javafx.scene.control.Button cancelButton = new javafx.scene.control.Button("Отмена (использовать дефолт)");

            HBox buttons = new HBox(10, saveButton, cancelButton);
            VBox root = new VBox(10, new javafx.scene.control.Label("Настройки острова:"), islandArea,
                    new javafx.scene.control.Label("Настройки животных:"), animalsArea, buttons);
            root.setPadding(new javafx.geometry.Insets(10));

            Scene scene = new Scene(root, 800, 800);
            stage.setScene(scene);
            stage.setTitle("Настройки симуляции острова");
            stage.show();

            saveButton.setOnAction(e -> {
                if (mainController != null) {
                    mainController.saveSettings(islandArea.getText(), animalsArea.getText());
                }
                onSettingsConfirmed.run();
            });

            cancelButton.setOnAction(e -> onSettingsConfirmed.run());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public String[] getParameters() {
        return new String[0];
    }

    @Override
    public void printResult(Result result) {
        // Реализация вывода результатов, если нужно
    }
}
