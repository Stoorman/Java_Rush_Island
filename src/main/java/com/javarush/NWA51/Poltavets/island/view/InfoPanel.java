package com.javarush.NWA51.Poltavets.island.view;

import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.geometry.Pos;

public class InfoPanel {

    public Node render(String islandName, int width, int height) {
        VBox box = new VBox(5);
        box.setAlignment(Pos.CENTER);

        Label nameLabel = new Label(islandName); // убрали слово "Название"
        Label sizeLabel = new Label(width + " x " + height);

        // Увеличиваем шрифт в 2 раза
        String style = "-fx-font-size: 28px; -fx-font-weight: bold; -fx-font-family: 'Segoe UI';";
        nameLabel.setStyle(style);
        sizeLabel.setStyle(style);

        box.getChildren().addAll(nameLabel, sizeLabel);
        return box;
    }
}