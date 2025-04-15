package com.jrpg.engine;

import java.awt.event.*;
import java.util.*;
import java.util.function.Predicate;

import javax.swing.JFrame;

public class GameInputHandler {
    private final Engine engine;
    private final Queue<KeyEvent> unhandledEventsQueue;
    private final int[] selectionIndexes = { 0, 0 };
    private boolean inMenu = false;

    public Queue<KeyEvent> getUnhandledEventsQueue() {
        return unhandledEventsQueue;
    }

    public GameInputHandler(Engine engine, JFrame frame) {
        this.engine = engine;
        this.unhandledEventsQueue = new LinkedList<>();

        frame.addKeyListener(new GameInputListener(this));
    }

    private OptionalInt getSelectionIndex(KeyEvent e) {
        List<GameObject> gameObjects = engine.getCurrentSceneGameObjects();
        Optional<GameObject> currentGameObject = getCurrentGameObject();

        if (currentGameObject.isEmpty()) {
            return OptionalInt.empty();
        }

        Vector2D currentPosition = currentGameObject.get().getPosition();
        var condition = filterCondition(e, currentPosition);

        int minIndex = -1;
        double minDistance = Double.POSITIVE_INFINITY;
        for (int i = 0; i < gameObjects.size(); i++) {
            GameObject gameObject = gameObjects.get(i);
            Vector2D position = gameObject.getPosition();

            if (gameObject == currentGameObject.get() || !condition.test(position)) {
                continue;
            }

            double distance = Vector2D.getDistance(position, currentPosition);
            if (distance < minDistance) {
                minIndex = i;
                minDistance = distance;
            }
        }

        if (minIndex == -1) {
            return OptionalInt.empty();
        }

        return OptionalInt.of(minIndex);
    }

    private void handleInput(KeyEvent e) {
        OptionalInt selectionIndex = getSelectionIndex(e);

        if (selectionIndex.isEmpty()) {
            return;
        }

        selectionIndexes[0] = selectionIndex.getAsInt();
    }

    /**
     *
     * @return returns the current selected gameObject index, or null if the current
     *         scene contains no gameObjects
     */
    private OptionalInt getCurrentGameObjectIndex() {
        List<GameObject> gameObjects = engine.getCurrentSceneGameObjects();
        if (gameObjects.isEmpty())
            return OptionalInt.empty();

        if (selectionIndexes[0] < 0 || selectionIndexes[0] > gameObjects.size())
            selectionIndexes[0] = 0;

        return OptionalInt.of(selectionIndexes[0]);
    }

    /**
     *
     * @return returns the current selected gameObject, or null if the current scene
     *         contains no gameObjects
     */
    public Optional<GameObject> getCurrentGameObject() {
        OptionalInt index = getCurrentGameObjectIndex();
        if (index.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(engine.getCurrentSceneGameObjects().get(index.getAsInt()));
    }

    /**
     *
     * @return returns the current selected menu index, or null if there is
     *         currently no menu open
     */
    public OptionalInt getCurrentMenuIndex() {
        if (!inMenu) {
            return OptionalInt.empty();
        }
        return OptionalInt.of(selectionIndexes[1]);
    }

    private Predicate<Vector2D> filterCondition(KeyEvent e, Vector2D currentPosition) {
        return switch (e.getKeyCode()) {
            case KeyEvent.VK_UP -> (position) -> position.getY() < currentPosition.getY();
            case KeyEvent.VK_DOWN -> (position) -> position.getY() > currentPosition.getY();
            case KeyEvent.VK_LEFT -> (position) -> position.getX() < currentPosition.getX();
            case KeyEvent.VK_RIGHT -> (position) -> position.getX() > currentPosition.getX();
            default -> (position) -> false;
        };
    }

    public void update() {
        while(unhandledEventsQueue.peek() != null){
            handleInput(unhandledEventsQueue.poll());
        }
    }

    public boolean isInMenu() {
        return inMenu;
    }
}