package com.javarush.NWA51.Poltavets.island.view;

import com.javarush.NWA51.Poltavets.island.entity.Cell;
import com.javarush.NWA51.Poltavets.island.entity.Result;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class GUIView implements View {

    private final Stage stage;
    private final GridRenderer gridRenderer;
    private final InfoPanel infoPanel;

    public GUIView(Stage stage) {
        this.stage = stage;
        this.gridRenderer = new GridRenderer();
        this.infoPanel = new InfoPanel();
    }

    @Override
    public void show(Cell[][] island) {
        BorderPane root = new BorderPane();
        root.setCenter(gridRenderer.render(island));
        root.setRight(infoPanel.render("Ява", island.length, island[0].length));

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
        // TODO
    }
}

