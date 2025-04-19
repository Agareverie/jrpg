package com.jrpg.example_game;

import java.lang.reflect.ParameterizedType;
import java.util.function.BiConsumer;

import com.jrpg.engine.Engine;

//might move this system into the engine
public class GameEventListener<T extends GameEvent> {
    private BiConsumer<T, Engine> callback;
    private Class<T> eventType;

    @SuppressWarnings("unchecked")
    public GameEventListener(BiConsumer<T, Engine> callback){
        this.eventType = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0]; 
        this.callback = callback;
    }

    @SuppressWarnings("unchecked")
    public void notifyEvent(GameEvent gameEvent, Engine engine){
        if(eventType.isInstance(gameEvent)) {
            callback.accept((T) gameEvent, engine);
        }
    }
}
