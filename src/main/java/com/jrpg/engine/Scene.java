package com.jrpg.engine;

import java.util.ArrayList;
import java.util.List;

public class Scene {
    private final List<GameObject> gameObjects;

    public List<GameObject> getGameObjects() {
        return gameObjects;
    }

    public Scene() {
        this.gameObjects = new ArrayList<>();
    }

    public void add(GameObject gameObject) {
        this.gameObjects.add(gameObject);
    }
}
