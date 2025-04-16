package com.jrpg.engine;

import java.util.List;
import java.util.Queue;
import java.util.ArrayList;
import java.util.LinkedList;

import javax.swing.JFrame;

public class Engine {
    private Scene currentScene;
    private Camera camera;
    private GameInputHandler gameInputHandler;
    private ArrayList<Scene> scenes;
    private Queue<Dialogue> dialogueQueue = new LinkedList<Dialogue>();
    private Dialogue currentDialogue;
    private GameState gameState = new GameState(this);

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

    public Dialogue getCurrentDialogue() {
        return currentDialogue;
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

    public GameAction getCurrentSelectedAction(){
        return gameInputHandler.getCurrentGameAction();
    }

    public void enqueueDialogue(Dialogue dialogue){
        dialogueQueue.add(dialogue);

        if(currentDialogue == null) currentDialogue = dialogueQueue.poll();
    }

    //i need a better name for this
    //this function is supposed to be called when the player has acknowledged the current dialogue
    //and the next one in the queue needs to be displayed
    public void finishDialogue(){
        currentDialogue = dialogueQueue.poll();
    }

    public void update(){
        camera.update();
        gameInputHandler.update();
    }
}
