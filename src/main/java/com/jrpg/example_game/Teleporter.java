package com.jrpg.example_game;

import com.jrpg.engine.components.*;

public class Teleporter extends GameObject {
    public Teleporter(Vector2D position, Vector2D dimensions, String spriteName) {
        super(position, dimensions, spriteName);
    }

    public void addDestination(ExampleScene exampleScene) {
        addGameAction(new GameAction(exampleScene.getName(), (engine) -> {
            engine.changeScenes(exampleScene);
        }));
    }
}
