package com.github.glassmc.molten.client.v1_8_9;

import com.github.bottlemc.molten.IScreenInterface;
import com.github.glassmc.sculpt.framework.Color;
import com.github.glassmc.sculpt.framework.Vector2D;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.Window;
import org.lwjgl.input.Cursor;
import org.lwjgl.input.Mouse;

import java.awt.*;
import java.util.List;

public class ScreenInterface implements IScreenInterface {

    @Override
    public double getWidth() {
        Window window = new Window(MinecraftClient.getInstance());
        return window.getScaledWidth();
    }

    @Override
    public double getHeight() {
        Window window = new Window(MinecraftClient.getInstance());
        return window.getScaledHeight();
    }

    @Override
    public void drawRectangle(double x, double y, double width, double height, double cornerRadius, Color color) {

    }

    @Override
    public void drawText(Font font, String text, double x, double y, Color color) {

    }

    @Override
    public Vector2D getMousePosition() {
        return null;
    }

    @Override
    public List<Vector2D> getMouseClicks() {
        return null;
    }

}
