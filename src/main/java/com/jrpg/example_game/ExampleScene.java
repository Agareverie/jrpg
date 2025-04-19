package com.jrpg.example_game;
import com.jrpg.engine.*;

public class ExampleScene extends Scene {
    private String name;

    public String getName() {
        return name;
    }

    public ExampleScene(String name){
        super();
        this.name = name;
    }

    public ExampleScene(String name, String backgroundSpriteName){
        this(name);
        setBackgroundImageSpriteName(backgroundSpriteName);
    }

    public void notifyEventToGameObjects(GameEvent gameEvent, Engine engine){
        for (GameObject gameObject : getGameObjects()) {
            if(!(gameObject instanceof ExampleGameObject)) continue;
            ExampleGameObject exampleGameObject = (ExampleGameObject) gameObject;
            exampleGameObject.getGameEventListenerManager().notifyEvent(gameEvent, engine);
        }
    }
}
