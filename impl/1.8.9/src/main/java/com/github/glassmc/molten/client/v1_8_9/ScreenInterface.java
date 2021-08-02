package com.github.glassmc.molten.client.v1_8_9;

import com.github.bottlemc.molten.IScreenInterface;
import com.github.glassmc.sculpt.framework.Color;
import com.github.glassmc.sculpt.framework.Vector2D;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.TitleScreen;
import net.minecraft.client.util.Window;

import java.awt.*;
import java.util.List;

public class ScreenInterface implements IScreenInterface {

    @Override
    public void setCurrentScreenDummy() {
        MinecraftClient.getInstance().openScreen(new DummyScreen());
    }

    @Override
    public Object getCurrentScreen() {
        return MinecraftClient.getInstance().currentScreen;
    }

    @Override
    public void setCurrentScreen(Object screen) {
        MinecraftClient.getInstance().openScreen((Screen) screen);
    }

}
