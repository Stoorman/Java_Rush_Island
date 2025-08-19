package com.javarush.NWA51.Poltavets.island;


import com.javarush.NWA51.Poltavets.island.app.Application;
import com.javarush.NWA51.Poltavets.island.controller.MainController;
import com.javarush.NWA51.Poltavets.island.entity.Result;
import com.javarush.NWA51.Poltavets.island.service.IslandInitialization;
import com.javarush.NWA51.Poltavets.island.view.ConsoleView;
import com.javarush.NWA51.Poltavets.island.view.View;

public class EntryPoint {
    public static void main(String[] args) {


       // View view = new ConsoleView();
       // MainController mainController = new MainController(view);
       // Application application = new Application(mainController);
       // Result result = application.run();
       // application.printResult(result);





        //Далле идёт отладочный код, котрый нужно потом закомментить
        String[] parameters = {"Ява", "10", "10"};
        IslandInitialization islandInitialization = new IslandInitialization(parameters);
        islandInitialization.print();

    }
}

