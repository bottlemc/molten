package com.github.glassmc.molten.client.v1_8_9;

import com.github.bottlemc.molten.Molten;
import com.github.glassmc.loader.GlassLoader;
import org.lwjgl.input.Keyboard;

public class MoltenHook {

    public static void onRender() {
        GlassLoader.getInstance().getAPI(Molten.class).render();
    }

    public static void onKeyPress() {
        if (Keyboard.getEventKeyState() && !Keyboard.isRepeatEvent()) {
            if (Keyboard.getEventKey() == Keyboard.KEY_RSHIFT) {
                GlassLoader.getInstance().getAPI(Molten.class).setShowing(true);
            } else if (Keyboard.getEventKey() == Keyboard.KEY_ESCAPE) {
                GlassLoader.getInstance().getAPI(Molten.class).setShowing(false);
            }
        }
    }

}
