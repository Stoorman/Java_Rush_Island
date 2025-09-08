package com.javarush.NWA51.Poltavets.island.view;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class AnimalConfigLoader {

    public static Map<String, List<String[]>> loadAnimalConfig(String path) {
        Map<String, List<String[]>> result = new HashMap<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(path))) {
            String line;
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty() || line.startsWith("#")) continue;

                String[] parts = line.split("=");
                String type = parts[0].contains(".") ? parts[0].split("\\.")[0] : "unknown";
                String[] values = parts[1].split(",");

                result.computeIfAbsent(type, k -> new ArrayList<>()).add(values);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }
}
