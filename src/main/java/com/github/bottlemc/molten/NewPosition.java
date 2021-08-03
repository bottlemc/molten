package com.github.bottlemc.molten;

import com.github.glassmc.sculpt.framework.Renderer;
import com.github.glassmc.sculpt.framework.constraint.Constraint;
import com.github.glassmc.sculpt.framework.element.Element;

import java.util.ArrayList;
import java.util.List;

public class NewPosition extends Constraint {

    private final long time;
    private final double initialX, finalX;
    private final double initialY, finalY;
    private final double initialWidth, finalWidth;
    private final double initialHeight, finalHeight;

    public NewPosition(long time, double initialX, double finalX, double initialY, double finalY, double initialWidth, double finalWidth, double initialHeight, double finalHeight) {
        this.possibleConstructors.add(new Constructor<>());

        this.time = time;
        this.initialX = initialX;
        this.finalX = finalX;
        this.initialY = initialY;
        this.finalY = finalY;
        this.initialWidth = initialWidth;
        this.finalWidth = finalWidth;
        this.initialHeight = initialHeight;
        this.finalHeight = finalHeight;
    }

    public long getTime() {
        return time;
    }

    public double getInitialX() {
        return initialX;
    }

    public double getFinalX() {
        return finalX;
    }

    public double getInitialY() {
        return initialY;
    }

    public double getFinalY() {
        return finalY;
    }

    public double getInitialWidth() {
        return initialWidth;
    }

    public double getFinalWidth() {
        return finalWidth;
    }

    public double getInitialHeight() {
        return initialHeight;
    }

    public double getFinalHeight() {
        return finalHeight;
    }

    private static class Constructor<T extends NewPosition> extends Constraint.Constructor<T> {

        private long timeCache = 0;
        private long previousUpdate;

        protected double update() {
            long previousUpdateDelta = System.currentTimeMillis() - previousUpdate;

            if(previousUpdate != 0) {
                timeCache += previousUpdateDelta;
            }

            timeCache = Math.max(0, Math.min(this.getComponent().getTime(), timeCache));

            previousUpdate = System.currentTimeMillis();

            return (double) timeCache / this.getComponent().getTime();
        }

        @Override
        public double getXValue(Renderer renderer, List<Element.Constructor<?>> appliedElements) {
            double percent = this.update();
            NewPosition newPosition = this.getComponent();
            return ((newPosition.getFinalX() - newPosition.getInitialX()) * percent + newPosition.getInitialX()) * this.getComponent().getElement().getParent().getConstructor().getWidth();
        }

        @Override
        public double getYValue(Renderer renderer, List<Element.Constructor<?>> appliedElements) {
            double percent = this.update();
            NewPosition newPosition = this.getComponent();
            return ((newPosition.getFinalY() - newPosition.getInitialY()) * percent + newPosition.getInitialY()) * this.getComponent().getElement().getParent().getConstructor().getHeight();
        }

        @Override
        public double getWidthValue(Renderer renderer, List<Element.Constructor<?>> appliedElements) {
            double percent = this.update();
            NewPosition newPosition = this.getComponent();
            return ((newPosition.getFinalWidth() - newPosition.getInitialWidth()) * percent + newPosition.getInitialWidth()) * this.getComponent().getElement().getParent().getConstructor().getWidth();
        }

        @Override
        public double getHeightValue(Renderer renderer, List<Element.Constructor<?>> appliedElements) {
            double percent = this.update();
            NewPosition newPosition = this.getComponent();
            return ((newPosition.getFinalHeight() - newPosition.getInitialHeight()) * percent + newPosition.getInitialHeight()) * this.getComponent().getElement().getParent().getConstructor().getHeight();
        }

    }

}
