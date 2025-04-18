package com.jrpg.engine;

import java.util.ArrayList;
import java.util.List;

public class Scene {
    private List<GameObject> gameObjects = new ArrayList<GameObject>();
    private List<GameObject> selectableGameObjects = new ArrayList<GameObject>();
    private String backgroundImageSpriteName;

    public List<GameObject> getGameObjects() {
        return gameObjects;
    }

    public List<GameObject> getSelectableGameObjects(){
        return selectableGameObjects;
    }

    public String getBackgroundImageSpriteName() {
        return backgroundImageSpriteName;
    }

    public void setBackgroundImageSpriteName(String backgroundImageSpriteName) {
        this.backgroundImageSpriteName = backgroundImageSpriteName;
    }

    public void add(GameObject gameObject){
        this.gameObjects.add(gameObject);
        if(gameObject.isSelectable()) this.selectableGameObjects.add(gameObject);
    }

    public void remove(GameObject gameObject){
        if(!this.gameObjects.contains(gameObject)) return;

        this.gameObjects.remove(gameObject);
        if(gameObject.isSelectable()) this.selectableGameObjects.remove(gameObject);
    }
}
