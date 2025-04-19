package com.jrpg.engine;

// TODO? make this into an enum and have the engine track it directly
// since it doesn't matter if the menu is open or not when the game is paused and etc.
public class GameState {
    private final Engine engine;

    private boolean inActionMenu = false;

    public boolean isInActionMenu() {
        return inActionMenu;
    }
    public boolean isInDialogue() {
        return engine.getCurrentDialogue() != null;
    }

    public void setInActionMenu(boolean inActionMenu) {
        this.inActionMenu = inActionMenu;
    }

    public GameState(Engine engine) {
        this.engine = engine;
    }
}
