package com.javarush.NWA51.Poltavets.island.view;

import com.javarush.NWA51.Poltavets.island.entity.Result;

import static com.javarush.NWA51.Poltavets.island.constants.ApplicationConstants.EXEPTION;
import static com.javarush.NWA51.Poltavets.island.constants.ApplicationConstants.SUCCESS;

public class ConsoleView implements View {
    @Override
    public String[] getParameters() {
        return new String[0];
    }

    @Override
    public void printResult(Result result) {
        switch (result.getResultCode()) {
            case OK -> System.out.println(SUCCESS);
            case ERROR -> System.out.println(EXEPTION + result.getApplicationException().getMessage());
        }
    }
}
