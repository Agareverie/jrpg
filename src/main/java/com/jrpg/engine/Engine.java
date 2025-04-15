package com.jrpg.engine;

import java.util.ArrayList;

import javax.swing.JFrame;

public class Engine {
    private Scene currentScene;
    private Camera camera;
    private GameInputHandler gameInputHandler;
    private ArrayList<Scene> scenes;

    public Scene getCurrentScene() {
        return currentScene;
    }

    public Camera getCamera() {
        return camera;
    }

    public GameInputHandler getGameInputHandler() {
        return gameInputHandler;
    }

    public Engine(JFrame frame, ArrayList<Scene> scenes){
        this.scenes = scenes;
        this.currentScene = scenes.get(0);

        camera = new Camera(this, frame);
        gameInputHandler = new GameInputHandler(this, frame);
    }

    public ArrayList<GameObject> getCurrentSceneGameObjects(){
        return currentScene.getGameObjects();
    }

    public void update(){
        camera.update();
        gameInputHandler.update();
    }
}
