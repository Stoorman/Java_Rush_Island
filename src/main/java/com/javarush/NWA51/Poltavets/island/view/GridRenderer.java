package com.javarush.NWA51.Poltavets.island.view;

import com.javarush.NWA51.Poltavets.island.entity.Animals;
import com.javarush.NWA51.Poltavets.island.entity.Cell;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
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
    private HBox legend;
    private Map<String, Color> animalColors;

    public Node render(Cell[][] island, String islandName, int width, int height, Runnable onNextDay) {
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
                grid.add(rect, y + SEA_WIDTH, x + SEA_WIDTH);

                if (x >= 0 && x < cols && y >= 0 && y < rows) {
                    Cell cell = island[y][x];
                    for (Map.Entry<Class<? extends Animals>, List<Animals>> entry : cell.getAnimalsMap().entrySet()) {
                        List<Animals> animals = entry.getValue();
                        if (!animals.isEmpty()) {
                            String animalName = animals.get(0).getAnimalTypeName();
                            animalColors.putIfAbsent(animalName, Color.GREEN);
                        }
                    }
                }
            }
        }

        StackPane gridContainer = new StackPane(grid);

        String fontStyle = "-fx-font-family: 'Segoe UI'; -fx-font-size: 14;";

        // Радиокнопки в два ряда
        ToggleGroup toggleGroup = new ToggleGroup();
        RadioButton rbBiome = new RadioButton("Биом");
        rbBiome.setToggleGroup(toggleGroup);
        rbBiome.setSelected(true);
        rbBiome.setStyle(fontStyle);

        GridPane radioGrid = new GridPane();
        radioGrid.setHgap(20);
        radioGrid.setVgap(5);
        radioGrid.setAlignment(Pos.CENTER);
        radioGrid.add(rbBiome, 0, 0);

        int col = 1, row = 0;
        int maxCols = (int) Math.ceil((animalColors.size() + 1) / 2.0);

        for (String animalName : animalColors.keySet()) {
            RadioButton rb = new RadioButton(animalName);
            rb.setToggleGroup(toggleGroup);
            rb.setStyle(fontStyle);
            radioGrid.add(rb, col, row);
            col++;
            if (col >= maxCols) {
                col = 0;
                row++;
            }
        }

        // Легенда радиокнопок — горизонтальная под островом
        legend = new HBox(10);
        legend.setAlignment(Pos.CENTER);
        legend.setPadding(new Insets(10));
        legend.setStyle(fontStyle);

        toggleGroup.selectedToggleProperty().addListener((obs, oldV, newV) -> updateGrid(toggleGroup));
        updateGrid(toggleGroup);

        // Информация об острове
        Node infoPanel = new InfoPanel().render(islandName, width, height);

        // Кнопка "Новый день"
        Button nextDayButton = new Button("Новый день");
        nextDayButton.setOnAction(e -> onNextDay.run());

        // Верхний блок: информация + кнопка + радиокнопки
        VBox topBox = new VBox(10);
        topBox.setAlignment(Pos.CENTER);
        topBox.setPadding(new Insets(10));
        topBox.getChildren().addAll(infoPanel, nextDayButton, radioGrid);

        // Центрируем остров и легенду по горизонтали
        VBox islandWithLegend = new VBox(10, gridContainer, legend);
        islandWithLegend.setAlignment(Pos.TOP_CENTER);

        HBox centerHBox = new HBox(islandWithLegend);
        centerHBox.setAlignment(Pos.TOP_CENTER); // горизонтальный центр
        centerHBox.setPadding(new Insets(0, 0, 10, 0));

        BorderPane root = new BorderPane();
        root.setTop(topBox);
        root.setCenter(centerHBox);

        return root;
    }

    private void updateGrid(ToggleGroup toggleGroup) {
        String selected = toggleGroup.getSelectedToggle() instanceof RadioButton rb ? rb.getText() : "Биом";
        legend.getChildren().clear();

        int rows = island.length;
        int cols = island[0].length;

        for (int x = -SEA_WIDTH; x < cols + SEA_WIDTH; x++) {
            for (int y = -SEA_WIDTH; y < rows + SEA_WIDTH; y++) {
                Rectangle rect = (Rectangle) getNodeFromGridPane(grid, y + SEA_WIDTH, x + SEA_WIDTH);

                if (x >= 0 && x < cols && y >= 0 && y < rows) {
                    Cell cell = island[y][x];
                    Color color;

                    if ("Биом".equals(selected)) {
                        color = getColorForSoil(cell.getSoilType());
                    } else {
                        List<Animals> animals = cell.getAnimalsMap().values().stream()
                                .filter(list -> !list.isEmpty() && list.get(0).getAnimalTypeName().equals(selected))
                                .findFirst()
                                .orElse(List.of());

                        int count = animals.size();
                        int max = animals.isEmpty() ? 1 : animals.get(0).getValueMax();
                        Color baseColor = animalColors.getOrDefault(selected, Color.GREEN);
                        color = getColorForAnimals(count, max, baseColor);
                    }
                    rect.setFill(color);
                } else {
                    rect.setFill(Color.LIGHTBLUE);
                }
            }
        }

        if ("Биом".equals(selected)) {
            legend.getChildren().addAll(
                    createLegendItem(Color.SANDYBROWN, "Пустыня"),
                    createLegendItem(Color.FORESTGREEN, "Лес"),
                    createLegendItem(Color.GOLDENROD, "Саванна"),
                    createLegendItem(Color.DARKGREEN, "Джунгли")
            );
        } else {
            Color baseColor = animalColors.getOrDefault(selected, Color.GREEN);
            legend.getChildren().addAll(
                    createLegendItem(getColorForAnimals(0, 1, baseColor), "0%"),
                    createLegendItem(getColorForAnimals(1, 4, baseColor), "25%"),
                    createLegendItem(getColorForAnimals(2, 4, baseColor), "50%"),
                    createLegendItem(getColorForAnimals(3, 4, baseColor), "75%"),
                    createLegendItem(getColorForAnimals(4, 4, baseColor), "100%")
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
        item.setAlignment(Pos.CENTER);
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
