package com.jrpg.engine;

public class GameState {
    private boolean inMenu = false;
    private boolean inDialogue = false;
    private boolean paused = false;

    public boolean isInMenu() {
        return inMenu;
    }
    public boolean isInDialogue() {
        return inDialogue;
    }
    public boolean isPaused() {
        return paused;
    }
    public void setInMenu(boolean inMenu) {
        this.inMenu = inMenu;
    }
    public void setInDialogue(boolean inDialogue) {
        this.inDialogue = inDialogue;
    }
    public void setPaused(boolean paused) {
        this.paused = paused;
    }
}
