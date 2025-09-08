package com.javarush.NWA51.Poltavets.island.repository;

import com.javarush.NWA51.Poltavets.island.entity.Animals;
import com.javarush.NWA51.Poltavets.island.entity.herbivore.*;
import com.javarush.NWA51.Poltavets.island.entity.predator.*;

import java.util.HashMap;
import java.util.Map;

public class EatingProbability {
    public static final Map<Class<? extends Animals>, Map<Class<? extends Animals>, Integer>> TABLE = new HashMap<>();

    static {
        // Волк
        Map<Class<? extends Animals>, Integer> wolfMap = new HashMap<>();
        wolfMap.put(Horse.class, 10);
        wolfMap.put(Deer.class, 15);
        wolfMap.put(Rabbit.class, 60);
        wolfMap.put(Mouse.class, 80);
        wolfMap.put(Goat.class, 60);
        wolfMap.put(Sheep.class, 70);
        wolfMap.put(Boar.class, 15);
        wolfMap.put(Buffalo.class, 10);
        wolfMap.put(Duck.class, 40);
        TABLE.put(Wolf.class, wolfMap);

        // Удав
        Map<Class<? extends Animals>, Integer> snakeMap = new HashMap<>();
        snakeMap.put(Fox.class, 15);
        snakeMap.put(Rabbit.class, 20);
        snakeMap.put(Mouse.class, 40);
        snakeMap.put(Duck.class, 10);
        TABLE.put(Snake.class, snakeMap);

        // Лиса
        Map<Class<? extends Animals>, Integer> foxMap = new HashMap<>();
        foxMap.put(Rabbit.class, 70);
        foxMap.put(Mouse.class, 90);
        foxMap.put(Duck.class, 60);
        foxMap.put(Caterpillar.class, 40);
        TABLE.put(Fox.class, foxMap);

        // Медведь
        Map<Class<? extends Animals>, Integer> bearMap = new HashMap<>();
        bearMap.put(Snake.class, 80);
        bearMap.put(Horse.class, 40);
        bearMap.put(Deer.class, 80);
        bearMap.put(Rabbit.class, 80);
        bearMap.put(Mouse.class, 90);
        bearMap.put(Goat.class, 70);
        bearMap.put(Sheep.class, 70);
        bearMap.put(Boar.class, 50);
        bearMap.put(Buffalo.class, 20);
        bearMap.put(Duck.class, 10);
        TABLE.put(Bear.class, bearMap);

        // Орёл
        Map<Class<? extends Animals>, Integer> eagleMap = new HashMap<>();
        eagleMap.put(Fox.class, 10);
        eagleMap.put(Rabbit.class, 90);
        eagleMap.put(Mouse.class, 90);
        eagleMap.put(Duck.class, 80);
        TABLE.put(Eagle.class, eagleMap);

        // Кабан
        Map<Class<? extends Animals>, Integer> boarMap = new HashMap<>();
        boarMap.put(Mouse.class, 50);
        boarMap.put(Caterpillar.class, 90);
        TABLE.put(Boar.class, boarMap);

        // Утка
        Map<Class<? extends Animals>, Integer> duckMap = new HashMap<>();
        duckMap.put(Caterpillar.class, 90);
        TABLE.put(Duck.class, duckMap);

        // Мышь
        Map<Class<? extends Animals>, Integer> mouseMap = new HashMap<>();
        mouseMap.put(Caterpillar.class, 90);
        TABLE.put(Mouse.class, mouseMap);
    }
}
