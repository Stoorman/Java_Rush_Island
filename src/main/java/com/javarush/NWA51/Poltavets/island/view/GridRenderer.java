package com.javarush.NWA51.Poltavets.island.view;

import com.javarush.NWA51.Poltavets.island.entity.Animals;
import com.javarush.NWA51.Poltavets.island.entity.Cell;
import com.javarush.NWA51.Poltavets.island.entity.herbivore.Rabbit;
import com.javarush.NWA51.Poltavets.island.entity.predator.Wolf;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

import java.util.List;

public class GridRenderer {

    private static final int CELL_SIZE = 20;
    private static final int SEA_WIDTH = 3;

    private Cell[][] island;
    private GridPane grid;
    private VBox legend;

    public Node render(Cell[][] island) {
        this.island = island;

        int rows = island.length;
        int cols = island[0].length;

        grid = new GridPane();

        // Инициализация сетки
        for (int y = -SEA_WIDTH; y < rows + SEA_WIDTH; y++) {
            for (int x = -SEA_WIDTH; x < cols + SEA_WIDTH; x++) {
                Rectangle rect = new Rectangle(CELL_SIZE, CELL_SIZE);
                rect.setStroke(Color.BLACK);
                rect.setStrokeWidth(0.2);
                grid.add(rect, y + SEA_WIDTH, x + SEA_WIDTH); // y=row, x=col
            }
        }

        StackPane gridContainer = new StackPane(grid);
        gridContainer.setAlignment(Pos.CENTER);

        // Радиокнопки для выбора режима окраски
        ToggleGroup toggleGroup = new ToggleGroup();
        RadioButton rbSoil = new RadioButton("Тип почвы");
        RadioButton rbWolves = new RadioButton("Волки");
        RadioButton rbRabbits = new RadioButton("Зайцы");
        rbSoil.setToggleGroup(toggleGroup);
        rbWolves.setToggleGroup(toggleGroup);
        rbRabbits.setToggleGroup(toggleGroup);
        rbSoil.setSelected(true);

        VBox controls = new VBox(5, rbSoil, rbWolves, rbRabbits);
        controls.setAlignment(Pos.CENTER_LEFT);
        controls.setPadding(new Insets(10));

        // Легенда
        legend = new VBox(5);
        legend.setAlignment(Pos.CENTER_LEFT);
        legend.setPadding(new Insets(10));

        // Перерисовка при выборе режима
        toggleGroup.selectedToggleProperty().addListener((obs, oldV, newV) -> updateGrid(rbSoil, rbWolves, rbRabbits));

        // Первичная отрисовка
        updateGrid(rbSoil, rbWolves, rbRabbits);

        HBox root = new HBox(20, gridContainer, new VBox(controls, legend));
        root.setAlignment(Pos.CENTER);

        return root;
    }

    private void updateGrid(RadioButton rbSoil, RadioButton rbWolves, RadioButton rbRabbits) {
        boolean soilMode = rbSoil.isSelected();
        boolean wolfMode = rbWolves.isSelected();
        boolean rabbitMode = rbRabbits.isSelected();

        legend.getChildren().clear();

        int rows = island.length;
        int cols = island[0].length;

        Color animalColor = Color.RED; // по умолчанию
        for (int y = -SEA_WIDTH; y < rows + SEA_WIDTH; y++) {
            for (int x = -SEA_WIDTH; x < cols + SEA_WIDTH; x++) {
                Rectangle rect = (Rectangle) getNodeFromGridPane(grid, y + SEA_WIDTH, x + SEA_WIDTH);

                if (y >= 0 && y < rows && x >= 0 && x < cols) {
                    Cell cell = island[y][x];
                    Color color;

                    if (soilMode) {
                        color = getColorForSoil(cell.getSoilType());
                    } else if (wolfMode) {
                        List<Animals> wolves = cell.getAnimalsMap().getOrDefault(Wolf.class, List.of());
                        int count = wolves.size();
                        int max = wolves.isEmpty() ? 1 : wolves.get(0).getValueMax();
                        color = getColorForAnimals(count, max, Color.RED);
                        animalColor = Color.RED;
                    } else { // rabbitMode
                        List<Animals> rabbits = cell.getAnimalsMap().getOrDefault(Rabbit.class, List.of());
                        int count = rabbits.size();
                        int max = rabbits.isEmpty() ? 1 : rabbits.get(0).getValueMax();
                        color = getColorForAnimals(count, max, Color.GREEN);
                        animalColor = Color.GREEN;
                    }

                    rect.setFill(color);
                } else {
                    rect.setFill(Color.LIGHTBLUE);
                }
            }
        }

        // Построение легенды
        if (soilMode) {
            legend.getChildren().addAll(
                    createLegendItem(Color.SANDYBROWN, "Пустыня"),
                    createLegendItem(Color.FORESTGREEN, "Лес"),
                    createLegendItem(Color.GOLDENROD, "Саванна"),
                    createLegendItem(Color.DARKGREEN, "Джунгли")
            );
        } else {
            // легенда для животных динамически с базовым цветом
            legend.getChildren().addAll(
                    createLegendItem(getColorForAnimals(0,1,animalColor), "0%"),
                    createLegendItem(getColorForAnimals(1,4,animalColor), "25%"),
                    createLegendItem(getColorForAnimals(2,4,animalColor), "50%"),
                    createLegendItem(getColorForAnimals(3,4,animalColor), "75%"),
                    createLegendItem(getColorForAnimals(4,4,animalColor), "100%")
            );
        }
    }

    private Color getColorForSoil(int soilType) {
        return switch (soilType) {
            case 0 -> Color.SANDYBROWN;
            case 1 -> Color.FORESTGREEN;
            case 2 -> Color.GOLDENROD;
            case 3 -> Color.DARKGREEN;
            default -> Color.LIGHTGREEN;
        };
    }

    private Color getColorForAnimals(int count, int valueMax, Color baseColor) {
        if (count <= 0 || valueMax <= 0) return Color.LIGHTGRAY;
        double ratio = Math.min((double) count / valueMax, 1.0);
        return Color.LIGHTGRAY.interpolate(baseColor, ratio);
    }

    private HBox createLegendItem(Color color, String label) {
        Rectangle rect = new Rectangle(20, 20, color);
        rect.setStroke(Color.BLACK);
        rect.setStrokeWidth(0.5);
        Text text = new Text(label);
        HBox item = new HBox(5, rect, text);
        item.setAlignment(Pos.CENTER_LEFT);
        return item;
    }

    private Node getNodeFromGridPane(GridPane gridPane, int col, int row) {
        for (Node node : gridPane.getChildren()) {
            Integer nodeCol = GridPane.getColumnIndex(node);
            Integer nodeRow = GridPane.getRowIndex(node);
            if (nodeCol != null && nodeRow != null && nodeCol == col && nodeRow == row) {
                return node;
            }
        }
        return null;
    }
}