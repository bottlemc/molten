package com.github.bottlemc.molten;

import com.github.glassmc.loader.GlassLoader;
import com.github.glassmc.loader.Listener;

public class MoltenInitializeListener implements Listener {

    @Override
    public void run() {
        GlassLoader.getInstance().registerAPI(new Molten());
        GlassLoader.getInstance().getAPI(Molten.class).open(new TestWindow());
    }

}
