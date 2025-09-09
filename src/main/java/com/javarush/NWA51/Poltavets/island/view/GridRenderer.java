package com.javarush.NWA51.Poltavets.island.view;

import com.javarush.NWA51.Poltavets.island.entity.Animals;
import com.javarush.NWA51.Poltavets.island.entity.Cell;
import javafx.animation.PauseTransition;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.util.Duration;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.util.Map.entry;

public class GridRenderer {

    private static final int CELL_SIZE = 20;
    private static final int SEA_WIDTH = 3;

    private Cell[][] island;
    private GridPane grid;
    private HBox legend;
    private Map<String, Color> animalColors;
    private VBox cellInfoBox;
    private StackPane overlayPane;

    private final Map<String, String> animalEmojiMap = Map.ofEntries(
            entry("Медведь", "🐻"), entry("Орёл", "🦅"), entry("Лиса", "🦊"), entry("Удав", "🐍"),
            entry("Волк", "🐺"), entry("Кабан", "🐗"), entry("Буйвол", "🐃"), entry("Гусеница", "🐛"),
            entry("Олень", "🦌"), entry("Утка", "🦆"), entry("Коза", "🐐"), entry("Лошадь", "🐎"),
            entry("Мышь", "🐁"), entry("Кролик", "🐇"), entry("Овца", "🐑")
    );

