package com.javarush.NWA51.Poltavets.island.entity.predator;

import com.javarush.NWA51.Poltavets.island.entity.Animals;

public abstract class Predator extends Animals implements AnimalEater {

    Predator(String[] parameters) {
        super(parameters);
    }
}
