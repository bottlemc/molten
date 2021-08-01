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
import java.util.Objects;

public class Window {

    private final String icon;

    private boolean closing;
    private long closeStartTime;

    private boolean maximized, prevMaximized = true;
    private double x, y;
    private double width, height;

    private double cachedWidth, cachedHeight;

    private double initialX, initialY;
    private double initialWidth, initialHeight;
    private double initialMouseX, initialMouseY;

    private final Container container;
    private final Container main;
    private final Container header;

    private boolean initialized = false;

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
            .add(main = new Container()
                .backgroundColor(new Absolute(configuration.background))
                .getLayout(RegionLayout.class)
                .add(header = new Container()
                    .height(new Absolute(10))
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
                        if (molten.getInteractedWindow() != null) {
                            molten.getCurrentInteracts().remove(Molten.InteractType.MOVE);
                            molten.setInteractedWindow(null);
                            this.checkSnapWindow();
                        }
                    })
                    .getLayout(RegionLayout.class)
                    .add(new Container()
                        .x(new Side(Side.Direction.NEGATIVE))
                        .width(new Relative(0.6, 0, true))
                        .height(new Relative(0.6))
                        .apply(container1 -> {
                            container1.backgroundColor(new Absolute(configuration.foregroundPrimary));
                            if(icon != null) {
                                container1.backgroundImage(icon);
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
                            molten.getCurrentInteracts().add(Molten.InteractType.MAXIMIZE_TOP);
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
                    if (molten.getInteractedWindow() == this && molten.getCurrentInteracts().contains(Molten.InteractType.RESIZE_LEFT)) {
                        molten.getCurrentInteracts().remove(Molten.InteractType.RESIZE_LEFT);
                        molten.setInteractedWindow(null);
                    }
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
                    if (molten.getInteractedWindow() == this && molten.getCurrentInteracts().contains(Molten.InteractType.RESIZE_TOP)) {
                        molten.getCurrentInteracts().remove(Molten.InteractType.RESIZE_TOP);
                        molten.setInteractedWindow(null);
                    }
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
                    if (molten.getInteractedWindow() == this && molten.getCurrentInteracts().contains(Molten.InteractType.RESIZE_RIGHT)) {
                        molten.getCurrentInteracts().remove(Molten.InteractType.RESIZE_RIGHT);
                        molten.setInteractedWindow(null);
                    }
                }),
                RegionLayout.Region.CENTER)
            .add(new Container()
                .y(new Relative(0.5))
                .width(new Relative(1, 5))
                .height(new Absolute(5))
                .onClick(container1 -> {
                    setInitials();
                    if (molten.getInteractedWindow() != this) {
                        molten.getCurrentInteracts().clear();
                    }
                    molten.getCurrentInteracts().add(Molten.InteractType.RESIZE_BOTTOM);
                    molten.getCurrentInteracts().remove(Molten.InteractType.MOVE);
                    molten.setInteractedWindow(this);
                })
                .onRelease(container1 -> {
                    if (molten.getInteractedWindow() == this && molten.getCurrentInteracts().contains(Molten.InteractType.RESIZE_BOTTOM)) {
                        molten.getCurrentInteracts().remove(Molten.InteractType.RESIZE_BOTTOM);
                        molten.setInteractedWindow(null);
                    }
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
        if (!initialized) {
            NewPosition newPosition = new NewPosition(100, x, x, y, y, 0, width, 0, height);
            this.container
                .x(newPosition)
                .y(newPosition)
                .width(newPosition)
                .height(newPosition);
            initialized = true;
        }

        if (molten.getCurrentInteracts().size() > 0 && molten.getInteractedWindow() == this) {
            IBackend backend = GlassLoader.getInstance().getInterface(IBackend.class);
            Vector2D mouseLocation = backend.getMouseLocation();
            double deltaMouseX = mouseLocation.getFirst() - initialMouseX;
            double deltaMouseY = mouseLocation.getSecond() - initialMouseY;
            double deltaRelativeMouseX = deltaMouseX / backend.getDimension().getFirst();
            double deltaRelativeMouseY = deltaMouseY / backend.getDimension().getSecond();
            double relativeMouseX = mouseLocation.getFirst() / backend.getDimension().getFirst();
            double relativeMouseY = mouseLocation.getSecond() / backend.getDimension().getSecond();
            for (Molten.InteractType type : new ArrayList<>(molten.getCurrentInteracts())) {
                if(type == Molten.InteractType.MOVE) {
                    this.x = this.initialX + deltaRelativeMouseX;
                    this.y = this.initialY + deltaRelativeMouseY;

                    if (maximized && (deltaRelativeMouseX > 0 || deltaRelativeMouseY > 0)) {
                        double absoluteWidth = width * backend.getDimension().getFirst();
                        double absoluteX = (x + 0.5) * backend.getDimension().getFirst() - absoluteWidth / 2;
                        this.width = cachedWidth;
                        this.height = cachedHeight;
                        double currentMouseRelativeX = mouseLocation.getFirst() / backend.getDimension().getFirst();
                        double grabbedRelativeX = (mouseLocation.getFirst() - absoluteX) / absoluteWidth;

                        this.initialX = currentMouseRelativeX - grabbedRelativeX * width - width / 2;
                        this.x = initialX;
                        this.initialY = -0.5 + height / 2;
                        this.y = initialY;
                        maximized = false;

                        NewPosition newPosition = new NewPosition(100, x, x, y, y, 1, width, 1, height);
                        this.container
                            .width(newPosition)
                            .height(newPosition);
                    }

                    this.updatePosition();
                } else if(type == Molten.InteractType.RESIZE_LEFT) {
                    this.x = this.initialX + deltaRelativeMouseX / 2;
                    this.width = this.initialWidth - deltaRelativeMouseX;
                    this.updatePosition();
                    this.updateSize();
                } else if(type == Molten.InteractType.RESIZE_TOP) {
                    this.y = this.initialY + deltaRelativeMouseY / 2;
                    this.height = this.initialHeight - deltaRelativeMouseY;
                    this.updatePosition();
                    this.updateSize();
                } else if(type == Molten.InteractType.RESIZE_RIGHT) {
                    this.x = this.initialX + deltaRelativeMouseX / 2;
                    this.width = this.initialWidth + deltaRelativeMouseX;
                    this.updatePosition();
                    this.updateSize();
                } else if(type == Molten.InteractType.RESIZE_BOTTOM) {
                    this.y = this.initialY + deltaRelativeMouseY / 2;
                    this.height = this.initialHeight + deltaRelativeMouseY;
                    this.updatePosition();
                    this.updateSize();
                } else if(type == Molten.InteractType.MAXIMIZE_TOP) {
                    double cachedX = x;
                    double cachedY = y;
                    cachedWidth = width;
                    cachedHeight = height;
                    x = 0;
                    y = 0;
                    width = 1;
                    height = 1;
                    maximized = true;

                    molten.getCurrentInteracts().remove(Molten.InteractType.MAXIMIZE_TOP);

                    NewPosition newPosition = new NewPosition(100, cachedX, x, cachedY, y, cachedWidth, 1, cachedHeight, 1);
                    this.container
                        .x(newPosition)
                        .y(newPosition)
                        .width(newPosition)
                        .height(newPosition);
                } else if(type == Molten.InteractType.MAXIMIZE_LEFT) {
                    double cachedX = x;
                    double cachedY = y;
                    cachedWidth = width;
                    cachedHeight = height;
                    x = -0.25;
                    y = 0;
                    width = 0.5;
                    height = 1;
                    maximized = true;

                    molten.getCurrentInteracts().remove(Molten.InteractType.MAXIMIZE_LEFT);

                    NewPosition newPosition = new NewPosition(100, cachedX, x, cachedY, y, cachedWidth, 0.5, cachedHeight, 1);
                    this.container
                            .x(newPosition)
                            .y(newPosition)
                            .width(newPosition)
                            .height(newPosition);
                } else if(type == Molten.InteractType.MAXIMIZE_RIGHT) {
                    double cachedX = x;
                    double cachedY = y;
                    cachedWidth = width;
                    cachedHeight = height;
                    x = 0.25;
                    y = 0;
                    width = 0.5;
                    height = 1;
                    maximized = true;

                    molten.getCurrentInteracts().remove(Molten.InteractType.MAXIMIZE_RIGHT);

                    NewPosition newPosition = new NewPosition(100, cachedX, x, cachedY, y, cachedWidth, 0.5, cachedHeight, 1);
                    this.container
                            .x(newPosition)
                            .y(newPosition)
                            .width(newPosition)
                            .height(newPosition);
                } else if(type == Molten.InteractType.CLOSE) {
                    NewPosition newPosition = new NewPosition(100, x, x, y, y, width, 0, height, 0);
                    this.container
                            .x(newPosition)
                            .y(newPosition)
                            .width(newPosition)
                            .height(newPosition);
                    closing = true;
                    closeStartTime = System.currentTimeMillis();
                }
            }
        }

        if (closing) {
            if(System.currentTimeMillis() - closeStartTime > 100) {
                this.close();
            }
        }

        if(prevMaximized != maximized) {
            if(maximized) {
                main.cornerRadius(new Absolute(0));
                header.cornerRadius(new Absolute(0));
            } else {
                main.cornerRadius(new Absolute(2.5));
                header.cornerRadius(new Pair<>(Element.Direction.LEFT, Element.Direction.TOP), new Absolute(2.5));
                header.cornerRadius(new Pair<>(Element.Direction.TOP, Element.Direction.RIGHT), new Absolute(2.5));
            }
            prevMaximized = maximized;
        }
    }

    private void checkSnapWindow() {
        IBackend backend = GlassLoader.getInstance().getInterface(IBackend.class);
        Vector2D mouseLocation = backend.getMouseLocation();
        double deltaMouseX = mouseLocation.getFirst() - initialMouseX;
        double deltaMouseY = mouseLocation.getSecond() - initialMouseY;
        double relativeMouseX = mouseLocation.getFirst() / backend.getDimension().getFirst();
        double relativeMouseY = mouseLocation.getSecond() / backend.getDimension().getSecond();

        if (relativeMouseX > 0.95) {
            molten.getCurrentInteracts().clear();
            molten.getCurrentInteracts().add(Molten.InteractType.MAXIMIZE_RIGHT);
            molten.setInteractedWindow(this);
        } else if (relativeMouseX < 0.05) {
            molten.getCurrentInteracts().clear();
            molten.getCurrentInteracts().add(Molten.InteractType.MAXIMIZE_LEFT);
            molten.setInteractedWindow(this);
        } else if (relativeMouseY < 0.05) {
            molten.getCurrentInteracts().clear();
            molten.getCurrentInteracts().add(Molten.InteractType.MAXIMIZE_TOP);
            molten.setInteractedWindow(this);
        }
    }

    private void updatePosition() {
        this.container
            .x(new Relative(this.x))
            .y(new Relative(this.y));
    }

    private void updateSize() {
        this.container
                .width(new Relative(this.width))
                .height(new Relative(this.height));
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
