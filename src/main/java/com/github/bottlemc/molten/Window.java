package com.github.bottlemc.molten;

import com.github.glassmc.loader.GlassLoader;
import com.github.glassmc.sculpt.framework.Color;
import com.github.glassmc.sculpt.framework.Vector2D;
import com.github.glassmc.sculpt.framework.backend.IBackend;
import com.github.glassmc.sculpt.framework.constraint.Absolute;
import com.github.glassmc.sculpt.framework.constraint.Relative;
import com.github.glassmc.sculpt.framework.element.Container;
import com.github.glassmc.sculpt.framework.layout.RegionLayout;

import java.util.ArrayList;
import java.util.List;

public class Window {

    private double x, y;
    private double width, height;

    private double initialX, initialY;
    private double initialWidth, initialHeight;
    private double initialMouseX, initialMouseY;

    private final Container container;

    private final List<InteractType> currentInteracts = new ArrayList<>();

    public Window(Container container) {
        this.container = new Container()
            .adjustElements(false)
            .getLayout(RegionLayout.class)
            .add(new Container()
                .getLayout(RegionLayout.class)
                .add(new Container()
                    .height(new Absolute(10))
                    .backgroundColor(new Absolute(new Color(0., 1., 0.)))
                    .onClick(container1 -> {
                        setInitials();
                        currentInteracts.add(InteractType.MOVE);
                    })
                    .onRelease(container1 -> {
                        currentInteracts.remove(InteractType.MOVE);
                    }),
                    RegionLayout.Region.TOP)
                .add(container,
                        RegionLayout.Region.BOTTOM)
                .getContainer(),
                RegionLayout.Region.CENTER)
            .add(new Container()
                .x(new Relative(-0.5))
                .width(new Absolute(5))
                .height(new Relative(1, 5))
                .onClick(container1 -> {
                    setInitials();
                    currentInteracts.add(InteractType.RESIZE_LEFT);
                    currentInteracts.remove(InteractType.MOVE);
                })
                .onRelease(container1 -> {
                    currentInteracts.remove(InteractType.RESIZE_LEFT);
                }),
                RegionLayout.Region.CENTER)
            .add(new Container()
                .y(new Relative(-0.5))
                .width(new Relative(1, 5))
                .height(new Absolute(5))
                .onClick(container1 -> {
                    setInitials();
                    currentInteracts.add(InteractType.RESIZE_TOP);
                    currentInteracts.remove(InteractType.MOVE);
                })
                .onRelease(container1 -> {
                    currentInteracts.remove(InteractType.RESIZE_TOP);
                }),
                RegionLayout.Region.CENTER)
            .add(new Container()
                .x(new Relative(0.5))
                .width(new Absolute(5))
                .height(new Relative(1, 5))
                .onClick(container1 -> {
                    setInitials();
                    currentInteracts.add(InteractType.RESIZE_RIGHT);
                    currentInteracts.remove(InteractType.MOVE);
                })
                .onRelease(container1 -> {
                    currentInteracts.remove(InteractType.RESIZE_RIGHT);
                }),
                RegionLayout.Region.CENTER)
            .add(new Container()
                .y(new Relative(0.5))
                .width(new Relative(1, 5))
                .height(new Absolute(5))
                .onClick(container1 -> {
                    setInitials();
                    currentInteracts.add(InteractType.RESIZE_BOTTOM);
                    currentInteracts.remove(InteractType.MOVE);
                })
                .onRelease(container1 -> {
                    currentInteracts.remove(InteractType.RESIZE_BOTTOM);
                }),
                RegionLayout.Region.CENTER)
        .getContainer();
    }

    private void setInitials() {
        IBackend backend = GlassLoader.getInstance().getInterface(IBackend.class);
        initialX = x;
        initialY = y;
        initialWidth = width;
        initialHeight = height;
        Vector2D mouseLocation = backend.getMouseLocation();
        initialMouseX = mouseLocation.getFirst();
        initialMouseY = mouseLocation.getSecond();
    }

    public void update() {
        this.container
            .x(new Relative(this.x))
            .y(new Relative(this.y))
            .width(new Relative(this.width))
            .height(new Relative(this.height));

        if(this.currentInteracts.size() > 0) {
            IBackend backend = GlassLoader.getInstance().getInterface(IBackend.class);
            Vector2D mouseLocation = backend.getMouseLocation();
            double deltaMouseX = mouseLocation.getFirst() - initialMouseX;
            double deltaMouseY = mouseLocation.getSecond() - initialMouseY;
            double deltaRelativeMouseX = deltaMouseX / backend.getDimension().getFirst();
            double deltaRelativeMouseY = deltaMouseY / backend.getDimension().getSecond();
            for(InteractType type : this.currentInteracts) {
                if(type == InteractType.MOVE) {
                    this.x = this.initialX + deltaRelativeMouseX;
                    this.y = this.initialY + deltaRelativeMouseY;
                } else if(type == InteractType.RESIZE_LEFT) {
                    this.x = this.initialX + deltaRelativeMouseX / 2;
                    this.width = this.initialWidth - deltaRelativeMouseX;
                } else if(type == InteractType.RESIZE_TOP) {
                    this.y = this.initialY + deltaRelativeMouseY / 2;
                    this.height = this.initialHeight - deltaRelativeMouseY;
                } else if(type == InteractType.RESIZE_RIGHT) {
                    this.x = this.initialX + deltaRelativeMouseX / 2;
                    this.width = this.initialWidth + deltaRelativeMouseX;
                } else if(type == InteractType.RESIZE_BOTTOM) {
                    this.y = this.initialY + deltaRelativeMouseY / 2;
                    this.height = this.initialHeight + deltaRelativeMouseY;
                }
            }
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

    private enum InteractType {
        MOVE, RESIZE_LEFT, RESIZE_TOP, RESIZE_RIGHT, RESIZE_BOTTOM
    }

}
