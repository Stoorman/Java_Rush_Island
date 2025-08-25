package com.javarush.NWA51.Poltavets.island;

import com.javarush.NWA51.Poltavets.island.entity.Cell;
import com.javarush.NWA51.Poltavets.island.service.IslandInitialization;
import com.javarush.NWA51.Poltavets.island.service.dto.IslandConfigDTO;
import com.javarush.NWA51.Poltavets.island.view.GUIView;
import javafx.application.Application;
import javafx.stage.Stage;

public class EntryPoint extends Application {

    @Override
    public void start(Stage stage) {
        // Конфигурация острова
        IslandConfigDTO configDTO = new IslandConfigDTO.Builder()
                .nameIsland("Ява")
                .xAxisSize(60)
                .yAxisSize(40)

                .soilType(4)
                .grassDesert(50, 200)
                .grassForest(50, 400)
                .grassJungle(50, 1000)
                .grassSavvanna(50, 600)
                .build();

        // Инициализация острова
        IslandInitialization islandInitialization = new IslandInitialization(configDTO);
        Cell[][] island = islandInitialization.getIsland(); // допустим, у тебя есть геттер

        // Отображение через GUIView
        GUIView guiView = new GUIView(stage);
        guiView.show(island);
    }

    public static void main(String[] args) {
        launch(args);
    }
}

    //public static void main(String[] args) {


       // View view = new ConsoleView();
       // MainController mainController = new MainController(view);
       // Application application = new Application(mainController);
       // Result result = application.run();
       // application.printResult(result);





        //Далле идёт отладочный код, котрый нужно потом закомментить
//        IslandConfigDTO configDTO = new IslandConfigDTO.Builder()
//                .nameIsland("Ява")
//                .xAxisSize(10)
//                .yAxisSize(10)
//                .soilType(4)
//                .grassDesert(50,200)
//                .grassForest(50,400)
//                .grassJungle(50,1000)
//                .grassSavvanna(50,600).build();
//
//        IslandInitialization islandInitialization = new IslandInitialization(configDTO);
//        islandInitialization.print();



