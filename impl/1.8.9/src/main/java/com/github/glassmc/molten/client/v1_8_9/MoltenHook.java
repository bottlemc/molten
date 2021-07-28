package com.github.glassmc.molten.client.v1_8_9;

import com.github.bottlemc.molten.Molten;
import com.github.glassmc.loader.GlassLoader;

public class MoltenHook {

    public static void onRender() {
        GlassLoader.getInstance().getAPI(Molten.class).render();
    }

}
