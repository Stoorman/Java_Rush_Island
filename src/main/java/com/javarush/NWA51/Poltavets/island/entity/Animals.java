package com.javarush.NWA51.Poltavets.island.entity;

public abstract class Animals {

    //Константы
    protected int weight;          // Вес животного
    protected Double fullnessSize; //Максимальная сытость
    protected Double hunger;       // голод или скорость убывания сытости
    protected int strokeMax;       // Длина хода максимальная
    protected int ageMax;          // Максимальный возраст животного
    protected int ageMin;          // Возраст до которго животное ребёнок

    //Изменяемые или задаются при создании
    protected Double fullness;     // Сытость
    protected int sex;             // Пол животного
    protected int age;             // Возраст животного

}
