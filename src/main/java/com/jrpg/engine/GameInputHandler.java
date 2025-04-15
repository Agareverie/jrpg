package com.jrpg.engine;

import java.awt.event.*;
import java.util.ArrayList;

import javax.swing.JFrame;

public class GameInputHandler {
    private Engine engine;
    private ArrayList<KeyEvent> unhandledEvents;

    public ArrayList<KeyEvent> getUnhandledEvents() {
        return unhandledEvents;
    }

    public GameInputHandler(Engine engine, JFrame frame) {
        this.engine = engine;
        this.unhandledEvents = new ArrayList<KeyEvent>();
        frame.addKeyListener(new GameInputListener(this));
    }

    private void handleInput(KeyEvent e) {
        // TODO
    }

    public void update() {
        unhandledEvents.forEach((KeyEvent e) -> {
            handleInput(e);
        });

        unhandledEvents.clear();
    }
}

class GameInputListener implements KeyListener {
    GameInputHandler gameInputHandler;

    public GameInputListener(GameInputHandler gameInputHandler) {
        this.gameInputHandler = gameInputHandler;
    }

    @Override
    public void keyPressed(KeyEvent e) {
        gameInputHandler.getUnhandledEvents().add(e);

        System.out.println(e.getKeyChar() + " was pressed");
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }

    @Override
    public void keyTyped(KeyEvent e) {

    }
}