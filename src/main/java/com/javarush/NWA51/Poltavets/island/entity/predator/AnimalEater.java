package com.javarush.NWA51.Poltavets.island.entity.predator;

import com.javarush.NWA51.Poltavets.island.entity.Animals;
import com.javarush.NWA51.Poltavets.island.repository.EatingProbability;
import com.javarush.NWA51.Poltavets.island.service.RandomValue;

import java.util.List;
import java.util.Map;

public interface AnimalEater {

    // Метод охоты
    default void hunt(List<Animals> animalsInCell) {
        // Получаем карту вероятностей для данного хищника: кого и с какой вероятностью он может съесть
        Map<Class<? extends Animals>, Integer> preyMap = EatingProbability.TABLE.get(this.getClass());

        if (preyMap == null) {
            return; // Этот вид никого не ест
        }

        for (Animals prey : animalsInCell) {        // Перебираем всех потенциальных жертв
            if (prey.isDead()) continue;            // Пропускаем дохлых

            Integer probability = preyMap.get(prey.getClass());   // Берем вероятность съесть конкретного животного
            // Используем многопоточный RandomValue для проверки вероятности по таблице
            if (probability != null && RandomValue.randomInt(0, 100) < probability) {
                ((Animals)this).addFullness(prey.getWeight());   // Прибавляем сытость хищника на вес жертвы
                prey.kill(); // Убиваем жертву
                break;       // После удачной охоты прекращаем искать дальше
            }
        }
    }
}
