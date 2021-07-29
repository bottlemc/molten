package com.github.bottlemc.molten;

import com.github.glassmc.loader.GlassLoader;
import com.github.glassmc.sculpt.Sculpt;
import com.github.glassmc.sculpt.framework.Renderer;
import com.github.glassmc.sculpt.framework.element.Container;
import com.github.glassmc.sculpt.framework.layout.RegionLayout;

import java.util.ArrayList;
import java.util.List;

public class Molten {

    private final Renderer renderer = new Renderer(GlassLoader.getInstance().getAPI(Sculpt.class).getBackend());
    private final Container container = new Container()
            .adjustElements(true);

    private List<Window> windows = new ArrayList<>();

    public void open(Window window) {
        this.windows.add(window);
        container.getLayout(RegionLayout.class).add(window.getContainer(), RegionLayout.Region.CENTER);
    }

    public void render() {
        for(Window window : windows) {
            window.update();
        }
        this.renderer.render(container);
    }

}
