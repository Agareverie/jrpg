package com.jrpg.engine;

import java.awt.event.*;
import java.util.LinkedList;
import java.util.Queue;

import javax.swing.JFrame;

public class GameInputHandler {
    private Engine engine;
    private Queue<KeyEvent> unhandledEventsQueue;

    public Queue<KeyEvent> getUnhandledEventsQueue() {
        return unhandledEventsQueue;
    }

    public GameInputHandler(Engine engine, JFrame frame) {
        this.engine = engine;
        this.unhandledEventsQueue = new LinkedList<KeyEvent>();
        frame.addKeyListener(new GameInputListener(this));
    }

    private void handleInput(KeyEvent e) {
        // TODO
    }

    public void update() {
        while(unhandledEventsQueue.peek() != null){
            handleInput(unhandledEventsQueue.poll());
        }
    }
}

class GameInputListener implements KeyListener {
    GameInputHandler gameInputHandler;

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