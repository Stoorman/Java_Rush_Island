package com.javarush.NWA51.Poltavets.island;

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

        // Показываем окно настроек перед запуском симуляции
        guiView.showSettingsWindow(() -> {
            // После того как пользователь подтвердил настройки, запускаем остров
            mainController.initIsland();
            mainController.showIsland();
        });
    }

    public static void main(String[] args) {
        launch(args);
    }
}
