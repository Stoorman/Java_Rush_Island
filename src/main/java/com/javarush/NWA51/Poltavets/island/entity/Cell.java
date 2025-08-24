package com.javarush.NWA51.Poltavets.island.entity;

import com.javarush.NWA51.Poltavets.island.service.AnimalFactory;
import com.javarush.NWA51.Poltavets.island.service.RandomValue;
import com.javarush.NWA51.Poltavets.island.service.dto.IslandConfigDTO;

import java.util.List;
import java.util.Map;

public class Cell {
    private int xAxis;                                                    //Координата ячейки по х
    private int yAxis;                                                    //Координата ячейки по y
    private Double grass;                                                 //Количество травы в ячейке
    private int grassMax;                                                 //Максимальное количество травы в ячейке
    private int grassGrowth;                                              //Скорость роста травы в процентах в сутки
    private int soilType;                                                 //Тип почвы
    private Cell[][] islands;                                             //Копия острова для перемещения
    private Map<Class<? extends Animals>, Integer> maxNumberAnimalMap;    //Карта класс - max кол-во в ячейке
    private Map<Class<? extends Animals>, List<Animals>> AnimalsMap;      //Карта класс - Список животных этого класса


    public Cell(int xAxis, int yAxis, IslandConfigDTO parametersCell) {
        this.xAxis = xAxis;
        this.yAxis = yAxis;
        switch (parametersCell.getSoilType()) {       // прописывается почва или генерируется случайно
            case 0, 1, 2, 3 -> this.soilType = parametersCell.getSoilType();
            default -> this.soilType = RandomValue.randomInt(0, 3);
        }
        this.grass = RandomValue.randomDouble(0.0, grassMax * 1.0);
        switch (soilType) {                          //прописываем скорость роста травы и максимальное количество в зависимости от почвы
            case 0 -> {
                this.grassGrowth = parametersCell.getGrassGrowthDesert();
                this.grassMax = parametersCell.getGrassMaxDesert();
            }
            case 1 -> {
                this.grassGrowth = parametersCell.getGrassGrowthForest();
                this.grassMax = parametersCell.getGrassMaxForest();
            }
            case 2 -> {
                this.grassGrowth = parametersCell.getGrassGrowthSavvanna();
                this.grassMax = parametersCell.getGrassMaxSavvanna();
            }
            case 3 -> {
                this.grassGrowth = parametersCell.getGrassGrowthJungle();
                this.grassMax = parametersCell.getGrassMaxJungle();
            }
        }
        AnimalFactory animalFactory = new AnimalFactory();
        try {
            this.AnimalsMap = animalFactory.createAnimal();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        // TODO Переработать обработку ошибок
    }

    public void print() {
        System.out.println(" X" + xAxis + " Y" + yAxis + " Тип почвы - " + soilType + ", Кол-во травы - " + grass);
    }


//    public Cell[][] runAnimals(Cell[][] Islands) {         //Метод перемещает животных по ячейкам
//        // TODO написать реализацию перемещения животных
//        return null;
//    }


    public void nextDayCell() {                                   //Действия при смене дня
        grassGrowt();          //рост травы
//        addAgeAnimals();     //прибавляется возраст животных
//        animalHunger();      //снижение уровня сытости животных
//        deleteDeadAnimals(); //удаляются мёртвые животные(голодные и старые)
//        birthAnimals();      //рожаются новые животные
//        predatorsHunt();     //хищники охотятся
//        herbivoresEat();     //травоятные едят траву
    }

    public void grassGrowt() {
        grass += grassGrowth;
        if(grass >= grassMax)
            grass = 1.0*grassMax;}

//
//    private void addAgeAnimals() {
//    }
//
//    private void animalHunger() {
//    }
//
//    private void deleteDeadAnimals() {
//    }
//
//    private void birthAnimals() {
//    }
//
//    private void predatorsHunt() {
//    }
//
//    private void herbivoresEat() {
//    }

}


