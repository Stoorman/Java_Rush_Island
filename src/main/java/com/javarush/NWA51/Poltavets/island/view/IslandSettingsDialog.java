package com.javarush.NWA51.Poltavets.island.view;

import com.javarush.NWA51.Poltavets.island.service.dto.IslandConfigDTO;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class IslandSettingsDialog {

    private IslandConfigDTO islandConfig;

    public IslandSettingsDialog(Stage owner) {
        Stage dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.initOwner(owner);

        GridPane grid = new GridPane();
        grid.setPadding(new Insets(10));
        grid.setVgap(5);
        grid.setHgap(10);

        TextField nameField = new TextField("Ява");
        TextField xSizeField = new TextField("15");
        TextField ySizeField = new TextField("10");
        TextField soilField = new TextField("0");

        TextField grassDesertGrowth = new TextField("50");
        TextField grassDesertMax = new TextField("200");
        TextField grassForestGrowth = new TextField("150");
        TextField grassForestMax = new TextField("400");
        TextField grassJungleGrowth = new TextField("600");
        TextField grassJungleMax = new TextField("1000");
        TextField grassSavvannaGrowth = new TextField("300");
        TextField grassSavvannaMax = new TextField("600");

        grid.addRow(0, new javafx.scene.control.Label("Имя острова:"), nameField);
        grid.addRow(1, new javafx.scene.control.Label("Размер X:"), xSizeField);
        grid.addRow(2, new javafx.scene.control.Label("Размер Y:"), ySizeField);
        grid.addRow(3, new javafx.scene.control.Label("Тип почвы:"), soilField);
        grid.addRow(4, new javafx.scene.control.Label("Рост травы пустыня:"), grassDesertGrowth);
        grid.addRow(5, new javafx.scene.control.Label("Макс трава пустыня:"), grassDesertMax);
        grid.addRow(6, new javafx.scene.control.Label("Рост травы лес:"), grassForestGrowth);
        grid.addRow(7, new javafx.scene.control.Label("Макс трава лес:"), grassForestMax);
        grid.addRow(8, new javafx.scene.control.Label("Рост травы джунгли:"), grassJungleGrowth);
        grid.addRow(9, new javafx.scene.control.Label("Макс трава джунгли:"), grassJungleMax);
        grid.addRow(10, new javafx.scene.control.Label("Рост травы саванна:"), grassSavvannaGrowth);
        grid.addRow(11, new javafx.scene.control.Label("Макс трава саванна:"), grassSavvannaMax);

        Button startButton = new Button("Создать остров");
        startButton.setOnAction(e -> {
            islandConfig = new IslandConfigDTO.Builder()
                    .nameIsland(nameField.getText())
                    .xAxisSize(Integer.parseInt(xSizeField.getText()))
                    .yAxisSize(Integer.parseInt(ySizeField.getText()))
                    .soilType(Integer.parseInt(soilField.getText()))
                    .grassDesert(Integer.parseInt(grassDesertGrowth.getText()), Integer.parseInt(grassDesertMax.getText()))
                    .grassForest(Integer.parseInt(grassForestGrowth.getText()), Integer.parseInt(grassForestMax.getText()))
                    .grassJungle(Integer.parseInt(grassJungleGrowth.getText()), Integer.parseInt(grassJungleMax.getText()))
                    .grassSavvanna(Integer.parseInt(grassSavvannaGrowth.getText()), Integer.parseInt(grassSavvannaMax.getText()))
                    .build();
            dialog.close();
        });

        grid.add(startButton, 1, 12);

        Scene scene = new Scene(grid);
        dialog.setScene(scene);
        dialog.setTitle("Настройки острова");
        dialog.showAndWait();
    }

    public IslandConfigDTO getIslandConfig() {
        return islandConfig;
    }
}

