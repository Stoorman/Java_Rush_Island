package com.javarush.NWA51.Poltavets.island.view;

import com.javarush.NWA51.Poltavets.island.controller.MainController;
import com.javarush.NWA51.Poltavets.island.entity.Cell;
import com.javarush.NWA51.Poltavets.island.entity.Result;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class GUIView implements View {

    private final Stage stage;
    private final GridRenderer gridRenderer;
    private final InfoPanel infoPanel;
    private MainController mainController;

    public GUIView(Stage stage) {
        this.stage = stage;
        this.gridRenderer = new GridRenderer();
        this.infoPanel = new InfoPanel();
    }

    public void setMainController(MainController mainController) {
        this.mainController = mainController;
    }

    @Override
    public void show(Cell[][] island) {
        BorderPane root = new BorderPane();

        // Вызываем GridRenderer.render с названием острова, размерами и Runnable для "Новый день"
        String islandName = "Ява"; // или получаем динамически
        int width = island.length;
        int height = island[0].length;

        root.setCenter(gridRenderer.render(island, islandName, width, height, () -> {
            if (mainController != null) {
                Platform.runLater(mainController::nextDay);
            }
        }));

        Scene scene = new Scene(root, 1200, 600);
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

            TextArea islandArea = new TextArea(defaultIsland);
            islandArea.setPrefRowCount(20);
            TextArea animalsArea = new TextArea(defaultAnimals);
            animalsArea.setPrefRowCount(20);

            Button saveButton = new Button("Сохранить и начать");
            Button cancelButton = new Button("Отмена (использовать дефолт)");

            HBox buttons = new HBox(10, saveButton, cancelButton);
            VBox root = new VBox(10, new Label("Настройки острова:"), islandArea,
                    new Label("Настройки животных:"), animalsArea, buttons);
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
