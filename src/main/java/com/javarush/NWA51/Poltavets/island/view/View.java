package com.javarush.NWA51.Poltavets.island.view;

import com.javarush.NWA51.Poltavets.island.entity.Result;

public interface View {
    String[] getParameters();
    void printResult(Result result);
}
