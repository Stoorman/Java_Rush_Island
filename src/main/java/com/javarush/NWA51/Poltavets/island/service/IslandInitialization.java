package com.javarush.NWA51.Poltavets.island.service;

import com.javarush.NWA51.Poltavets.island.entity.Cell;

public class IslandInitialization {
    private int xAxisSize;
    private int yAxisSize;
    private Cell[][] island;


    public IslandInitialization(String[] parameters){
       // TODO написать разбивку входного массива по параметрам

        this.island = new Cell[xAxisSize][yAxisSize]; //создаём массив ячеек для будущего острова
        // TODO написать реализацию генерации каждой ячейки
    }






}
