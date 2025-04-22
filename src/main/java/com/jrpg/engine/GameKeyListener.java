package com.jrpg.engine;

import java.awt.event.*;

class GameKeyListener implements KeyListener {
    GameInputHandler gameInputHandler;

    public GameKeyListener(GameInputHandler gameInputHandler) {
        this.gameInputHandler = gameInputHandler;
    }

    @Override
    public void keyPressed(KeyEvent e) {
        gameInputHandler.getUnhandledEventsQueue().add(e);
    }

    @Override
    public void keyReleased(KeyEvent e) {}

    @Override
    public void keyTyped(KeyEvent e) {}
}
