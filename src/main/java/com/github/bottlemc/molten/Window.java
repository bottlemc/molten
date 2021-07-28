package com.github.bottlemc.molten;

import com.github.glassmc.loader.GlassLoader;
import com.github.glassmc.sculpt.framework.Color;
import com.github.glassmc.sculpt.framework.Renderer;
import com.github.glassmc.sculpt.framework.Vector2D;
import com.github.glassmc.sculpt.framework.backend.IBackend;
import com.github.glassmc.sculpt.framework.element.Container;

import java.awt.*;
import java.util.List;

public class Window {

    private final Renderer renderer = new Renderer(new Backend());
    private final Container container;

    private double x, y;
    private double width, height;

    public Window(Container container) {
        this.container = container;
    }

    public void update() {

    }

    public void render() {
        this.renderer.render(container);
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

    private class Backend implements IBackend {

        private final IBackend backend = GlassLoader.getInstance().getInterface(IBackend.class);

        @Override
        public Vector2D getLocation() {
            Vector2D screenDimensions = backend.getDimension();
            return new Vector2D(x * screenDimensions.getFirst(), y * screenDimensions.getSecond());
        }

        @Override
        public Vector2D getDimension() {
            Vector2D screenDimensions = backend.getDimension();
            return new Vector2D(width * screenDimensions.getFirst(), height * screenDimensions.getSecond());
        }

        @Override
        public void preRender() {

        }

        @Override
        public void drawRectangle(double v, double v1, double v2, double v3, double v4, Color color) {
            this.backend.drawRectangle(v, v1, v2, v3, v4, color);
        }

        @Override
        public void drawText(Font font, String s, double v, double v1, Color color) {
            this.backend.drawText(font, s, v, v1, color);
        }

        @Override
        public void postRender() {

        }

        @Override
        public Vector2D getMouseLocation() {
            return this.backend.getMouseLocation();
        }

        @Override
        public List<Vector2D> getMouseClicks() {
            return this.backend.getMouseClicks();
        }

    }

}
