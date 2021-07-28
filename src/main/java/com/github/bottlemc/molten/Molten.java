package com.github.bottlemc.molten;

import java.util.ArrayList;
import java.util.List;

public class Molten {

    private List<Window> windows = new ArrayList<>();

    public void open(Window window) {
        this.windows.add(window);
    }

    public void render() {
        for(Window window : windows) {
            window.update();
            window.render();
        }
    }

}
