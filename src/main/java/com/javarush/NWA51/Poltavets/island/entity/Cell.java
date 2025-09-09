package com.javarush.NWA51.Poltavets.island.entity;

import com.javarush.NWA51.Poltavets.island.entity.herbivore.GrassEater;
import com.javarush.NWA51.Poltavets.island.entity.predator.AnimalEater;
import com.javarush.NWA51.Poltavets.island.service.AnimalFactory;
import com.javarush.NWA51.Poltavets.island.service.RandomValue;
import com.javarush.NWA51.Poltavets.island.service.SingleAnimalFactory;
import com.javarush.NWA51.Poltavets.island.service.dto.IslandConfigDTO;

import java.util.*;

public class Cell {
    private final int xAxis;                                                    // Координата ячейки по x
    private final int yAxis;                                                    // Координата ячейки по y
    private Double grass;                                                       // Количество травы в ячейке
    private int grassMax;                                                       // Максимальное количество травы в ячейке
    private int grassGrowth;                                                    // Скорость роста травы в сутки
    private final int soilType;                                                 // Тип почвы
    private Map<Class<? extends Animals>, List<Animals>> AnimalsMap;           // Карта класс - Список животных этого класса

    private int bornTotal = 0;                                                 // Всего родилось животных в клетке
    private int deadTotal = 0;                                                 // Всего умерло животных в клетке

