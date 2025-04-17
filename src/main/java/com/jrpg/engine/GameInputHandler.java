package com.jrpg.engine;

import java.awt.event.*;
import java.util.List;
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
     * @return returns the current selected game action index, or null if there is
     *         currently no menu open
     */
    public Integer getCurrentGameActionIndex() {
        if (!engine.getGameState().isInActionMenu())
            return null;

        List<GameAction> gameActions = engine.getCurrentGameActions();
        int index = selectionIndexes[1];

        // so that the menu index appear in approximately the same location whenever the
        // menu is opened
        if (index >= gameActions.size())
            index = gameActions.size() - 1;
        if (index < 0)
            index = 0;
        selectionIndexes[1] = index;

        return selectionIndexes[1];
    }

    public GameAction getCurrentGameAction() {
        Integer index = getCurrentGameActionIndex();
        if (index == null)
            return null;
        return engine.getCurrentGameActions().get(index);
    }

    private void handleInput(KeyEvent e) {
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
                handleConfirm();
                break;
            case KeyEvent.VK_X:
                handleCancel();

        }
    }

    private void handleConfirm() {
        GameState gameState = engine.getGameState();
        if (gameState.isInDialogue()) {
            engine.finishDialogue();
        } else {
            if (gameState.isInActionMenu())
                engine.getCurrentGameActions().get(selectionIndexes[1]).getOnRun().accept(engine);
            gameState.setInActionMenu(!gameState.isInActionMenu());
        }
    }

    private void handleCancel() {
        engine.getGameState().setInActionMenu(false);
    }

    private void handleDirectionalInput(String direction) {
        GameState gameState = engine.getGameState();
        if (gameState.isInDialogue())
            return; // don't handle movement during dialogue
        if (gameState.isInActionMenu()) {
            List<GameAction> gameActions = engine.getCurrentGameActions();
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

    // works by moving to the closest gameObject in the direction given
    // objects are selected from a cone
    private void handleObjectSelectionChange(String direction) {
        ArrayList<GameObject> gameObjects = engine.getCurrentSceneGameObjects();
        GameObject currentGameObject = getCurrentGameObject();
        Vector2D currentPosition = currentGameObject.getPosition();

        // filter out gameObjects that don't pass the condition
        Predicate<Vector2D> filterCondition;
        switch (direction) {
            case "up":
                filterCondition = (Vector2D relativePosition) -> {
                    return relativePosition.getY() < 0
                            && Math.abs(relativePosition.getX()) <= -relativePosition.getY() * 1.5;
                };
                break;
            case "down":
                filterCondition = (Vector2D relativePosition) -> {
                    return relativePosition.getY() > 0
                            && Math.abs(relativePosition.getX()) <= relativePosition.getY() * 1.5;
                };
                break;
            case "left":
                filterCondition = (Vector2D relativePosition) -> {
                    return relativePosition.getX() < 0
                            && Math.abs(relativePosition.getY()) <= -relativePosition.getX() * 1.5;
                };
                break;
            case "right":
                filterCondition = (Vector2D relativePosition) -> {
                    return relativePosition.getX() > 0
                            && Math.abs(relativePosition.getY()) <= relativePosition.getX() * 1.5;
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
            Vector2D relativePosition = gameObject.getPosition().sub(currentPosition);

            if (!filterCondition.test(relativePosition))
                continue;
            double distance = relativePosition.getLength();
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