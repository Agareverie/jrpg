package com.jrpg.engine;

import java.util.List;

import javax.swing.JFrame;

public class Engine {
    private Scene currentScene;
    private final Camera camera;
    private final GameInputHandler gameInputHandler;
    private final List<Scene> scenes;

    public Engine(JFrame frame, List<Scene> scenes) {
        this.scenes = scenes;
        this.currentScene = scenes.getFirst();

        camera = new Camera(this, frame);
        gameInputHandler = new GameInputHandler(this, frame);
    }

    public void update() {
        camera.update();
        gameInputHandler.update();
    }

    public Camera getCamera() {
        return camera;
    }

    public GameInputHandler getGameInputHandler() {
        return gameInputHandler;
    }

    public List<GameObject> getCurrentSceneGameObjects(){
        return currentScene.getGameObjects();
    }

    public Scene getCurrentScene() {
        return currentScene;
    }

    public void setCurrentScene(int index) {
        this.currentScene = scenes.get(index);
    }
}
