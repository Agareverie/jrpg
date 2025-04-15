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

    public Engine(JFrame frame, ArrayList<Scene> scenes){
        this.scenes = scenes;
        this.currentScene = scenes.get(0);

        camera = new Camera(this, frame);
        gameInputHandler = new GameInputHandler(this, frame);
        //for testing
    }

    public void update(){
        camera.update();
        gameInputHandler.update();
    }
}
