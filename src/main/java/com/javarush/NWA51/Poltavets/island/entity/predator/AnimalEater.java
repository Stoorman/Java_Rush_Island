package com.javarush.NWA51.Poltavets.island.entity.predator;

import com.javarush.NWA51.Poltavets.island.entity.Animals;
import com.javarush.NWA51.Poltavets.island.entity.Cell;
import com.javarush.NWA51.Poltavets.island.repository.EatingProbability;
import com.javarush.NWA51.Poltavets.island.service.RandomValue;

import java.util.List;
import java.util.Map;

public interface AnimalEater {

    // Метод охоты с логированием
    default void hunt(List<Animals> animalsInCell, Cell cell) {
        Map<Class<? extends Animals>, Integer> preyMap = EatingProbability.TABLE.get(this.getClass());
        if (preyMap == null) return; // Этот хищник никого не ест

        for (Animals prey : animalsInCell) {
            if (prey.isDead()) continue;

            Integer probability = preyMap.get(prey.getClass());
            if (probability != null && RandomValue.randomInt(0, 100) < probability) {
                ((Animals)this).addFullness(prey.getWeight());
                prey.kill(); // помечаем жертву как мёртвую
               // System.out.println("🐺 " + ((Animals)this).getAnimalName() +
               //         " съел 🐇 " + prey.getAnimalName() +
                //        " в X=" + cell.getXAxis() + " Y=" + cell.getYAxis());
                break; // удачная охота завершает поиск
            }
        }
    }
}
