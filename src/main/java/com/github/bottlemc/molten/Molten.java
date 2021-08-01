package com.github.bottlemc.molten;

import com.github.bottlemc.flame.Flame;
import com.github.glassmc.loader.GlassLoader;
import com.github.glassmc.sculpt.Sculpt;
import com.github.glassmc.sculpt.framework.Renderer;
import com.github.glassmc.sculpt.framework.constraint.Absolute;
import com.github.glassmc.sculpt.framework.constraint.Copy;
import com.github.glassmc.sculpt.framework.constraint.Relative;
import com.github.glassmc.sculpt.framework.element.Container;
import com.github.glassmc.sculpt.framework.layout.ListLayout;
import com.github.glassmc.sculpt.framework.layout.RegionLayout;

import java.util.*;
import java.util.function.Supplier;

public class Molten {

    private final Flame flame = GlassLoader.getInstance().getAPI(Flame.class);

    private final Renderer renderer = new Renderer(GlassLoader.getInstance().getAPI(Sculpt.class).getBackend());
    private final Container dockList;
    private final Container container = new Container()
            .adjustElements(false)
            .getLayout(RegionLayout.class)
            .add(new Container()
                .backgroundColor(new Absolute(flame.getConfiguration().elementBackground))
                .x(new Relative(-0.5, 17.5, false))
                .width(new Absolute(25))
                .height(new Relative(1, -10, false))
                .cornerRadius(new Absolute(2.5))
                .getLayout(RegionLayout.class)
                .add(dockList = new Container()
                    .layout(new ListLayout(ListLayout.Type.VERTICAL)),
                    RegionLayout.Region.CENTER)
                .getContainer(),
                RegionLayout.Region.LEFT)
            .getContainer();

    private final Map<Window, Container> windows = new HashMap<>();

    private Window interactedWindow;
    private final List<InteractType> currentInteracts = new ArrayList<>();

    private final List<Container> pinnedItems = new ArrayList<>();

    private Container getItem(String title, String icon) {
        return new Container()
            .width(new Relative(0.7))
            .height(new Copy())
            .padding(new Relative(0.15))
            .getLayout(RegionLayout.class)
            .add(new Container()
                .width(new Relative(0.7))
                .height(new Relative(0.7))
                .apply(container1 -> {
                    container1.backgroundColor(new Absolute(flame.getConfiguration().foregroundPrimary));
                    if(icon != null) {
                        container1.backgroundImage(icon);
                    }
                }),
                RegionLayout.Region.CENTER)
            .getContainer();
    }

    public void addPinnedItem(String name, String icon, Supplier<Window> generator) {
        dockList.add(this.getItem(name, icon)
            .onClick(container1 -> {
                this.open(generator.get());
            }));
    }

    public void open(Window window) {
        container.getLayout(RegionLayout.class).add(window.getContainer(), RegionLayout.Region.CENTER);

        Container container = this.getItem("", window.getIcon())
            .adjustElements(false)
            .onClick(container1 -> {
                this.setFocused(window);
            })
            .getLayout(RegionLayout.class)
            .add(new Container()
                .backgroundColor(new Absolute(flame.getConfiguration().foregroundPrimary))
                .width(new Absolute(0.75))
                .height(new Relative(0.4)),
                RegionLayout.Region.RIGHT)
            .getContainer();

        dockList.getLayout(ListLayout.class).add(container);

        this.windows.put(window, container);
    }

    public void close(Window window) {
        Container dockItem = this.windows.remove(window);
        container.remove(window.getContainer());
        dockList.remove(dockItem);
    }

    public void render() {
        for(Window window : new HashSet<>(windows.keySet())) {
            window.update();
        }
        this.renderer.render(container);
    }

    public void setInteractedWindow(Window interactedWindow) {
        this.interactedWindow = interactedWindow;
        if (interactedWindow != null) {
            Container dockItem = this.windows.remove(interactedWindow);
            this.windows.put(interactedWindow, dockItem);
            this.setFocused(interactedWindow);
        }
    }

    public void setFocused(Window window) {
        this.container.remove(window.getContainer());
        this.container.add(window.getContainer());
    }

    public Window getInteractedWindow() {
        return interactedWindow;
    }

    public List<InteractType> getCurrentInteracts() {
        return currentInteracts;
    }

    public enum InteractType {
        MOVE, RESIZE_LEFT, RESIZE_TOP, RESIZE_RIGHT, RESIZE_BOTTOM, MAXIMIZE, CLOSE
    }

}
