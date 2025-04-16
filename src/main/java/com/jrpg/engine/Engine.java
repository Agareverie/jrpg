package com.jrpg.engine;

import java.util.List;
import java.util.ArrayList;

import javax.swing.JFrame;

public class Engine {
    private Scene currentScene;
    private Camera camera;
    private GameInputHandler gameInputHandler;
    private ArrayList<Scene> scenes;
    private GameState gameState = new GameState();

    public Scene getCurrentScene() {
        return currentScene;
    }

    public Camera getCamera() {
        return camera;
    }

    public GameState getGameState(){
        return this.gameState;
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

    public GameObject getCurrentSelectedGameObject(){
        return gameInputHandler.getCurrentGameObject();
    }

    //TODO add generic actions
    public List<GameAction> getCurrentActions(){
        return getCurrentSelectedGameObject().getGameActions();
    }

    public void update(){
        camera.update();
        gameInputHandler.update();
    }
}
