package com.github.bottlemc.molten;

import com.github.glassmc.sculpt.framework.Color;
import com.github.glassmc.sculpt.framework.Vector2D;

import java.awt.*;
import java.util.List;

public interface IScreenInterface {

    double getWidth();
    double getHeight();

    void drawRectangle(double x, double y, double width, double height, double cornerRadius, Color color);
    void drawText(Font font, String text, double x, double y, Color color);

    Vector2D getMousePosition();

    List<Vector2D> getMouseClicks();

}
