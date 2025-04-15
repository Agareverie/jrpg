package com.jrpg.engine;

import java.util.ArrayList;

public class Scene {
    private ArrayList<GameObject> gameObjects;

    public ArrayList<GameObject> getGameObjects() {
        return gameObjects;
    }

    public Scene(){
        this.gameObjects = new ArrayList<GameObject>();
    }

    public void add(GameObject gameObject){
        this.gameObjects.add(gameObject);
    }
}
