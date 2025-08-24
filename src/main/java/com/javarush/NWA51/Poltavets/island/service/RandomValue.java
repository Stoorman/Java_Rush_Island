package com.javarush.NWA51.Poltavets.island.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Random;

public class RandomValue {
    private static Random random = new Random();

    //Метод генерирует случайное натуральное число(Double) [minValue...maxValue]
    public static Double randomDouble(Double minValue, Double maxValue) {
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

    //Метод генерирует случайное целое число(int) [minValue...maxValue]
    public static int randomInt(int minValue, int maxValue) {
        return random.nextInt(maxValue - minValue + 1) + minValue;
    }

    //Метод генерирует случайное двоичное число(boolean)
    public static boolean randomBoolean() {
        return random.nextBoolean();
    }



}
