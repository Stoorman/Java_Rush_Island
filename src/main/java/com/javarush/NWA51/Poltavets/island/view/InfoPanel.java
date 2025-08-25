package com.javarush.NWA51.Poltavets.island.view;

import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

public class InfoPanel {

    public Node render(String islandName, int width, int height) {
        VBox box = new VBox(10);
        box.getChildren().addAll(
                new Label("Название: " + islandName),
                new Label("Размер: " + width + " x " + height)
        );
        return box;
    }
}
