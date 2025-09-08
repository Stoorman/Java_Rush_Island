package com.javarush.NWA51.Poltavets.island.entity.herbivore;

import com.javarush.NWA51.Poltavets.island.entity.Animals;
import com.javarush.NWA51.Poltavets.island.service.RandomValue;

public abstract class Herbivore extends Animals implements GrassEater {

    Herbivore(String[] parameters) {super(parameters);}

    @Override
    public Double eatGrass(Double grass) { //едим траву, если она есть
        double eatenGrass;
        if(grass > (fullnessSize-fullness)){    //травы больше полного насыщения
            eatenGrass = RandomValue.randomDouble(0.0, fullnessSize-fullness, 0.01);
        } else {
            eatenGrass = RandomValue.randomDouble(0.0, grass, 0.01);
        }
        fullness += eatenGrass;
        grass -= eatenGrass;
        if(grass<0)  grass=0.0;

        return grass;
    }
}
