package com.github.bottlemc.molten;

import com.github.glassmc.sculpt.framework.Color;
import com.github.glassmc.sculpt.framework.constraint.Absolute;
import com.github.glassmc.sculpt.framework.element.Container;

public class TestWindow extends Window {

    public TestWindow() {
        super(new Container()
            .backgroundColor(new Absolute(new Color(1.0, 0.0, 0.0))));

        this.setX(0.5);
        this.setY(0.5);
        this.setWidth(0.5);
        this.setHeight(0.5);
    }

}
