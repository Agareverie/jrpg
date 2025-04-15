package com.jrpg.engine;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

class GameInputListener implements KeyListener {
    private final GameInputHandler gameInputHandler;

    public GameInputListener(GameInputHandler gameInputHandler) {
        this.gameInputHandler = gameInputHandler;
    }

    @Override
    public void keyPressed(KeyEvent e) {
        gameInputHandler.getUnhandledEventsQueue().add(e);
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }

    @Override
    public void keyTyped(KeyEvent e) {

    }
}