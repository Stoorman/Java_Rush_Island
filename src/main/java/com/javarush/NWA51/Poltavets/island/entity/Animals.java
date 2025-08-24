package com.javarush.NWA51.Poltavets.island.entity;

import com.javarush.NWA51.Poltavets.island.service.RandomValue;

public abstract class Animals {

    //Константы
    protected String animalName;
    protected int weight;          // Вес животного
    protected Double fullnessSize; //Максимальная сытость
    protected Double hunger;       // голод или скорость убывания сытости
    protected int strokeMax;       // Длина хода максимальная

    protected int ageMax;          // Максимальный возраст животного
    protected int ageMin;          // Возраст до которого животное ребёнок

    //Изменяемые или задаются при создании
    protected Double fullness;     // Сытость
    protected boolean sex;         // Пол животного
    protected int age;             // Возраст животного


    protected Animals(String[] parametersAnimal){
        //параметры, которые задаются для всех животных вида
        this.animalName = parametersAnimal[0];
        this.weight=Integer.parseInt(parametersAnimal[1]);
        this.fullnessSize=Double.parseDouble(parametersAnimal[2]);
        this.hunger=Double.parseDouble(parametersAnimal[3]);
        this.strokeMax=Integer.parseInt(parametersAnimal[4]);
        this.ageMax=Integer.parseInt(parametersAnimal[6]);
        this.ageMin=Integer.parseInt(parametersAnimal[7]);
        //случайные параметры
        this.fullness= RandomValue.randomDouble(hunger*2, fullnessSize);
        this.sex = RandomValue.randomBoolean();
        if(Integer.parseInt(parametersAnimal[8])==1) {
            this.age = RandomValue.randomInt(0,ageMin);
        } else {
            this.age = 0;
        }


    }

    public boolean isAlive() { // метод проверяющий живое ли животное
        //TODO писать реализацию метода
        return false;
    }

    public int[] run(int[] startPosition) {    //метод вычисляет позицию клетки, куда пойдёт животное
        //TODO написать реализацию метода
            return null;
    }

    public void print(){
        System.out.println("Вид - " + animalName + ", Вес - " + weight );
        // TODO дописать метод распечатки животного
    }

}
