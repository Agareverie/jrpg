package com.jrpg.engine.components;

import java.util.ArrayList;

public class GameObject {
    private Vector2D position;
    private Vector2D dimensions;
    private String spriteName;
    private final ArrayList<GameAction> gameActions;
    private Dialogue description;
    private boolean selectable = true;

    public Vector2D getPosition() {
        return position;
    }

    public void setPosition(Vector2D position) {
        this.position = position;
    }

    public Vector2D getDimensions() {
        return dimensions;
    }

    public void setDimensions(Vector2D dimensions) {
        this.dimensions = dimensions;
    }

    public String getSpriteName() {
        return spriteName;
    }

    public void setSpriteName(String spriteName) {
        this.spriteName = spriteName;
    }

    public ArrayList<GameAction> getGameActions() {
        return gameActions;
    }

    public Dialogue getDescription() {
        return description;
    }

    public void setDescription(Dialogue description) {
        this.description = description;
    }

    public boolean isSelectable() {
        return selectable;
    }

    public void setSelectable(boolean selectable) {
        this.selectable = selectable;
    }

    public GameObject(Vector2D position, Vector2D dimensions, String spriteName) {
        this.position = position;
        this.dimensions = dimensions;
        this.spriteName = spriteName;
        this.gameActions = new ArrayList<>();
    }

    public void addGameAction(GameAction gameAction) {
        this.gameActions.add(gameAction);
    }
}