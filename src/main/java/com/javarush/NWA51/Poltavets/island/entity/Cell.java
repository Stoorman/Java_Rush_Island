package com.javarush.NWA51.Poltavets.island.entity;

import com.javarush.NWA51.Poltavets.island.entity.herbivore.GrassEater;
import com.javarush.NWA51.Poltavets.island.entity.predator.AnimalEater;
import com.javarush.NWA51.Poltavets.island.service.AnimalFactory;
import com.javarush.NWA51.Poltavets.island.service.RandomValue;
import com.javarush.NWA51.Poltavets.island.service.SingleAnimalFactory;
import com.javarush.NWA51.Poltavets.island.service.dto.IslandConfigDTO;

import java.util.*;

public class Cell {
    private final int xAxis;                                                    // Координата ячейки по х
    private final int yAxis;                                                    // Координата ячейки по y
    private Double grass;                                                       // Количество травы в ячейке
    private int grassMax;                                                       // Максимальное количество травы в ячейке
    private int grassGrowth;                                                    // Скорость роста травы в сутки
    private final int soilType;                                                 // Тип почвы
    private Map<Class<? extends Animals>, List<Animals>> AnimalsMap;           // Карта класс - Список животных этого класса

    public Cell(int xAxis, int yAxis, IslandConfigDTO parametersCell) {
        this.xAxis = xAxis;
        this.yAxis = yAxis;
        switch (parametersCell.getSoilType()) {  // прописывается почва или генерируется случайно
            case 0, 1, 2, 3 -> this.soilType = parametersCell.getSoilType();
            default -> this.soilType = RandomValue.randomInt(0, 3);
        }

        switch (soilType) {  // прописываем скорость роста травы и максимальное количество в зависимости от почвы
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
        this.grass = RandomValue.randomDouble(0.0, grassMax * 1.0, 0.01);
        AnimalFactory animalFactory = new AnimalFactory(); //создаём фабрику животных
        try {
            this.AnimalsMap = animalFactory.createAnimal();  //получаем сгенерированую карту животных
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void print() {
        StringBuilder sb = new StringBuilder();
        sb.append("X=").append(xAxis)
                .append(" Y=").append(yAxis)
                .append(" | Тип почвы: ").append(soilType)
                .append(" | Трава: ").append(String.format("%.1f", grass))
                .append("/").append(grassMax)
                .append(" | Животные: ");

        if (AnimalsMap.isEmpty()) {
            sb.append("нет");
        } else {
            for (Map.Entry<Class<? extends Animals>, List<Animals>> entry : AnimalsMap.entrySet()) {
                String animalType = entry.getKey().getSimpleName();
                int count = entry.getValue().size();
                sb.append(animalType).append("=").append(count).append(" ");
            }
        }
    }

    // Метод перемещает животных по ячейкам
    public void runAnimals(Cell[][] islands) {
        for (Map.Entry<Class<? extends Animals>, List<Animals>> entry : AnimalsMap.entrySet()) {
            List<Animals> animalList = entry.getValue();
            Iterator<Animals> iterator = animalList.iterator(); //перебираем через итератор
            while (iterator.hasNext()) {
                Animals animal = iterator.next();
                int newXAxis = xAxis;
                int newYAxis = yAxis;
                int xAxisMax = islands.length;
                int yAxisMax = islands[0].length;
                int buffer;

                if (animal.getSpeed() > 0) {   //Если может двигаться
                    for (int i = 0; i < animal.getSpeed(); i++) { //то случайно выбираем направление движения для каждого хода
                        int[] vector = RandomValue.randomVector();
                        if (vector[0] != 0 || vector[1] != 0) {  //тут защита от выхода за пределы массива
                            buffer = newXAxis;                   //если выходим за пределы, то остаёмся на месте
                            newXAxis = newXAxis + vector[0];
                            if (newXAxis >= xAxisMax || newXAxis < 0) newXAxis = buffer;

                            buffer = newYAxis;
                            newYAxis = newYAxis + vector[1];
                            if (newYAxis >= yAxisMax || newYAxis < 0) newYAxis = buffer;
                        }
                    }
                }

                if (islands[newXAxis][newYAxis].getNumbersOfAnimals(animal.getClass()) < animal.getValueMax()    //смотрим еть ли место в целевой ячейке
                        && (xAxis != newXAxis || yAxis != newYAxis)) {                                           //целевая яцейка не является текущей
                    islands[newXAxis][newYAxis].addAnimal(animal);                                               //значит добавляем
                    System.out.println(animal.getAnimalName() + " переходит из ячейки X=" + xAxis + " Y=" + yAxis +
                            " в ячейку X=" + newXAxis + " Y=" + newYAxis);
                    iterator.remove();                                                                        //даляем из текущей
                }
            }
        }
    }

    // Добавление животного в ячейку
    public void addAnimal(Animals animal) {
        AnimalsMap.get(animal.getClass()).add(animal);
    }

    // Удаляем всех мёртвых животных из клетки
    private void removeDeadAnimals() {
        for (List<Animals> list : AnimalsMap.values()) {
            list.removeIf(Animals::isDead);
        }
    }

    //Рост травы
    public void grassGrowt() {
        grass += grassGrowth;
        if (grass >= grassMax) grass = 1.0 * grassMax; //если перешли за максимум, то опускаем до максимума
    }

    public void nextDayCell() throws Exception {  // Действия при смене дня
        grassGrowt(); // рост травы

        // Возраст, голод и рождение
        for (Map.Entry<Class<? extends Animals>, List<Animals>> entry : AnimalsMap.entrySet()) {
            List<Animals> animalList = entry.getValue();
            List<Animals> tempAnimalList = new ArrayList<>();

            for (Animals animal : animalList) {
                animal.addAgeAnimals();
                animal.animalHunger();

                if (!animal.isDead() && animal.birthAnimals(animalList)) {
                    tempAnimalList.add(new SingleAnimalFactory().createAnimal(animal.getClass()));
                }
            }
            animalList.addAll(tempAnimalList);
        }

        // Убираем мёртвых после голода и старости
        removeDeadAnimals();

        //Список всех животных для еды и охоты
        List<Animals> animalsListEat = new ArrayList<>();
        for (List<Animals> list : AnimalsMap.values()) {
            animalsListEat.addAll(list);
        }
        Collections.shuffle(animalsListEat);

        // Травоядные едят траву
        for (Animals animal : animalsListEat) {
            if (animal instanceof GrassEater && grass > 0) {
                grass = ((GrassEater) animal).eatGrass(grass);
            }
        }

        // Хищники охотятся на животных
        for (Animals animal : animalsListEat) {
            if (animal instanceof AnimalEater) {
                ((AnimalEater) animal).hunt(animalsListEat);
            }
        }

        // Убираем дохлых после охоты
        removeDeadAnimals();
    }

    public int getSoilType() {
        return soilType;
    }
    //Скольеко животных этого класса в ячейке
    public int getNumbersOfAnimals(Class<? extends Animals> animalType) {
        return AnimalsMap.get(animalType).size();
    }

    //Какие животный сейчас в ячейке
    public Map<Class<? extends Animals>, List<Animals>> getAnimalsMap() {
        return AnimalsMap;
    }
}
