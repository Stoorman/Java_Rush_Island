package com.javarush.NWA51.Poltavets.island.service;

import com.javarush.NWA51.Poltavets.island.entity.Cell;

public class IslandInitialization {
    private String nameIsland;
    private int xAxisSize;
    private int yAxisSize;
    private Cell[][] island;


    String[] cellParameters = {"0","0","4"};  // TODO Убрать отладочный массив


    public IslandInitialization(String[] parameters) {
        this.nameIsland = parameters[0];
        xAxisSize = Integer.parseInt(parameters[1]);
        yAxisSize = Integer.parseInt(parameters[2]);

        // TODO написать разбивку входного массива по параметрам
        this.island = new Cell[xAxisSize][yAxisSize]; //создаём массив ячеек для будущего острова
        for (int i = 0; i < xAxisSize; i++) {
            for (int j = 0; j < yAxisSize; j++) {
                cellParameters[0]=Integer.toString(i);
                cellParameters[1]=Integer.toString(j);
                island[i][j] = new Cell(cellParameters);
            }
        }
        // TODO написать реализацию генерации каждой ячейки

    }


    public void print() {
        System.out.println("Название острова - " + nameIsland);
        for(Cell[] c:island) {
            for(Cell z:c) {
                z.print();
            }
        }
    }


    //Далле отладочный код, который потом надо закомментить


}
