package com.jrpg.engine;

//TODO? make this into an enum and have the engine track it directly
//since it doesn't matter if the menu is open or not when the game is paused and etc.
public class GameState {
    private Engine engine;
    private boolean inActionMenu = false;
    private boolean paused = false;

    public boolean isInActionMenu() {
        return inActionMenu;
    }
    public boolean isInDialogue() {
        return engine.getCurrentDialogue() != null;
    }
    public boolean isPaused() {
        return paused;
    }
    public void setInActionMenu(boolean inActionMenu) {
        this.inActionMenu = inActionMenu;
    }
    public void setPaused(boolean paused) {
        this.paused = paused;
    }

    public GameState(Engine engine){
        this.engine = engine;
    }
}