    public Cell(int xAxis, int yAxis, IslandConfigDTO parametersCell) {
        this.xAxis = xAxis;
        this.yAxis = yAxis;

        // Присваиваем тип почвы или случайно генерируем
        switch (parametersCell.getSoilType()) {
            case 0, 1, 2, 3 -> this.soilType = parametersCell.getSoilType();
            default -> this.soilType = RandomValue.randomInt(0, 3);
        }

        // Настройка роста травы и максимума в зависимости от типа почвы
        switch (soilType) {
            case 0 -> { this.grassGrowth = parametersCell.getGrassGrowthDesert(); this.grassMax = parametersCell.getGrassMaxDesert(); }
            case 1 -> { this.grassGrowth = parametersCell.getGrassGrowthForest(); this.grassMax = parametersCell.getGrassMaxForest(); }
            case 2 -> { this.grassGrowth = parametersCell.getGrassGrowthSavvanna(); this.grassMax = parametersCell.getGrassMaxSavvanna(); }
            case 3 -> { this.grassGrowth = parametersCell.getGrassGrowthJungle(); this.grassMax = parametersCell.getGrassMaxJungle(); }
        }

        // Инициализация количества травы
        this.grass = RandomValue.randomDouble(0.0, grassMax * 1.0, 0.01);

        // Создаём фабрику животных и генерируем животных для ячейки
        AnimalFactory animalFactory = new AnimalFactory();
        try {
            this.AnimalsMap = animalFactory.createAnimal();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    // Сброс флагов движения животных в начале дня
    public void resetRunFlags() {
        for (List<Animals> list : AnimalsMap.values()) {
            for (Animals animal : list) {
                animal.setRun(false);
            }
        }
    }

    // Перемещение животных по острову
    public void runAnimals(Cell[][] islands) {
        for (Map.Entry<Class<? extends Animals>, List<Animals>> entry : AnimalsMap.entrySet()) {
            List<Animals> animalList = entry.getValue();
            Iterator<Animals> iterator = animalList.iterator();
            while (iterator.hasNext()) {
                Animals animal = iterator.next();
                int newXAxis = xAxis;
                int newYAxis = yAxis;
                int xAxisMax = islands.length;
                int yAxisMax = islands[0].length;
                int buffer;

                // Если животное может двигаться и ещё не двигалось
                if (animal.getSpeed() > 0 && !animal.isRun()) {
                    for (int i = 0; i < animal.getSpeed(); i++) {
                        int[] vector = RandomValue.randomVector();
                        if (vector[0] != 0 || vector[1] != 0) {
                            buffer = newXAxis;
                            newXAxis = newXAxis + vector[0];
                            if (newXAxis >= xAxisMax || newXAxis < 0) newXAxis = buffer;

                            buffer = newYAxis;
                            newYAxis = newYAxis + vector[1];
                            if (newYAxis >= yAxisMax || newYAxis < 0) newYAxis = buffer;
                        }
                    }
                }

                // Перемещаем животное, если целевая ячейка не переполнена
                if ((xAxis != newXAxis || yAxis != newYAxis) &&
                        islands[newXAxis][newYAxis].getNumbersOfAnimals(animal.getClass()) < animal.getValueMax()) {
                    islands[newXAxis][newYAxis].addAnimal(animal);
                    animal.setRun(true);
                    System.out.println(animal.getAnimalName() + " 🐾 переместился из X=" + xAxis + ", Y=" + yAxis +
                            " в X=" + newXAxis + ", Y=" + newYAxis);
                    iterator.remove();
                }
            }
        }
    }

    // Добавление животного в ячейку
    public void addAnimal(Animals animal) {
        AnimalsMap.get(animal.getClass()).add(animal);
    }

    // Удаляем всех мёртвых животных из клетки и возвращаем их количество
    private int removeDeadAnimals() {
        int count = 0;
        for (List<Animals> list : AnimalsMap.values()) {
            Iterator<Animals> iterator = list.iterator();
            while (iterator.hasNext()) {
                Animals a = iterator.next();
                if (a.isDead()) {
                    System.out.println("☠ " + a.getAnimalName() + " умер в клетке X=" + xAxis + ", Y=" + yAxis);
                    iterator.remove();
                    count++;
                }
            }
        }
        return count;
    }

    // Рост травы в ячейке
    public void grassGrowt() {
        grass += grassGrowth;
        if (grass >= grassMax) grass = 1.0 * grassMax;
    }

    // Полный цикл действий клетки за день
    public int[] nextDayCell() throws Exception {
        grassGrowt(); // рост травы

        int birthCount = 0;

        // Еда и охота
        List<Animals> animalsListEat = new ArrayList<>();
        for (List<Animals> list : AnimalsMap.values()) animalsListEat.addAll(list);
        Collections.shuffle(animalsListEat);

        // Едят траву
        for (Animals animal : animalsListEat) {
            if (animal instanceof GrassEater && grass > 0) {
                double before = grass;
                grass = ((GrassEater) animal).eatGrass(grass);
                System.out.println("🌿 " + animal.getAnimalName() + " съел траву: " + (before - grass) +
                        " в клетке X=" + xAxis + ", Y=" + yAxis);
            }
        }

        // Охота
        for (Animals animal : animalsListEat) {
            if (animal instanceof AnimalEater) {
                ((AnimalEater) animal).hunt(animalsListEat, this);
            }
        }

        // Возраст, голод и рождение животных
        for (Map.Entry<Class<? extends Animals>, List<Animals>> entry : AnimalsMap.entrySet()) {
            List<Animals> animalList = entry.getValue();
            List<Animals> tempAnimalList = new ArrayList<>();

            for (Animals animal : animalList) {
                animal.addAgeAnimals();   // возраст
                animal.animalHunger();    // голод

                // Рождение животных
                if (!animal.isDead() && animal.birthAnimals(animalList)) {
                    Animals child = new SingleAnimalFactory().createAnimal(animal.getClass());
                    tempAnimalList.add(child);
                    birthCount++;
                    System.out.println("👶 Родился " + child.getAnimalName() + " в клетке X=" + xAxis + ", Y=" + yAxis);
                }
            }
            animalList.addAll(tempAnimalList);
        }

        // Удаляем всех мёртвых животных и считаем их количество
        int deadCount = removeDeadAnimals();

        // Обновляем суммарную статистику
        bornTotal += birthCount;
        deadTotal += deadCount;

        // Вывод статистики по клетке
        int totalAnimals = 0;
        for (List<Animals> list : AnimalsMap.values()) totalAnimals += list.size();

        System.out.println("Статистика клетки X=" + xAxis + ", Y=" + yAxis + ": " +
                "Всего животных=" + totalAnimals +
                ", Родилось=" + birthCount +
                ", Умерло=" + deadCount +
                ", Трава=" + grass);

        return new int[]{birthCount, deadCount};
    }

    // Геттеры
    public int getSoilType() { return soilType; }
    public double getGrass() { return grass; }
    public int getGrassMax() { return grassMax; }
    public int getXAxis() { return xAxis; }
    public int getYAxis() { return yAxis; }
    public int getNumbersOfAnimals(Class<? extends Animals> animalType) { return AnimalsMap.get(animalType).size(); }
    public Map<Class<? extends Animals>, List<Animals>> getAnimalsMap() { return AnimalsMap; }

    // Методы для получения суммарной статистики
    public int getBornTotal() { return bornTotal; }
    public int getDeadTotal() { return deadTotal; }
}