    public Node render(Cell[][] island, String islandName, int width, int height, Runnable onNextDay) {
        this.island = island;
        int rows = island.length;
        int cols = island[0].length;

        grid = new GridPane();
        animalColors = new HashMap<>();

        cellInfoBox = new VBox(10);
        cellInfoBox.setPadding(new Insets(10));
        cellInfoBox.setAlignment(Pos.TOP_LEFT);
        cellInfoBox.setMinSize(250, 450);
        cellInfoBox.setPrefSize(250, 450);
        cellInfoBox.setMaxSize(250, 450);

        ScrollPane scrollPane = new ScrollPane(cellInfoBox);
        scrollPane.setFitToWidth(true);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scrollPane.setMinSize(250, 450);
        scrollPane.setPrefSize(250, 450);
        scrollPane.setMaxSize(250, 450);

        for (int x = -SEA_WIDTH; x < cols + SEA_WIDTH; x++) {
            for (int y = -SEA_WIDTH; y < rows + SEA_WIDTH; y++) {
                Rectangle rect = new Rectangle(CELL_SIZE, CELL_SIZE);
                rect.setStroke(Color.BLACK);
                rect.setStrokeWidth(0.2);
                grid.add(rect, y + SEA_WIDTH, x + SEA_WIDTH);

                int finalX = x;
                int finalY = y;
                rect.setOnMouseClicked(e -> {
                    if (finalX >= 0 && finalX < cols && finalY >= 0 && finalY < rows) {
                        showCellInfo(island[finalY][finalX]);
                    } else {
                        showSeaInfo();
                    }
                });

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

        overlayPane = new StackPane();
        overlayPane.getChildren().add(grid);

        String fontStyle = "-fx-font-family: 'Segoe UI'; -fx-font-size: 14;";

        ToggleGroup toggleGroup = new ToggleGroup();
        RadioButton rbBiome = new RadioButton("Биом");
        rbBiome.setToggleGroup(toggleGroup);
        rbBiome.setSelected(true);
        rbBiome.setStyle(fontStyle);

        RadioButton rbGrass = new RadioButton("Трава");
        rbGrass.setToggleGroup(toggleGroup);
        rbGrass.setStyle(fontStyle);

        GridPane radioGrid = new GridPane();
        radioGrid.setHgap(20);
        radioGrid.setVgap(5);
        radioGrid.setAlignment(Pos.CENTER);
        radioGrid.add(rbBiome, 0, 0);

        int col = 1, row = 0;
        int maxCols = (int) Math.ceil((animalColors.size() + 2) / 2.0);

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
        radioGrid.add(rbGrass, col, row);

        legend = new HBox(10);
        legend.setAlignment(Pos.CENTER);
        legend.setPadding(new Insets(10));
        legend.setStyle(fontStyle);

        toggleGroup.selectedToggleProperty().addListener((obs, oldV, newV) -> updateGrid(toggleGroup));
        updateGrid(toggleGroup);

        Node infoPanel = new InfoPanel().render(islandName, width, height);

        Button nextDayButton = new Button("Новый день");
        nextDayButton.setOnAction(e -> showNewDayOverlay(onNextDay));

        VBox topBox = new VBox(10);
        topBox.setAlignment(Pos.CENTER);
        topBox.setPadding(new Insets(10));
        topBox.getChildren().addAll(infoPanel, nextDayButton, radioGrid);

        VBox islandWithLegend = new VBox(10, overlayPane, legend);
        islandWithLegend.setAlignment(Pos.TOP_CENTER);

        HBox centerHBox = new HBox(10, islandWithLegend, scrollPane);
        centerHBox.setAlignment(Pos.TOP_CENTER);
        centerHBox.setPadding(new Insets(0, 0, 10, 0));

        BorderPane root = new BorderPane();
        root.setTop(topBox);
        root.setCenter(centerHBox);

        return root;
    }

    // ------------------ Плашка нового дня ------------------
    private void showNewDayOverlay(Runnable onNextDay) {
        HBox notification = new HBox(10);
        notification.setStyle("-fx-background-color: rgba(255, 255, 224, 0.9); -fx-padding: 15; -fx-background-radius: 10;");
        notification.setAlignment(Pos.CENTER);

        // Символ дня/солнца вместо эмодзи
        Text text = new Text("Новый день наступает!");
        text.setStyle("-fx-font-size: 18; -fx-font-weight: bold; -fx-font-family: 'Segoe UI';");
        notification.getChildren().add(text);

        overlayPane.getChildren().add(notification);
        StackPane.setAlignment(notification, Pos.TOP_CENTER);

        PauseTransition pauseBeforeUpdate = new PauseTransition(Duration.millis(300));
        pauseBeforeUpdate.setOnFinished(e -> {
            onNextDay.run();
            overlayPane.getChildren().remove(notification);
        });
        pauseBeforeUpdate.play();
    }

    // ------------------ Остальные методы ------------------
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
                    } else if ("Трава".equals(selected)) {
                        double ratio = Math.min(cell.getGrass() / cell.getGrassMax(), 1.0);
                        color = Color.web("#ADFF2F").interpolate(Color.GREEN, ratio);
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
        } else if ("Трава".equals(selected)) {
            legend.getChildren().addAll(
                    createLegendItem(Color.web("#ADFF2F"), "0%"),
                    createLegendItem(Color.GREEN.interpolate(Color.web("#ADFF2F"), 0.25), "25%"),
                    createLegendItem(Color.GREEN.interpolate(Color.web("#ADFF2F"), 0.5), "50%"),
                    createLegendItem(Color.GREEN.interpolate(Color.web("#ADFF2F"), 0.75), "75%"),
                    createLegendItem(Color.GREEN, "100%")
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

    private void showCellInfo(Cell cell) {
        cellInfoBox.getChildren().clear();
        Text title = new Text("🌱 Биом: " + getSoilName(cell.getSoilType()));
        title.setStyle("-fx-font-size: 16; -fx-font-weight: bold;");
        Text grassInfo = new Text("Трава: " + (int) cell.getGrass() + "/" + cell.getGrassMax());

        VBox animalsBox = new VBox(2);
        int totalAnimals = 0;
        for (Map.Entry<Class<? extends Animals>, List<Animals>> entry : cell.getAnimalsMap().entrySet()) {
            List<Animals> list = entry.getValue();
            if (!list.isEmpty()) {
                String name = list.get(0).getAnimalTypeName();
                String emoji = animalEmojiMap.getOrDefault(name, "");
                int count = list.size();
                totalAnimals += count;
                animalsBox.getChildren().add(new Text(emoji + " " + name + ": " + count));
            }
        }

        Text bornInfo = new Text("👶 Родилось: " + cell.getBornTotal());
        Text deadInfo = new Text("☠ Умерло: " + cell.getDeadTotal());
        Text totalInfo = new Text("🔢 Всего животных: " + totalAnimals);

        cellInfoBox.getChildren().addAll(title, grassInfo, new Text("Животные:"), animalsBox, bornInfo, deadInfo, totalInfo);
    }

    private void showSeaInfo() {
        cellInfoBox.getChildren().clear();
        Text seaText = new Text(
                "🌊🌊🌊\n" +
                        "Это море!\n" +
                        "Корабли лежат разбиты,\n" +
                        "Сундуки стоят раскрыты.\n" +
                        "Изумруды и рубины осыпаются дождём.\n" +
                        "Если хочешь быть богатым,\n" +
                        "Если хочешь быть счастливым,\n" +
                        "Оставайся, мальчик, с нами -\n" +
                        "Будешь нашим королём,\n" +
                        "Будешь нашим королём."
        );
        seaText.setStyle("-fx-font-size: 14;");
        cellInfoBox.getChildren().add(seaText);
    }

    private String getSoilName(int soilType) {
        return switch (soilType) {
            case 0 -> "Пустыня";
            case 1 -> "Лес";
            case 2 -> "Саванна";
            case 3 -> "Джунгли";
            default -> "Неизвестно";
        };
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
