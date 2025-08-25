package com.javarush.NWA51.Poltavets.island.service;

import com.javarush.NWA51.Poltavets.island.entity.Animals;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;

public class AnimalFactory {
    private static final String BASE_PACKAGE = "com.javarush.NWA51.Poltavets.island.entity."; //Путь к пакету с Animals

    public AnimalFactory() {
    }


    public Map<Class<? extends Animals>, List<Animals>> createAnimal() throws Exception {
        Map<Class<? extends Animals>, List<Animals>> animalsMap = new HashMap<>();
        Properties properties = new Properties();                                                               //Класс для хранения настроек
        Random random = new Random();

        try (InputStream input = getClass().getClassLoader()                                                    //Создаём поток
                .getResourceAsStream("repository/animals.prm")) {     //Ищем файл в classpath
            if (input == null) {
                throw new IOException("Файл animals.prm не найден!");
            }
            properties.load(input);                                                                            //загружаем настройки потоком в объект класса Properties
        }
        //TODO Вывести пути и сообщения об ошибке в переменные, их значения в отдельный файл

        for (String key : properties.stringPropertyNames()) {                                                 //Через цикл проходим все ключи в properties, а именно все названия классов животных
            String fullPath = BASE_PACKAGE + key;

            //Class<? extends Animals> clazz = (Class<? extends Animals>) Class.forName(fullPath);     // Так ругается, т.к. можем получить из файла класс не наследник Animals
            Class<? extends Animals> clazz = Class.forName(fullPath).asSubclass(Animals.class);
            String[] animalParameters = properties.getProperty(key).split(",");                  //Получаем параметры по ключу и разделяем их на массив разделитель ","

            //Создание классов через рефлексию
            List<Animals> list = new ArrayList<>();
            int countAnimals = random.nextInt(Integer.parseInt(animalParameters[5]) + 1);       //Случайное количество животных, но не больше максимума
            for (int i = 0; i < countAnimals; i++) {
                Animals animal = clazz.getDeclaredConstructor(String[].class)
                        .newInstance((Object) animalParameters);
                list.add(animal);                                                                     //Добавляем животное в список
            }
            animalsMap.put(clazz, list);                                                              //Добавляем список по ключу в карту
        }

        return animalsMap;
    }


}


