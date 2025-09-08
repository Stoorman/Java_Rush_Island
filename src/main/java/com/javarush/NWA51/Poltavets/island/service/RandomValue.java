package com.javarush.NWA51.Poltavets.island.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.concurrent.ThreadLocalRandom;

public class RandomValue {

    //Метод генерирует случайное число Double [minValue...maxValue] с шагом step
    public static Double randomDouble(Double minValue, Double maxValue, Double step) {
        double rawValue = minValue + (maxValue - minValue) * ThreadLocalRandom.current().nextDouble();
        BigDecimal bdValue = BigDecimal.valueOf(rawValue);
        BigDecimal bdStep = BigDecimal.valueOf(step);
        BigDecimal rounded = bdValue.divide(bdStep, 0, RoundingMode.HALF_UP)
                .multiply(bdStep);
        return rounded.doubleValue();
    }

    //Метод генерирует случайное целое число int [minValue...maxValue]
    public static int randomInt(int minValue, int maxValue) {
        return ThreadLocalRandom.current().nextInt(minValue, maxValue + 1);
    }

    //Метод генерирует случайное двоичное число boolean
    public static boolean randomBoolean() {
        return ThreadLocalRandom.current().nextBoolean();
    }

    //Метод генерирует случайный вектор [-1,0,1] по каждой оси
    public static int[] randomVector() {
        int xAxis = randomInt(-1, 1);
        int yAxis = randomInt(-1, 1);
        return new int[]{xAxis, yAxis};
    }
}
