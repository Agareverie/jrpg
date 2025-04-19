package com.jrpg.example_game;

import java.util.function.BiConsumer;

import com.jrpg.engine.Engine;

//might move this system into the engine
public class GameEventListener {
    private String eventName;
    private BiConsumer<GameEvent, Engine> callback;

    public GameEventListener(String eventName, BiConsumer<GameEvent, Engine> callback){
        this.eventName = eventName;
        this.callback = callback;
    }

    public void notifyEvent(GameEvent gameEvent, Engine engine){
        if(eventName.equals(gameEvent.getEventName())) callback.accept(gameEvent, engine);
    }
}
