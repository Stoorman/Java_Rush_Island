package com.javarush.NWA51.Poltavets.island.view;

import com.javarush.NWA51.Poltavets.island.entity.Animals;
import com.javarush.NWA51.Poltavets.island.entity.Cell;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GridRenderer {

    private static final int CELL_SIZE = 20;
    private static final int SEA_WIDTH = 3;

    private Cell[][] island;
    private GridPane grid;
    private VBox legend;
    private Map<String, Color> animalColors;

    public Node render(Cell[][] island) {
        this.island = island;
        int rows = island.length;
        int cols = island[0].length;

        grid = new GridPane();
        animalColors = new HashMap<>();

        // Инициализация сетки с морем
        for (int x = -SEA_WIDTH; x < cols + SEA_WIDTH; x++) {
            for (int y = -SEA_WIDTH; y < rows + SEA_WIDTH; y++) {
                Rectangle rect = new Rectangle(CELL_SIZE, CELL_SIZE);
                rect.setStroke(Color.BLACK);
                rect.setStrokeWidth(0.2);
                grid.add(rect, y + SEA_WIDTH, x + SEA_WIDTH); // поменяли местами x и y

                // Собираем все типы животных для легенды
                if (x >= 0 && x < cols && y >= 0 && y < rows) {
                    Cell cell = island[y][x];
                    for (Class<? extends Animals> animalClass : cell.getAnimalsMap().keySet()) {
                        animalColors.putIfAbsent(animalClass.getSimpleName(),
                                Color.color(Math.random(), Math.random(), Math.random()));
                    }
                }
            }
        }

        StackPane gridContainer = new StackPane(grid);
        gridContainer.setAlignment(Pos.CENTER);

        // Стиль для кириллицы
        String fontStyle = "-fx-font-family: 'Segoe UI'; -fx-font-size: 14;";

        // Радиокнопки для выбора режима окраски
        ToggleGroup toggleGroup = new ToggleGroup();
        RadioButton rbSoil = new RadioButton("Тип почвы");
        rbSoil.setToggleGroup(toggleGroup);
        rbSoil.setSelected(true);
        rbSoil.setStyle(fontStyle);

        VBox controls = new VBox(5, rbSoil);
        controls.setAlignment(Pos.CENTER_LEFT);
        controls.setPadding(new Insets(10));
        controls.setStyle(fontStyle);

        // Добавляем радио-кнопки для всех животных
        for (String animalName : animalColors.keySet()) {
            RadioButton rb = new RadioButton(animalName);
            rb.setToggleGroup(toggleGroup);
            rb.setStyle(fontStyle);
            controls.getChildren().add(rb);
        }

        // Легенда
        legend = new VBox(5);
        legend.setAlignment(Pos.CENTER_LEFT);
        legend.setPadding(new Insets(10));
        legend.setStyle(fontStyle);

        toggleGroup.selectedToggleProperty().addListener((obs, oldV, newV) -> updateGrid(toggleGroup));

        // Первичная отрисовка
        updateGrid(toggleGroup);

        HBox root = new HBox(20, gridContainer, new VBox(controls, legend));
        root.setAlignment(Pos.CENTER);

        return root;
    }

    private void updateGrid(ToggleGroup toggleGroup) {
        String selected = toggleGroup.getSelectedToggle() instanceof RadioButton rb ? rb.getText() : "Тип почвы";
        legend.getChildren().clear();

        int rows = island.length;
        int cols = island[0].length;

        Color animalColor = Color.RED;

        for (int x = -SEA_WIDTH; x < cols + SEA_WIDTH; x++) {
            for (int y = -SEA_WIDTH; y < rows + SEA_WIDTH; y++) {
                Rectangle rect = (Rectangle) getNodeFromGridPane(grid, y + SEA_WIDTH, x + SEA_WIDTH);

                if (x >= 0 && x < cols && y >= 0 && y < rows) {
                    Cell cell = island[y][x];
                    Color color;

                    if ("Тип почвы".equals(selected)) {
                        color = getColorForSoil(cell.getSoilType());
                    } else {
                        List<Animals> animals = cell.getAnimalsMap().getOrDefault(
                                cell.getAnimalsMap().keySet().stream()
                                        .filter(c -> c.getSimpleName().equals(selected))
                                        .findFirst().orElse(null),
                                List.of()
                        );

                        int count = animals.size();
                        int max = animals.isEmpty() ? 1 : animals.get(0).getValueMax();
                        animalColor = animalColors.getOrDefault(selected, Color.RED);
                        color = getColorForAnimals(count, max, animalColor);
                    }
                    rect.setFill(color);
                } else {
                    rect.setFill(Color.LIGHTBLUE); // море
                }
            }
        }

        // Построение легенды
        if ("Тип почвы".equals(selected)) {
            legend.getChildren().addAll(
                    createLegendItem(Color.SANDYBROWN, "Пустыня"),
                    createLegendItem(Color.FORESTGREEN, "Лес"),
                    createLegendItem(Color.GOLDENROD, "Саванна"),
                    createLegendItem(Color.DARKGREEN, "Джунгли")
            );
        } else {
            // легенда для животных
            Color baseColor = animalColors.getOrDefault(selected, Color.RED);
            legend.getChildren().addAll(
                    createLegendItem(getColorForAnimals(0,1,baseColor), "0%"),
                    createLegendItem(getColorForAnimals(1,4,baseColor), "25%"),
                    createLegendItem(getColorForAnimals(2,4,baseColor), "50%"),
                    createLegendItem(getColorForAnimals(3,4,baseColor), "75%"),
                    createLegendItem(getColorForAnimals(4,4,baseColor), "100%")
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
        text.setStyle("-fx-font-family: 'Segoe UI'; -fx-font-size: 14;");
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
