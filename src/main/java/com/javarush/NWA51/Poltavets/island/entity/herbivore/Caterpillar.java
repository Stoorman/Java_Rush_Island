package com.javarush.NWA51.Poltavets.island.entity.herbivore;

import com.javarush.NWA51.Poltavets.island.service.RandomValue;

public class Caterpillar extends Herbivore{
    public Caterpillar (String[] parameters) {super(parameters);}

    @Override
    public Double eatGrass(Double grass) {
        if(grass<=0){
            isDead = true; //гусеница не поела и сразу сдохла
        }
        return grass;
    }
}

