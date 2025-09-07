package com.javarush.NWA51.Poltavets.island.entity.herbivore;

import com.javarush.NWA51.Poltavets.island.entity.Animals;

public abstract class Herbivore extends Animals implements GrassEater {

    Herbivore(String[] parameters) {super(parameters);}
}
