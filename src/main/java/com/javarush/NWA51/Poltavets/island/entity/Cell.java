package com.javarush.NWA51.Poltavets.island.entity;

import com.javarush.NWA51.Poltavets.island.entity.herbivore.GrassEater;
import com.javarush.NWA51.Poltavets.island.entity.herbivore.Herbivore;
import com.javarush.NWA51.Poltavets.island.service.AnimalFactory;
import com.javarush.NWA51.Poltavets.island.service.RandomValue;
import com.javarush.NWA51.Poltavets.island.service.SingleAnimalFactory;
import com.javarush.NWA51.Poltavets.island.service.dto.IslandConfigDTO;

import java.util.*;

public class Cell {
    private final int xAxis;                                                    //Координата ячейки по х
    private final int yAxis;                                                    //Координата ячейки по y
    private Double grass;                                                 //Количество травы в ячейке
    private int grassMax;                                                 //Максимальное количество травы в ячейке
    private int grassGrowth;                                              //Скорость роста травы в сутки
    private final int soilType;                                                 //Тип почвы
    private Map<Class<? extends Animals>, List<Animals>> AnimalsMap;      //Карта класс - Список животных этого класса


    public Cell(int xAxis, int yAxis, IslandConfigDTO parametersCell) {
        this.xAxis = xAxis;
        this.yAxis = yAxis;
        switch (parametersCell.getSoilType()) {       // прописывается почва или генерируется случайно
            case 0, 1, 2, 3 -> this.soilType = parametersCell.getSoilType();
            default -> this.soilType = RandomValue.randomInt(0, 3);
        }

        switch (soilType) {                          //прописываем скорость роста травы и максимальное количество в зависимости от почвы
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
        AnimalFactory animalFactory = new AnimalFactory();
        try {
            this.AnimalsMap = animalFactory.createAnimal();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        // TODO Переработать обработку ошибок
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
                String animalType = entry.getKey().getSimpleName();   // Название класса без пакета
                int count = entry.getValue().size();                 // Количество
                sb.append(animalType).append("=").append(count).append(" ");
            }
        }
    }

    //Метод перемещает животных по ячейкам
   public void runAnimals(Cell[][] islands) {
       for (Map.Entry<Class<? extends Animals>, List<Animals>> entry : AnimalsMap.entrySet()) { //Перебираем все классы животных
           List<Animals> animalList = entry.getValue(); //получаем список животных
           Iterator<Animals> iterator = animalList.iterator(); //итератор
           while (iterator.hasNext()) { //перебираем пока есть следующий элемент
               Animals animal = iterator.next(); //следующий элемент
               //Определяем новые координаты
               int newXAxis = xAxis;
               int newYAxis = yAxis;
               int xAxisMax = islands.length;
               int yAxisMax = islands[0].length;
               int buffer = 0;
               if(animal.getSpeed()>0) {  //двигаемся, если скорость >0
                   for (int i = 0; i < animal.getSpeed(); i++) { //при каждом перемещении выбираем направление движения
                       int[] vector = RandomValue.randomVector();
                       if (vector[0] != 0 || vector[1] != 0) {
                           buffer = newXAxis;
                           newXAxis = newXAxis + vector[0];
                           if (newXAxis >= xAxisMax || newXAxis < 0) { //Проверяем на выход за границы массива
                               newXAxis = buffer;
                           }
                           buffer = newYAxis;
                           newYAxis = newYAxis + vector[1];
                           if (newYAxis >= yAxisMax || newYAxis < 0) {
                               newYAxis = buffer;
                           }
                       }
                       //   System.out.println(animal.getAnimalName()+ " X=" +newXAxis + " Y=" +newYAxis);
                   }
               }
               if (islands[newXAxis][newYAxis].getNumbersOfAnimals(animal.getClass()) < animal.getValueMax() && (xAxis != newXAxis || yAxis !=newYAxis)){
                   //Если кол-во животных этого типа меньше макс кол-ва животных и целевая ячейка не совпадает с текущей, то переносим животное в целевую ячейку
                   islands[newXAxis][newYAxis].addAnimal(animal); // добавляем животное в целевую ячейку
                   System.out.println(animal.getAnimalName() + " переходит из ячейки X=" + xAxis + " Y=" +yAxis + " в ячейку X=" + newXAxis + " Y=" + newYAxis);
                   iterator.remove(); //удаляем животное их текущей ячейки
               }
           }
       }
    }

    //Добавление животного в ячейку
    public void addAnimal(Animals animal) {
        //Class<? extends Animals> animalType = animal.getClass(); //какой класс у животного
        //List<Animals> animalsList = AnimalsMap.get(animalType);  //ищем список по ключу
        //animalsList.add(animal); //добавляем в список животное
        AnimalsMap.get(animal.getClass()).add(animal); //короткая запись
    }


    public void nextDayCell() throws Exception{                                   //Действия при смене дня
        grassGrowt();          //рост травы
        for (Map.Entry<Class<? extends Animals>, List<Animals>> entry : AnimalsMap.entrySet()) { //Перебираем все классы животных
            List<Animals> animalList = entry.getValue(); //получаем список животных
            Iterator<Animals> iterator = animalList.iterator(); //итератор
            List<Animals> tempAnimalList = new ArrayList<>(); //временный лист для новорожденных, т.к. во время итерации добавить нельзя
            while (iterator.hasNext()) { //перебираем пока есть следующий элемент
                Animals animal = iterator.next(); //следующий элемент
                animal.addAgeAnimals();  //прибавляем возраст
                animal.animalHunger();   //проголодался
                if(!animal.isDead()) {     //если ещё живой, то продолжаем
                    if(animal.birthAnimals(animalList)) { //животное рожает?
                        tempAnimalList.add(new SingleAnimalFactory().createAnimal(animal.getClass()));  // добавляем новое животное (0 возраст) во временный лист
                    }
                } else {
                    iterator.remove(); //удаляем мертвое животное
                }
            }
            animalList.addAll(tempAnimalList); // добавляем за пределами итератора из временного листа новорожденных
        }
        //А теперь животные начинают жрать траву и друг друга

        // Травоядные едят траву
        List<Animals> animalsListEat = new ArrayList<>();   //Список для всех животных
        for (List<Animals> list : AnimalsMap.values()) {    // Перебираем все списки в карте
            animalsListEat.addAll(list);                    // Сваливаем всех в кучу
        }
        Collections.shuffle(animalsListEat);                // Перемешиваем список
        for (Animals animal : animalsListEat) {             // Теперь проходим весь список по порядку
            if(animal instanceof GrassEater && grass > 0) {              // Если ест траву и трава есть
                grass = ((GrassEater) animal).eatGrass(grass);          //возвращает остаток травы
            }
        }




    }

    public void grassGrowt() {
        grass += grassGrowth;
        if(grass >= grassMax)
            grass = 1.0*grassMax;}


    public int getSoilType() {
        return soilType;
    }

    public int getNumbersOfAnimals(Class<? extends Animals> animalType) {
        return AnimalsMap.get(animalType).size();
    }

    public Map<Class<? extends Animals>, List<Animals>> getAnimalsMap() {
        return AnimalsMap;
    }
}



