package com.javarush.NWA51.Poltavets.island.app;

import com.javarush.NWA51.Poltavets.island.controller.MainController;

public class ApplicationIsland {

    private final MainController mainController;

    public ApplicationIsland(MainController mainController) {
        this.mainController = mainController;
    }

    // Запуск приложения: инициализация острова и первичная отрисовка
    public void start() {
        mainController.initIsland();
        mainController.showIsland();
    }

    // Переход на следующий день (многопоточно)
    public void nextDay() {
        mainController.nextDay();
    }
}
