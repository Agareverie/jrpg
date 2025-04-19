package com.jrpg.example_game;

import java.util.ArrayList;
import java.util.List;

import com.jrpg.engine.Engine;

public class GameEventListenerManager {
    private List<GameEventListener<?>> gameEventListeners = new ArrayList<GameEventListener<?>>();

    public void registerEventListener(GameEventListener<?> gameEventListener) {
        this.gameEventListeners.add(gameEventListener);
    }

    public void notifyEvent(GameEvent event, Engine engine) {
        for (GameEventListener<?> gameEventListener : gameEventListeners) {
            gameEventListener.notifyEvent(event, engine);
        }
    }
}
