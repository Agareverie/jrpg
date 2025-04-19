package com.jrpg.example_game;

import com.jrpg.engine.components.*;
import com.jrpg.engine.*;

public class ExampleScene extends Scene {
    private final String name;

    public String getName() {
        return name;
    }

    public ExampleScene(String name){
        super();
        this.name = name;
    }

    public ExampleScene(String name, String backgroundSpriteName) {
        this(name);
        setBackgroundImageSpriteName(backgroundSpriteName);
    }

    public void notifyEventToGameObjects(GameEvent gameEvent, Engine engine) {
        getGameObjects().stream()
                .filter(gameObject -> gameObject instanceof ExampleGameObject)
                .map(gameObject -> (ExampleGameObject) gameObject)
                .forEach(gameObject -> {
                    gameObject.getGameEventListenerManager().notifyEvent(gameEvent, engine);
                });
    }
}
