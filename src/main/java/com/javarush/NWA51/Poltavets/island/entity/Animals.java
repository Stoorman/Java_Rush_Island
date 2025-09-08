package com.javarush.NWA51.Poltavets.island.entity;

import com.javarush.NWA51.Poltavets.island.service.RandomValue;

import java.util.List;

public abstract class Animals {

    //Константы
    private final String animalName;
    private final Double weight;          // Вес животного
    protected final Double fullnessSize; //Максимальная сытость
    private final Double hunger;       // голод или скорость убывания сытости
    private final int speed;       // Длина хода максимальная
    private final int valueMax;        // Максимальное количество в клетке

    private final int ageMax;          // Максимальный возраст животного
    private final int ageMin;          // Возраст до которого животное ребёнок
    private final boolean sex;         // Пол животного 0-M, 1-W
    private final int probabilityOfBirth;         // Вероятность рождения в процентах


    //Изменяемые или задаются при создании
    protected Double fullness;     // Сытость
    private int age;             // Возраст животного
    protected boolean isDead = false; //умер?
    protected boolean isRun = false;  // флаг, который говорит, ходило животное или нет


    protected Animals(String[] parametersAnimal){
        //параметры, которые задаются для всех животных вида
        this.animalName = parametersAnimal[0] + RandomValue.randomInt(0,10000);
        this.weight=Double.parseDouble(parametersAnimal[1]);
        this.fullnessSize=Double.parseDouble(parametersAnimal[2]);
        this.hunger=Double.parseDouble(parametersAnimal[3]);
        this.speed=Integer.parseInt(parametersAnimal[4]);
        this.valueMax= Integer.parseInt(parametersAnimal[5]);
        this.ageMax=Integer.parseInt(parametersAnimal[6]);
        this.ageMin=Integer.parseInt(parametersAnimal[7]);
        this.probabilityOfBirth = Integer.parseInt(parametersAnimal[9]);
        //случайные параметры
        this.fullness= RandomValue.randomDouble(hunger*2, fullnessSize, 0.01);
        this.sex = RandomValue.randomBoolean();
        if(Integer.parseInt(parametersAnimal[8])==1) {
            this.age = RandomValue.randomInt(0,ageMin);
        } else {
            this.age = 0;
        }


    }
    //Методы следующего дня

    //Прибавляем возраст
    public void addAgeAnimals() {
        age++;
        if(age > ageMax) {
            isDead = true;  //умирает от старости
        }
    }

    //Убавляем сытость
    public void animalHunger() {
        fullness = fullness-hunger;
        if(fullness < 0) {
            isDead = true;
        }
    }

    //Рождаются новые животные
    public boolean birthAnimals(List<Animals> animalsList){
        if(sex && age>=ageMin) {   // проверяем пол(W) и возраст
            for (Animals animals : animalsList) {
                if (!animals.getSex() && animals.getAge()>=ageMin && RandomValue.randomInt(0,100)<=probabilityOfBirth) {
                    //ищет в списке M подходящего возраста, а затем случайным образом происходит рождение
                    return true;
                }
            }
        }
        return false;
    }

    //Это хищник съел жертву
    public void kill() {isDead = true;}

    //Задаёт флаг ходило животное или нет
    public void setRun(boolean isRun) {this.isRun = isRun;}

    public boolean isRun() {return isRun;}

    //Увеличение сытости
    public void addFullness(double foodWeight) {
        this.fullness = Math.min(this.fullness + foodWeight, this.fullnessSize);
        // тут защита от переполнения сытости
    }

    //Геттеры
    public String getAnimalName() {return animalName;}

    public int getValueMax() {return valueMax;}

    public int getSpeed() {return speed;}

    public boolean isDead() {return isDead;}

    public int getAge() {return age;}

    public boolean getSex() {return sex;}

    public Double getWeight() {return weight;}
}
