package com.github.bottlemc.molten;

import com.github.glassmc.sculpt.framework.element.Container;

public class TestWindow extends Window {

    public TestWindow() {
        super("TEST WINDOW", new Container());

        this.setWidth(0.5);
        this.setHeight(0.5);
    }

}
