package com.jrpg.example_game;

import java.lang.reflect.ParameterizedType;

import com.jrpg.engine.Engine;

//might move this system into the engine
public abstract class GameEventListener<T extends GameEvent> {
    @SuppressWarnings("unchecked")
    private Class<T> eventType = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];

    protected abstract void run(T gameEvent, Engine engine);

    @SuppressWarnings("unchecked")
    public void notifyEvent(GameEvent gameEvent, Engine engine) {
        if (eventType.isInstance(gameEvent)) {
            run((T) gameEvent, engine);
        }
    }
}
