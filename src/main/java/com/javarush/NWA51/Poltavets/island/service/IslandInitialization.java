package com.javarush.NWA51.Poltavets.island.service;

import com.javarush.NWA51.Poltavets.island.entity.Cell;
import com.javarush.NWA51.Poltavets.island.service.dto.IslandConfigDTO;

public class IslandInitialization {
    private String nameIsland;
    private int xAxisSize;
    private int yAxisSize;
    private Cell[][] island;

    public IslandInitialization(IslandConfigDTO islandConfigDTO) {
        this.nameIsland = islandConfigDTO.getNameIsland();
        this.xAxisSize = islandConfigDTO.getxAxisSize();
        this.yAxisSize = islandConfigDTO.getyAxisSize();
        this.island = new Cell[xAxisSize][yAxisSize]; //создаём массив ячеек для будущего острова
        for (int i = 0; i < xAxisSize; i++) {
            for (int j = 0; j < yAxisSize; j++) {
                island[i][j] = new Cell(i,j,islandConfigDTO);

            }
        }
    }

    public Cell[][] getIsland(){
        return island;
    }


    public void print() {
        System.out.println("Название острова - " + nameIsland);
        for (Cell[] c : island) {
            for (Cell z : c) {
                z.print();
            }
        }
    }


    //Далее отладочный код, который потом надо закомментить


}
