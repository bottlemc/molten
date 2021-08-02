package com.github.glassmc.molten.client.v1_8_9;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.Window;

import java.awt.*;

public class DummyScreen extends Screen {

    @Override
    public void render(int mouseX, int mouseY, float v) {
        if (MinecraftClient.getInstance().world == null) {
            Window window = new Window(MinecraftClient.getInstance());
            DrawableHelper.fill(0, 0, window.getScaledWidth(), window.getScaledHeight(), new Color(60, 60, 70).getRGB());
        }
        super.render(mouseX, mouseY, v);
    }

    @Override
    protected void keyPressed(char c, int i) {

    }

}
