package com.jrpg.engine;

import com.jrpg.engine.components.GameAction;
import com.jrpg.engine.components.GameObject;
import com.jrpg.engine.components.Vector2D;
import com.jrpg.engine.settings.AcceptKeyMaps;
import com.jrpg.engine.settings.DirectionalKeyMaps;

import java.awt.event.*;
import java.util.List;
import java.util.LinkedList;
import java.util.Queue;
import java.util.function.Predicate;

import javax.swing.JFrame;

public class GameInputHandler {
    private final Engine engine;
    private final Queue<KeyEvent> unhandledEventsQueue;
    private DirectionalKeyMaps directionalKeyMaps = DirectionalKeyMaps.ARROW_KEYS;
    private AcceptKeyMaps acceptKeyMaps = AcceptKeyMaps.ZX;

    // first one is objects in current scene
    // second one is actions in menus
    private final int[] selectionIndexes = { 0, 0 };

    public Queue<KeyEvent> getUnhandledEventsQueue() {
        return unhandledEventsQueue;
    }

    public DirectionalKeyMaps getDirectionalKeyMaps() {
        return directionalKeyMaps;
    }

    public AcceptKeyMaps getAcceptKeyMaps() {
        return acceptKeyMaps;
    }

    public GameInputHandler(Engine engine, JFrame frame) {
        this.engine = engine;
        this.unhandledEventsQueue = new LinkedList<>();
        frame.addKeyListener(new GameKeyListener(this));
    }

    public void setKeyMaps(DirectionalKeyMaps directionalKeyMaps) {
        this.directionalKeyMaps = directionalKeyMaps;
    }

    public void setKeyMaps(AcceptKeyMaps acceptKeyMaps) {
        this.acceptKeyMaps = acceptKeyMaps;
    }

    public void setKeyMaps(DirectionalKeyMaps directionalKeyMaps, AcceptKeyMaps acceptKeyMaps) {
        this.directionalKeyMaps = directionalKeyMaps;
        this.acceptKeyMaps = acceptKeyMaps;
    }

    /**
     * 
     * @return returns the current selected gameObject index, or null if the current
     *         scene contains no gameObjects
     */
    public Integer getCurrentGameObjectIndex() {
        List<GameObject> gameObjects = engine.getCurrentSceneSelectableGameObjects();
        if (gameObjects.isEmpty()) {
            return null;
        }

        // for scene changes
        if (selectionIndexes[0] < 0 || selectionIndexes[0] >= gameObjects.size())
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
        return engine.getCurrentSceneSelectableGameObjects().get(index);
    }

    /**
     * 
     * @return returns the current selected game action index, or null if there is
     *         currently no menu open
     */
    public Integer getCurrentGameActionIndex() {
        if (!engine.getGameState().isInActionMenu()) {
            return null;
        }

        List<GameAction> gameActions = engine.getCurrentGameActions();
        int index = selectionIndexes[1];

        if (gameActions.isEmpty()) {
            return null;
        }

        // So that the menu index appear in approximately the same location whenever the
        // menu is opened
        if (index >= gameActions.size()) {
            index = gameActions.size() - 1;
        }

        if (index < 0) {
            index = 0;
        }

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
        int keyCode = e.getKeyCode();

        if (keyCode == directionalKeyMaps.left()) {
            handleDirectionalInput(Direction.LEFT);

        } else if (keyCode == directionalKeyMaps.right()) {
            handleDirectionalInput(Direction.RIGHT);

        } else if (keyCode == directionalKeyMaps.up()) {
            handleDirectionalInput(Direction.UP);

        } else if (keyCode == directionalKeyMaps.down()) {
            handleDirectionalInput(Direction.DOWN);

        } else if (keyCode == acceptKeyMaps.confirm()) {
            handleConfirm();

        } else if (keyCode == acceptKeyMaps.cancel()) {
            handleCancel();
        }
    }

    private void handleConfirm() {
        GameState gameState = engine.getGameState();
        if (gameState.isInDialogue()) {
            engine.toNextDialogue();
            return;
        }

        if (gameState.isInActionMenu()) {
            List<GameAction> gameActions = engine.getCurrentGameActions();
            if (gameActions.isEmpty()) {
                return;
            }
            GameAction gameAction = gameActions.get(selectionIndexes[1]);
            gameAction.getOnRun().accept(engine);
            if(gameAction.closesMenu()) gameState.setInActionMenu(false);
            return;
        }

        if (engine.getCurrentGameActions().isEmpty()) {
            return;
        }

        gameState.setInActionMenu(true);
    }

    private void handleCancel() {
        engine.getGameState().setInActionMenu(false);
    }

    private void handleDirectionalInput(Direction direction) {
        GameState gameState = engine.getGameState();
        if (gameState.isInDialogue())
            return; // don't handle movement during dialogue
        if (gameState.isInActionMenu()) {
            List<GameAction> gameActions = engine.getCurrentGameActions();
            switch (direction) {
                case UP:
                    this.selectionIndexes[1]--;
                    if (this.selectionIndexes[1] < 0)
                        this.selectionIndexes[1] = gameActions.size() - 1;
                    break;
                case DOWN:
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
    private void handleObjectSelectionChange(Direction direction) {
        List<GameObject> gameObjects = engine.getCurrentSceneSelectableGameObjects();
        if (gameObjects.isEmpty()) {
            return;
        }
        GameObject currentGameObject = getCurrentGameObject();
        Vector2D currentPosition = currentGameObject.getPosition();

        // filter out gameObjects that don't pass the condition
        Predicate<Vector2D> filterCondition;
        switch (direction) {
            case UP:
                filterCondition = (Vector2D relativePosition) -> {
                    return relativePosition.getY() < 0 && Math.abs(relativePosition.getX()) <= -relativePosition.getY() * 1.5;
                };
                break;
            case DOWN:
                filterCondition = (Vector2D relativePosition) -> {
                    return relativePosition.getY() > 0
                            && Math.abs(relativePosition.getX()) <= relativePosition.getY() * 1.5;
                };
                break;
            case LEFT:
                filterCondition = (Vector2D relativePosition) -> {
                    return relativePosition.getX() < 0
                            && Math.abs(relativePosition.getY()) <= -relativePosition.getX() * 1.5;
                };
                break;
            case RIGHT:
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

            if (!filterCondition.test(relativePosition)) {
                continue;
            }
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

    public void reset(){
        this.unhandledEventsQueue.clear();
    }

    public void update() {
        while (unhandledEventsQueue.peek() != null) {
            handleInput(unhandledEventsQueue.poll());
        }
    }
}