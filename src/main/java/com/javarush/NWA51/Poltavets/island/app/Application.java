package com.javarush.NWA51.Poltavets.island.app;

import com.javarush.NWA51.Poltavets.island.controller.MainController;
import com.javarush.NWA51.Poltavets.island.entity.Result;
import com.javarush.NWA51.Poltavets.island.repository.ResultCode;

public class Application {

    private final MainController mainController;

    public Application(MainController mainController) {
        this.mainController = mainController;
    }

    public Result run() {
        String[] parameters = mainController.getView().getParameters();
        return new Result(ResultCode.OK);
        // TODO 23 лекция 2:05:00 подумать над применением в разрезе текущей задачи
    }

    public void printResult(Result result) {
        mainController.getView().printResult(result);
    }
}
