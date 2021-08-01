package com.github.bottlemc.molten;

import com.github.bottlemc.flame.Flame;
import com.github.bottlemc.flame.FlameConfiguration;
import com.github.glassmc.loader.GlassLoader;
import com.github.glassmc.sculpt.framework.Pair;
import com.github.glassmc.sculpt.framework.Vector2D;
import com.github.glassmc.sculpt.framework.backend.IBackend;
import com.github.glassmc.sculpt.framework.constraint.Absolute;
import com.github.glassmc.sculpt.framework.constraint.Relative;
import com.github.glassmc.sculpt.framework.constraint.Side;
import com.github.glassmc.sculpt.framework.element.Container;
import com.github.glassmc.sculpt.framework.element.Element;
import com.github.glassmc.sculpt.framework.element.Text;
import com.github.glassmc.sculpt.framework.layout.RegionLayout;

import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Window {

    private final String icon;

    private boolean maximized;
    private double x, y;
    private double width, height;

    private double cachedWidth, cachedHeight;

    private double initialX, initialY;
    private double initialWidth, initialHeight;
    private double initialMouseX, initialMouseY;

    private final Container container;

    private final Molten molten = GlassLoader.getInstance().getAPI(Molten.class);

    public Window(String title, String icon,  Container container) {
        this.icon = icon;
        Font raleway = null;

        try {
            raleway = Font.createFont(Font.TRUETYPE_FONT, Objects.requireNonNull(Window.class.getClassLoader().getResourceAsStream("Raleway-Bold.ttf")));
        } catch (FontFormatException | IOException e) {
            e.printStackTrace();
        }

        FlameConfiguration configuration = GlassLoader.getInstance().getAPI(Flame.class).getConfiguration();
        this.container = new Container()
            .adjustElements(false)
            .getLayout(RegionLayout.class)
            .add(new Container()
                .backgroundColor(new Absolute(configuration.background))
                .cornerRadius(new Absolute(2.5))
                .getLayout(RegionLayout.class)
                .add(new Container()
                    .height(new Absolute(10))
                    .cornerRadius(new Pair<>(Element.Direction.LEFT, Element.Direction.TOP), new Absolute(2.5))
                    .cornerRadius(new Pair<>(Element.Direction.TOP, Element.Direction.RIGHT), new Absolute(2.5))
                    .backgroundColor(new Absolute(configuration.elementBackground))
                    .onClick(container1 -> {
                        setInitials();
                        if (molten.getInteractedWindow() != this) {
                            molten.getCurrentInteracts().clear();
                        }
                        molten.getCurrentInteracts().add(Molten.InteractType.MOVE);
                        molten.setInteractedWindow(this);
                    })
                    .onRelease(container1 -> {
                        molten.getCurrentInteracts().remove(Molten.InteractType.MOVE);
                        molten.setInteractedWindow(null);
                    })
                    .getLayout(RegionLayout.class)
                    .add(new Container()
                        .x(new Side(Side.Direction.NEGATIVE))
                        .width(new Relative(0.6, 0, true))
                        .height(new Relative(0.6))
                        .apply(container1 -> {
                            if(icon != null) {
                                container1.backgroundImage(icon);
                            } else {
                                container1.backgroundColor(new Absolute(configuration.foregroundPrimary));
                            }
                        })
                        .padding(Element.Direction.LEFT, new Absolute(3)),
                        RegionLayout.Region.CENTER)
                    .add(new Text()
                        .x(new Side(Side.Direction.NEGATIVE))
                        .size(new Relative(0.425, 0, true))
                        .text(title)
                        .font(raleway)
                        .padding(Element.Direction.LEFT, new Absolute(3))
                        .color(new Absolute(configuration.foregroundPrimary)),
                        RegionLayout.Region.CENTER)
                    .add(new Container()
                        .x(new Side(Side.Direction.POSITIVE))
                        .width(new Relative(0.6, 0, true))
                        .height(new Relative(0.6))
                        .padding(Element.Direction.RIGHT, new Absolute(2))
                        .onClick(container1 -> {
                            molten.getCurrentInteracts().clear();
                            molten.getCurrentInteracts().add(Molten.InteractType.CLOSE);
                        })
                        .getLayout(RegionLayout.class)
                        .add(new Container()
                            .backgroundColor(new Absolute(configuration.foregroundPrimary))
                            .backgroundImage("molten/exit.png")
                            .width(new Relative(0.6))
                            .height(new Relative(0.6)),
                            RegionLayout.Region.CENTER)
                        .getContainer(),
                        RegionLayout.Region.CENTER)
                    .add(new Container()
                        .x(new Side(Side.Direction.POSITIVE))
                        .width(new Relative(0.6, 0, true))
                        .height(new Relative(0.6))
                        .padding(Element.Direction.RIGHT, new Absolute(2))
                        .onClick(container1 -> {
                            molten.getCurrentInteracts().clear();
                            molten.getCurrentInteracts().add(Molten.InteractType.MAXIMIZE);
                        })
                        .getLayout(RegionLayout.class)
                        .add(new Container()
                            .backgroundColor(new Absolute(configuration.foregroundPrimary))
                            .backgroundImage("molten/maximize.png")
                            .width(new Relative(0.6))
                            .height(new Relative(0.6)),
                            RegionLayout.Region.CENTER)
                        .getContainer(),
                        RegionLayout.Region.CENTER)
                    .getContainer(),
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
                    if (molten.getInteractedWindow() != this) {
                        molten.getCurrentInteracts().clear();
                    }
                    molten.getCurrentInteracts().add(Molten.InteractType.RESIZE_LEFT);
                    molten.getCurrentInteracts().remove(Molten.InteractType.MOVE);
                    molten.setInteractedWindow(this);
                })
                .onRelease(container1 -> {
                    molten.getCurrentInteracts().remove(Molten.InteractType.RESIZE_LEFT);
                    molten.setInteractedWindow(null);
                }),
                RegionLayout.Region.CENTER)
            .add(new Container()
                .y(new Relative(-0.5))
                .width(new Relative(1, 5))
                .height(new Absolute(5))
                .onClick(container1 -> {
                    setInitials();
                    if (molten.getInteractedWindow() != this) {
                        molten.getCurrentInteracts().clear();
                    }
                    molten.getCurrentInteracts().add(Molten.InteractType.RESIZE_TOP);
                    molten.getCurrentInteracts().remove(Molten.InteractType.MOVE);
                    molten.setInteractedWindow(this);
                })
                .onRelease(container1 -> {
                    molten.getCurrentInteracts().remove(Molten.InteractType.RESIZE_TOP);
                    molten.setInteractedWindow(null);
                }),
                RegionLayout.Region.CENTER)
            .add(new Container()
                .x(new Relative(0.5))
                .width(new Absolute(5))
                .height(new Relative(1, 5))
                .onClick(container1 -> {
                    setInitials();
                    if (molten.getInteractedWindow() != this) {
                        molten.getCurrentInteracts().clear();
                    }
                    molten.getCurrentInteracts().add(Molten.InteractType.RESIZE_RIGHT);
                    molten.getCurrentInteracts().remove(Molten.InteractType.MOVE);
                    molten.setInteractedWindow(this);
                })
                .onRelease(container1 -> {
                    molten.getCurrentInteracts().remove(Molten.InteractType.RESIZE_RIGHT);
                    molten.setInteractedWindow(null);
                }),
                RegionLayout.Region.CENTER)
            .add(new Container()
                .y(new Relative(0.5))
                .width(new Relative(1, 5))
                .height(new Absolute(5))
                .onClick(container1 -> {
                    if (molten.getInteractedWindow() == null) {
                        setInitials();
                        molten.getCurrentInteracts().add(Molten.InteractType.RESIZE_BOTTOM);
                        molten.getCurrentInteracts().remove(Molten.InteractType.MOVE);
                        molten.setInteractedWindow(this);
                    }
                })
                .onRelease(container1 -> {
                    molten.getCurrentInteracts().remove(Molten.InteractType.RESIZE_BOTTOM);
                    molten.setInteractedWindow(null);
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

        if(molten.getCurrentInteracts().size() > 0 && molten.getInteractedWindow() == this) {
            IBackend backend = GlassLoader.getInstance().getInterface(IBackend.class);
            Vector2D mouseLocation = backend.getMouseLocation();
            double deltaMouseX = mouseLocation.getFirst() - initialMouseX;
            double deltaMouseY = mouseLocation.getSecond() - initialMouseY;
            double deltaRelativeMouseX = deltaMouseX / backend.getDimension().getFirst();
            double deltaRelativeMouseY = deltaMouseY / backend.getDimension().getSecond();
            for (Molten.InteractType type : new ArrayList<>(molten.getCurrentInteracts())) {
                if(type == Molten.InteractType.MOVE) {
                    this.x = this.initialX + deltaRelativeMouseX;
                    this.y = this.initialY + deltaRelativeMouseY;

                    if (maximized && (deltaRelativeMouseX > 0 || deltaRelativeMouseY > 0)) {
                        this.width = cachedWidth;
                        this.height = cachedHeight;
                        this.initialX = (mouseLocation.getFirst() / backend.getDimension().getFirst() - width * (mouseLocation.getFirst() / backend.getDimension().getFirst())) - width / 2;
                        this.x = initialX;
                        this.initialY = -0.5 + height / 2;
                        this.y = initialY;
                        maximized = false;
                    }
                } else if(type == Molten.InteractType.RESIZE_LEFT) {
                    this.x = this.initialX + deltaRelativeMouseX / 2;
                    this.width = this.initialWidth - deltaRelativeMouseX;
                } else if(type == Molten.InteractType.RESIZE_TOP) {
                    this.y = this.initialY + deltaRelativeMouseY / 2;
                    this.height = this.initialHeight - deltaRelativeMouseY;
                } else if(type == Molten.InteractType.RESIZE_RIGHT) {
                    this.x = this.initialX + deltaRelativeMouseX / 2;
                    this.width = this.initialWidth + deltaRelativeMouseX;
                } else if(type == Molten.InteractType.RESIZE_BOTTOM) {
                    this.y = this.initialY + deltaRelativeMouseY / 2;
                    this.height = this.initialHeight + deltaRelativeMouseY;
                } else if(type == Molten.InteractType.MAXIMIZE) {
                    cachedWidth = width;
                    cachedHeight = height;
                    x = 0;
                    y = 0;
                    width = 1;
                    height = 1;
                    maximized = true;

                    molten.getCurrentInteracts().remove(Molten.InteractType.MAXIMIZE);
                } else if(type == Molten.InteractType.CLOSE) {
                    this.close();
                }
            }
        }
    }

    public void close() {
        GlassLoader.getInstance().getAPI(Molten.class).close(this);
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

    public String getIcon() {
        return icon;
    }

    public Container getContainer() {
        return container;
    }

}
