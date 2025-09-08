package com.javarush.NWA51.Poltavets.island.view;

import com.javarush.NWA51.Poltavets.island.controller.MainController;
import com.javarush.NWA51.Poltavets.island.entity.Cell;
import com.javarush.NWA51.Poltavets.island.entity.Result;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;

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

    // Показ симуляции острова
    @Override
    public void show(Cell[][] island) {
        BorderPane root = new BorderPane();
        root.setCenter(gridRenderer.render(island));
        root.setRight(infoPanel.render("Ява", island.length, island[0].length));

        Button nextDayButton = new Button("Новый день");
        nextDayButton.setOnAction(e -> {
            if (mainController != null) {
                Platform.runLater(mainController::nextDay);
            }
        });

        VBox controlsBox = new VBox(10, nextDayButton);
        controlsBox.setPadding(new Insets(10));
        root.setBottom(controlsBox);

        Scene scene = new Scene(root, 1200, 600);
        stage.setScene(scene);
        stage.setTitle("Симуляция острова");
        stage.show();
    }

    // Показ окна настроек перед симуляцией с callback после подтверждения
    public void showSettingsWindow(Runnable onSettingsConfirmed) {
        try {
            // Чтение файлов настроек из classpath
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
            root.setPadding(new Insets(10));

            Scene scene = new Scene(root, 800, 800);
            stage.setScene(scene);
            stage.setTitle("Настройки симуляции острова");
            stage.show();

            saveButton.setOnAction(e -> {
                if (mainController != null) {
                    // Сохраняем изменения
                    mainController.saveSettings(islandArea.getText(), animalsArea.getText());
                }
                onSettingsConfirmed.run();
            });

            cancelButton.setOnAction(e -> {
                // Используем дефолтные настройки
                onSettingsConfirmed.run();
            });

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
    }
}
