package com.javarush.NWA51.Poltavets.island.entity.herbivore;

import com.javarush.NWA51.Poltavets.island.entity.predator.AnimalEater;

public class Mouse extends Herbivore implements AnimalEater {

    public Mouse(String[] parameters) {
        super(parameters);
    }
}

