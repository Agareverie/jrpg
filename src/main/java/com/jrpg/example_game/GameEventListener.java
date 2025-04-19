package com.jrpg.example_game;

import java.util.function.BiConsumer;

import com.jrpg.engine.Engine;

//might move this system into the engine
public class GameEventListener<T extends GameEvent> {
    private BiConsumer<T, Engine> callback;
    private Class<T> eventType;

    public GameEventListener(Class<T> eventType, BiConsumer<T, Engine> callback){
        this.eventType = eventType;
        this.callback = callback;
    }

    public void notifyEvent(GameEvent gameEvent, Engine engine){
        if(eventType.isInstance(gameEvent)) {
            callback.accept((T) gameEvent, engine);
        }
    }
}
