package com.jrpg.engine;

import com.jrpg.engine.components.Dialogue;
import com.jrpg.engine.components.GameAction;
import com.jrpg.engine.components.GameObject;
import com.jrpg.engine.components.Scene;
import com.jrpg.engine.settings.AcceptKeyMaps;
import com.jrpg.engine.settings.DirectionalKeyMaps;

import java.util.List;
import java.util.Queue;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.stream.Stream;

import javax.swing.JFrame;

public class Engine {
    private Scene currentScene;
    private Dialogue currentDialogue;

    private final Camera camera;
    private final GameInputHandler gameInputHandler;
    private final List<GameAction> generalGameActions = new ArrayList<>();
    private final Queue<Dialogue> dialogueQueue = new LinkedList<>();
    private final GameState gameState = new GameState(this);

    public Scene getCurrentScene() {
        return currentScene;
    }

    public Camera getCamera() {
        return camera;
    }

    public GameState getGameState() {
        return this.gameState;
    }

    public GameInputHandler getGameInputHandler() {
        return gameInputHandler;
    }

    public Dialogue getCurrentDialogue() {
        return currentDialogue;
    }

    public Engine(JFrame frame, Scene initialScene) {
        camera = new Camera(this, frame);
        gameInputHandler = new GameInputHandler(this, frame);
        changeScenes(initialScene);
    }

    public List<GameObject> getCurrentSceneGameObjects() {
        return currentScene.getGameObjects();
    }

    public List<GameObject> getCurrentSceneSelectableGameObjects() {
        return currentScene.getSelectableGameObjects();
    }

    public GameObject getCurrentSelectedGameObject() {
        return gameInputHandler.getCurrentGameObject();
    }

    public void setKeyMaps(DirectionalKeyMaps directionalKeyMaps) {
        gameInputHandler.setKeyMaps(directionalKeyMaps);
    }

    public void setKeyMaps(AcceptKeyMaps acceptKeyMaps) {
        gameInputHandler.setKeyMaps(acceptKeyMaps);
    }

    public void setKeyMaps(DirectionalKeyMaps directionalKeyMaps, AcceptKeyMaps acceptKeyMaps) {
        gameInputHandler.setKeyMaps(directionalKeyMaps, acceptKeyMaps);
    }

    public DirectionalKeyMaps getCurrentDirectionalKeyMaps(){
        return gameInputHandler.getDirectionalKeyMaps();
    }

    public AcceptKeyMaps getCurrentAcceptKeyMaps(){
        return gameInputHandler.getAcceptKeyMaps();
    }

    public List<GameAction> getCurrentGameActions() {
        GameObject currentGameObject = getCurrentSelectedGameObject();
        List<GameAction> gameActions = new ArrayList<GameAction>();
        if (currentGameObject == null)
            return gameActions;

        Stream.concat(generalGameActions.stream(), currentGameObject.getGameActions().stream()).forEach(gameAction -> {
            if (gameAction.getCondition().test(currentGameObject)) {
                gameActions.add(gameAction);
            }
        });

        return gameActions;
    }

    public GameAction getCurrentSelectedGameAction() {
        return gameInputHandler.getCurrentGameAction();
    }

    public void addGeneralGameAction(GameAction gameAction) {
        generalGameActions.add(gameAction);
    }

    public void removeGeneralGameAction(GameAction gameAction) {
        generalGameActions.remove(gameAction);
    }

    public void enqueueDialogue(Dialogue dialogue) {
        dialogueQueue.add(dialogue);

        if (currentDialogue == null) {
            toNextDialogue();
        }
    }

    public void toNextDialogue() {
        if (camera.isAnimatedDialogueFinished()) {
            currentDialogue = dialogueQueue.poll();
            camera.setAnimatedDialogue(currentDialogue);
        } else {
            camera.skipAnimatedDialogue();
        }
    }

    public void changeScenes(Scene scene) {
        currentScene = scene;
        camera.clearAnimations();
    }

    /**
     * resets the engine
     * and sets the currentScene to the given scene
     * @param scene
     */
    public void reset(Scene initialScene) {
        dialogueQueue.clear();
        generalGameActions.clear();
        gameInputHandler.reset();
        changeScenes(initialScene);
    }

    public void update() {
        camera.update();
        gameInputHandler.update();
    }

    public void loop() {
        while (true) {
            update();
            try {
                Thread.sleep(Math.round(1000.0f / 60.0f));
            } catch (InterruptedException ignored) {
            }
        }
    }
}
