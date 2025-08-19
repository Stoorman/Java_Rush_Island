package com.javarush.NWA51.Poltavets.island.constants.Animals;

public class WolfConstants {
    public final String ANIMAL_NAME = "Wolf";       //  Имя вида
    public final int WEIGHT= 50;                    //  Вес животного
    public final Double FULLNESS_SIZE = 8.0;        //  Максимальная сытость
    public final int STROKEMAX = 3;                 //  Длина хода максимальная

    //Не реализовано
    protected Double hunger;                //  Голод или скорость убывания сытости
    protected int ageMax;                   //  Максимальный возраст животного
    protected int ageMin;                   //  Возраст до которго животное ребёнок
}
