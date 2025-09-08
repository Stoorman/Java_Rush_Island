package com.javarush.NWA51.Poltavets.island;

import com.javarush.NWA51.Poltavets.island.app.ApplicationIsland;
import com.javarush.NWA51.Poltavets.island.controller.MainController;
import com.javarush.NWA51.Poltavets.island.view.GUIView;
import javafx.application.Application;
import javafx.stage.Stage;

public class EntryPoint extends Application {

    @Override
    public void start(Stage stage) {
        // Создаём GUI
        GUIView guiView = new GUIView(stage);

        // Создаём контроллер и связываем с GUI
        MainController mainController = new MainController(guiView);
        guiView.setMainController(mainController);

        // Создаём ApplicationIsland и запускаем первичную инициализацию
        ApplicationIsland applicationIsland = new ApplicationIsland(mainController);
        applicationIsland.start();
    }

    public static void main(String[] args) {
        launch(args);
    }
}





