package com.jrpg.engine.components;

import java.util.ArrayList;
import java.util.List;

public class Scene {
    private final List<GameObject> gameObjects = new ArrayList<GameObject>();
    private String backgroundImageSpriteName;

    public List<GameObject> getGameObjects() {
        return gameObjects;
    }

    public String getBackgroundImageSpriteName() {
        return backgroundImageSpriteName;
    }

    public void setBackgroundImageSpriteName(String backgroundImageSpriteName) {
        this.backgroundImageSpriteName = backgroundImageSpriteName;
    }

    public void add(GameObject gameObject) {
        this.gameObjects.add(gameObject);
    }


    public void remove(GameObject gameObject) {
        if (!this.gameObjects.contains(gameObject)) {
            return;
        }

        this.gameObjects.remove(gameObject);
    }

    public List<GameObject> getSelectableGameObjects(){
        List<GameObject> selectableGameObjects = new ArrayList<>();

        for(GameObject gameObject : gameObjects) {
            if(gameObject.isSelectable()) selectableGameObjects.add(gameObject);
        }

        return selectableGameObjects;
    }
}
