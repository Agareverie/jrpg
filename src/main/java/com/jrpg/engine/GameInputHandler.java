package com.jrpg.engine;

import java.awt.event.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.function.Predicate;

import javax.swing.JFrame;

public class GameInputHandler {
    private Engine engine;
    private Queue<KeyEvent> unhandledEventsQueue;

    // first one is objects in current scene
    // second one is actions in menus
    private int[] selectionIndexes = { 0, 0 };

    public Queue<KeyEvent> getUnhandledEventsQueue() {
        return unhandledEventsQueue;
    }

    public GameInputHandler(Engine engine, JFrame frame) {
        this.engine = engine;
        this.unhandledEventsQueue = new LinkedList<KeyEvent>();
        frame.addKeyListener(new GameKeyListener(this));
    }

    /**
     * 
     * @return returns the current selected gameObject index, or null if the current
     *         scene contains no gameObjects
     */
    public Integer getCurrentGameObjectIndex() {
        ArrayList<GameObject> gameObjects = engine.getCurrentSceneGameObjects();
        if (gameObjects.size() <= 0)
            return null;

        // for scene changes
        if (selectionIndexes[0] < 0 || selectionIndexes[0] > gameObjects.size())
            selectionIndexes[0] = 0;

        return selectionIndexes[0];
    }

    /**
     * 
     * @return returns the current selected gameObject, or null if the current scene
     *         contains no gameObjects
     */
    public GameObject getCurrentGameObject() {
        Integer index = getCurrentGameObjectIndex();
        if (index == null)
            return null;
        return engine.getCurrentSceneGameObjects().get(index);
    }

    /**
     * 
     * @return returns the current selected menu index, or null if there is
     *         currently no menu open
     */
    public Integer getCurrentMenuIndex() {
        if (!engine.getGameState().isInMenu())
            return null;
        return selectionIndexes[1];
    }

    private void handleInput(KeyEvent e) {
        GameState gameState = engine.getGameState();
        switch (e.getKeyCode()) {
            case KeyEvent.VK_LEFT:
                handleDirectionalInput("left");
                break;
            case KeyEvent.VK_RIGHT:
                handleDirectionalInput("right");
                break;
            case KeyEvent.VK_UP:
                handleDirectionalInput("up");
                break;
            case KeyEvent.VK_DOWN:
                handleDirectionalInput("down");
                break;
            case KeyEvent.VK_Z:
                if (gameState.isInMenu())
                    getCurrentGameObject().getGameActions().get(selectionIndexes[1]).getOnRun().accept(engine);
                gameState.setInMenu(!gameState.isInMenu());
                break;
            case KeyEvent.VK_X:
                gameState.setInMenu(false);

        }
    }

    private void handleDirectionalInput(String direction) {
        if (engine.getGameState().isInMenu()) {
            ArrayList<GameAction> gameActions = getCurrentGameObject().getGameActions();
            switch (direction) {
                case "up":
                    this.selectionIndexes[1]--;
                    if (this.selectionIndexes[1] < 0)
                        this.selectionIndexes[1] = gameActions.size() - 1;
                    break;
                case "down":
                    this.selectionIndexes[1]++;
                    if (this.selectionIndexes[1] >= gameActions.size())
                        this.selectionIndexes[1] = 0;
                    break;
            }
        } else
            handleObjectSelectionChange(direction);
    }

    // TODO maybe not use a string as an input and infer the direction directly from
    // a keyEvent or something
    // works by moving to the closest gameObject in the direction given
    private void handleObjectSelectionChange(String direction) {
        ArrayList<GameObject> gameObjects = engine.getCurrentSceneGameObjects();
        GameObject currentGameObject = getCurrentGameObject();
        Vector2D currentPosition = currentGameObject.getPosition();

        // filter out objects in the wrong direction
        Predicate<Vector2D> filterCondition;
        switch (direction) {
            case "up":
                filterCondition = (Vector2D position) -> {
                    return position.getY() < currentPosition.getY();
                };
                break;
            case "down":
                filterCondition = (Vector2D position) -> {
                    return position.getY() > currentPosition.getY();
                };

                break;
            case "left":
                filterCondition = (Vector2D position) -> {
                    return position.getX() < currentPosition.getX();
                };
                break;
            case "right":
                filterCondition = (Vector2D position) -> {
                    return position.getX() > currentPosition.getX();
                };
                break;
            default:
                return;
        }

        Integer minIndex = null;
        double minDistance = Double.POSITIVE_INFINITY;
        for (int i = 0; i < gameObjects.size(); i++) {
            GameObject gameObject = gameObjects.get(i);
            if (gameObject == currentGameObject)
                continue;
            Vector2D position = gameObject.getPosition();

            if (!filterCondition.test(position))
                continue;
            double distance = Vector2D.getDistance(position, currentPosition);
            if (distance < minDistance) {
                minIndex = i;
                minDistance = distance;
            }
        }

        // do nothing if no target was found
        if (minIndex == null)
            return;

        selectionIndexes[0] = minIndex;
    }

    public void update() {
        while (unhandledEventsQueue.peek() != null) {
            handleInput(unhandledEventsQueue.poll());
        }
    }
}