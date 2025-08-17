package com.javarush.NWA51.Poltavets.island.controller;

import com.javarush.NWA51.Poltavets.island.view.View;

public class MainController {
    private View view;
    public MainController(View view) {
        this.view=view;
    }

    public View getView() {
        return view;
    }
}
