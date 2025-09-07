package com.javarush.NWA51.Poltavets.island.service;

import com.javarush.NWA51.Poltavets.island.entity.Animals;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class SingleAnimalFactory {
    private static final String BASE_PACKAGE = "com.javarush.NWA51.Poltavets.island.entity."; //Путь к пакету с Animals

    public Animals createAnimal(Class<? extends Animals> animal) throws Exception {
        Properties properties = new Properties();
        try (InputStream input = getClass().getClassLoader()
                .getResourceAsStream("repository/animals.prm");
             Reader reader = new InputStreamReader(input, StandardCharsets.UTF_8)) {

            if (input == null) {
                throw new IOException("Файл animals.prm не найден!");
            }
            properties.load(reader);
        }

        // Определяем ключ для поиска
        String key = animal.getName().replace(BASE_PACKAGE, "");

        if (!properties.containsKey(key)) {
            throw new IllegalArgumentException("Тип животного " + key + " не найден в properties");
        }

        String[] animalParameters = properties.getProperty(key).split(",");
        animalParameters[8] = "0"; // создаём животное как новорождённое

        // Собираем полный путь к классу
        String fullPath = BASE_PACKAGE + key;

        Class<? extends Animals> clazz = Class.forName(fullPath).asSubclass(Animals.class);
        return clazz.getDeclaredConstructor(String[].class)
                .newInstance((Object) animalParameters);
    }
}
// TODO Убрать текст в отдельный файл
// TODO Этот класс очень поход на AnimalFactory, может из них сделать один