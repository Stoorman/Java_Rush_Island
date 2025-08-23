package com.javarush.NWA51.Poltavets.island.entity;

public abstract class Animals {

    //Константы
    protected String animalName;
    protected int weight;          // Вес животного
    protected Double fullnessSize; //Максимальная сытость
    protected int strokeMax;       // Длина хода максимальная
    protected Double hunger;       // голод или скорость убывания сытости
    protected int ageMax;          // Максимальный возраст животного
    protected int ageMin;          // Возраст до которого животное ребёнок

    //Изменяемые или задаются при создании
    protected Double fullness;     // Сытость
    protected int sex;             // Пол животного
    protected int age;             // Возраст животного


    protected Animals(String[] parametersAnimal){
        this.animalName = parametersAnimal[0];
        this.weight=Integer.parseInt(parametersAnimal[1]);
        this.fullnessSize=Double.parseDouble(parametersAnimal[2]);
        this.strokeMax=Integer.parseInt(parametersAnimal[3]);

    }

    public boolean isAlive() { // метод проверяющий живое ли животное
        //TODO писать реализацию метода
        return false;
    }

    public int[] run(int[] startPosition) {    //метод вычиляет позицию клетки, куда пойдёт животное
        //TODO написать реализацию метода
            return null;
    }

    public void print(){
        System.out.println("Вид - " + animalName + ", Вес - " + weight );
        // TODO дописать метод распечатки животного
    }

}
