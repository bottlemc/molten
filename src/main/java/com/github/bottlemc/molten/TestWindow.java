package com.github.bottlemc.molten;

import com.github.glassmc.sculpt.framework.Color;
import com.github.glassmc.sculpt.framework.constraint.Hover;
import com.github.glassmc.sculpt.framework.element.Container;

public class TestWindow extends Window {

    public TestWindow() {
        super(new Container()
            .backgroundColor(new Hover(500, new Color(1., 0., 0.), new Color(0., 1., 0.))));

        this.setX(0);
        this.setY(0);
        this.setWidth(0.5);
        this.setHeight(0.5);
    }

}
