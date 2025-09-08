package com.javarush.NWA51.Poltavets.island.service;

import com.javarush.NWA51.Poltavets.island.service.dto.IslandConfigDTO;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class IslandConfigLoader {

    // Метод загружает параметры острова из файла и возвращает DTO
    public static IslandConfigDTO loadConfig(String path) {
        Properties prop = new Properties();
        try (FileInputStream fis = new FileInputStream(path)) {
            prop.load(fis);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Не удалось загрузить параметры острова из файла: " + path);
        }

        // Проверяем наличие всех обязательных параметров
        String[] required = {
                "nameIsland","xAxisSize","yAxisSize","soilType",
                "grassDesertGrowth","grassDesertMax",
                "grassForestGrowth","grassForestMax",
                "grassJungleGrowth","grassJungleMax",
                "grassSavvannaGrowth","grassSavvannaMax"
        };
        for (String key : required) {
            if (prop.getProperty(key) == null) {
                throw new RuntimeException("Отсутствует параметр в файле: " + key);
            }
        }

        // Создаём DTO на основе считанных значений
        return new IslandConfigDTO.Builder()
                .nameIsland(prop.getProperty("nameIsland"))  // Имя острова
                .xAxisSize(Integer.parseInt(prop.getProperty("xAxisSize")))  // Размер по X
                .yAxisSize(Integer.parseInt(prop.getProperty("yAxisSize")))  // Размер по Y
                .soilType(Integer.parseInt(prop.getProperty("soilType")))    // Тип почвы
                .grassDesert(
                        Integer.parseInt(prop.getProperty("grassDesertGrowth")),  // Рост травы в пустыне
                        Integer.parseInt(prop.getProperty("grassDesertMax"))      // Макс трава пустыни
                )
                .grassForest(
                        Integer.parseInt(prop.getProperty("grassForestGrowth")),  // Рост травы в лесу
                        Integer.parseInt(prop.getProperty("grassForestMax"))      // Макс трава леса
                )
                .grassJungle(
                        Integer.parseInt(prop.getProperty("grassJungleGrowth")),  // Рост травы в джунглях
                        Integer.parseInt(prop.getProperty("grassJungleMax"))      // Макс трава джунглей
                )
                .grassSavvanna(
                        Integer.parseInt(prop.getProperty("grassSavvannaGrowth")), // Рост травы в саванне
                        Integer.parseInt(prop.getProperty("grassSavvannaMax"))     // Макс трава саванны
                )
                .build();
    }
}
