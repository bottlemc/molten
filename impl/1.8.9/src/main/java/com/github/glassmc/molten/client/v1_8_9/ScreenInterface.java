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
        //System.out.println(DummyScreen.class.getClassLoader());
        //System.out.println(this.getClass().getClassLoader() + " " + MinecraftClient.class.getClassLoader());
        MinecraftClient.getInstance().openScreen(new DummyScreen());

        //MinecraftClient.getInstance().closeScreen();
        //MinecraftClient.getInstance().openScreen(new TitleScreen());
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
