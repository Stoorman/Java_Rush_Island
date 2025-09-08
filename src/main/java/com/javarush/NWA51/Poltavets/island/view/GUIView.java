package com.javarush.NWA51.Poltavets.island.view;

import com.javarush.NWA51.Poltavets.island.controller.MainController;
import com.javarush.NWA51.Poltavets.island.entity.Cell;
import com.javarush.NWA51.Poltavets.island.entity.Result;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

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

    // Метод для установки контроллера
    public void setMainController(MainController mainController) {
        this.mainController = mainController;
    }

    @Override
    public void show(Cell[][] island) {
        BorderPane root = new BorderPane();

        // Основная сетка острова
        root.setCenter(gridRenderer.render(island));
        root.setRight(infoPanel.render("Ява", island.length, island[0].length));

        // Кнопка "Новый день"
        Button nextDayButton = new Button("Новый день");
        nextDayButton.setOnAction(e -> {
            if (mainController != null) {
                // Запускаем новый день через контроллер
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

    @Override
    public String[] getParameters() {
        return new String[0];
    }

    @Override
    public void printResult(Result result) {
        // TODO: можно выводить статус или результат дня
    }
}
