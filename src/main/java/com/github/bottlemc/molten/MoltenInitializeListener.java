package com.github.bottlemc.molten;

import com.github.glassmc.loader.GlassLoader;
import com.github.glassmc.loader.Listener;
import com.github.glassmc.sculpt.framework.backend.IBackend;

public class MoltenInitializeListener implements Listener {

    @Override
    public void run() {
        GlassLoader.getInstance().registerAPI(new Molten());
        GlassLoader.getInstance().getAPI(Molten.class).addPinnedItem("test", "molten/test/icon.png", TestWindow::new);
    }

}
