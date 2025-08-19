package com.javarush.NWA51.Poltavets.island.entity;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Random;

public class Cell {
    private int xAxis;                      //Координата ячейки по х
    private int yAxis;                      //Координата ячейки по y
    private Double grass;                   //Количество травы в ячейке
    private Double grassMax;                //Максимальное количество травы в ячейке
    private int soilType;                   //Тип почвы
    private Cell[][] islands;               //Копия острова для перемещения
    private List<Animals> animalsList;      //Список животных


    public Cell(String[] parametersCell) {
        this.xAxis = Integer.parseInt(parametersCell[0]);
        this.yAxis = Integer.parseInt(parametersCell[1]);
        switch (Integer.parseInt(parametersCell[2])) {       // прописывается почва или генерируется случайно
            case 0, 1, 2, 3 -> this.soilType = Integer.parseInt(parametersCell[2]);
            case 4 -> this.soilType = randomInt(0, 3);
            default -> this.soilType = 2;
        }
        this.grassMax = 200.0;        //  TODO прописать механизм смены количества травы в записимости от почвы
        this.grass = randomDouble(0.0, grassMax);
        //  TODO генерация случайного количества животных в ячейке
    }

    //Метод генерирует случайное целое число(int) [minValue...maxValue]
    private int randomInt(int minValue, int maxValue) {
        Random random = new Random();
        return random.nextInt(maxValue - minValue + 1) + minValue;

    }

    //Метод генерирует случайное натуральное число(Double) [minValue...maxValue]
    private Double randomDouble(Double minValue, Double maxValue) {
        Random random = new Random();
        double step = 0.05; // кратность округления
        // Случайное число в диапазоне [minValue, maxValue)
        double rawValue = minValue + (maxValue - minValue) * random.nextDouble();
        // Округление к ближайшему шагу
        BigDecimal bdValue = BigDecimal.valueOf(rawValue);   //TODO посмотреть BigDecimal и RoundingMode
        BigDecimal bdStep = BigDecimal.valueOf(step);
        BigDecimal rounded = bdValue.divide(bdStep, 0, RoundingMode.HALF_UP)
                .multiply(bdStep);
        return rounded.doubleValue();
    }

    public void print() {
        System.out.println(" X" + xAxis + " Y" + yAxis + " Тип почвы - " + soilType + ", Кол-во травы - " + grass);
    }


    public Cell[][] runAnimals(Cell[][] Islands) {         //Метод перемещает животных по ячейкам
        // TODO написать реализацию перемещения животных
        return null;
    }


    public void nextDayCell() {                                   //Действия при смене дня
        grassGrowt();        //рост травы
        addAgeAnimals();     //прибавляется возраст животных
        animalHunger();      //снижение уровня сытости животных
        deleteDeadAnimals(); //удаляются мёртвые животные(голодные и старые)
        birthAnimals();      //рожаются новые животные
        predatorsHunt();     //хищники охотятся
        herbivoresEat();     //травоятные едят траву


    }

    public void grassGrowt() {
        // TODO Написать реализацию роста травы
        return;
    }

    private void addAgeAnimals() {
    }

    private void animalHunger() {
    }

    private void deleteDeadAnimals() {
    }

    private void birthAnimals() {
    }

    private void predatorsHunt() {
    }

    private void herbivoresEat() {
    }

}
