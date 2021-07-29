package com.github.bottlemc.molten;

import com.github.glassmc.loader.GlassLoader;
import com.github.glassmc.sculpt.framework.Color;
import com.github.glassmc.sculpt.framework.Vector2D;
import com.github.glassmc.sculpt.framework.backend.IBackend;
import com.github.glassmc.sculpt.framework.constraint.Absolute;
import com.github.glassmc.sculpt.framework.constraint.Relative;
import com.github.glassmc.sculpt.framework.element.Container;
import com.github.glassmc.sculpt.framework.layout.RegionLayout;
import com.github.glassmc.sculpt.v1_8_9.SculptInitializeListener;

public class Window {

    private double x, y;
    private double width, height;

    private double initialX, initialY;
    private double initialMouseX, initialMouseY;
    private boolean dragged;

    private final Container container;

    public Window(Container container) {
        this.container = new Container()
            .getLayout(RegionLayout.class)
            .add(new Container()
                .backgroundColor(new Absolute(new Color(0., 1., 0.)))
                .height(new Absolute(10))
                .onClick(container1 -> {
                    IBackend backend = GlassLoader.getInstance().getInterface(IBackend.class);
                    initialX = x;
                    initialY = y;
                    Vector2D mouseLocation = backend.getMouseLocation();
                    initialMouseX = mouseLocation.getFirst();
                    initialMouseY = mouseLocation.getSecond();
                    dragged = true;
                })
                .onRelease(container1 -> {
                    dragged = false;
                }),
                RegionLayout.Region.TOP)
            .add(container,
                RegionLayout.Region.BOTTOM)
            .getContainer();
    }

    public void update() {
        this.container
            .x(new Relative(this.x))
            .y(new Relative(this.y))
            .width(new Relative(this.width))
            .height(new Relative(this.height));
        if(dragged) {
            IBackend backend = GlassLoader.getInstance().getInterface(IBackend.class);
            Vector2D mouseLocation = backend.getMouseLocation();
            double deltaMouseX = mouseLocation.getFirst() - initialMouseX;
            double deltaMouseY = mouseLocation.getSecond() - initialMouseY;
            double deltaRelativeMouseX = deltaMouseX / backend.getDimension().getFirst();
            double deltaRelativeMouseY = deltaMouseY / backend.getDimension().getSecond();

            this.x = this.initialX + deltaRelativeMouseX;
            this.y = this.initialY + deltaRelativeMouseY;
        }
    }

    public void setX(double x) {
        this.x = x;
    }

    public void setY(double y) {
        this.y = y;
    }

    public void setWidth(double width) {
        this.width = width;
    }

    public void setHeight(double height) {
        this.height = height;
    }

    public Container getContainer() {
        return container;
    }

}
