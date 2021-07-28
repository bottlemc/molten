package com.github.glassmc.molten.client.v1_8_9;

import com.github.bottlemc.molten.IScreenInterface;
import com.github.glassmc.loader.GlassLoader;
import com.github.glassmc.loader.Listener;

public class MoltenInitializeListener implements Listener {

    @Override
    public void run() {
        GlassLoader.getInstance().registerInterface(IScreenInterface.class, new ScreenInterface());
        GlassLoader.getInstance().registerTransformer(MoltenTransformer.class);
    }

}
