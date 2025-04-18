package com.jrpg.engine;

import java.util.ArrayList;
import java.util.List;

public class Scene {
    private List<GameObject> gameObjects = new ArrayList<GameObject>();
    private List<GameObject> selectableGameObjects = new ArrayList<GameObject>();

    public List<GameObject> getGameObjects() {
        return gameObjects;
    }

    public List<GameObject> getSelectableGameObjects(){
        return selectableGameObjects;
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
