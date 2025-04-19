package com.jrpg.example_game;

import java.util.function.BiConsumer;

import com.jrpg.engine.Engine;

//might move this system into the engine
public class GameEventListener<T extends GameEvent> {
    private BiConsumer<T, Engine> callback;

    public GameEventListener(BiConsumer<T, Engine> callback){
        this.callback = callback;
    }

    public void notifyEvent(T gameEvent, Engine engine){
        if(gameEvent instanceof T) callback.accept(gameEvent, engine);
    }
}
