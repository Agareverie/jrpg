package com.jrpg.engine;

public class GameObject {

    //fields for rendering
    private Vector2D position;
    private Vector2D dimensions;
    private String spriteName;

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

    public GameObject(Vector2D position, Vector2D dimensions, String spriteName){
        this.position = position;
        this.dimensions = dimensions;
        this.spriteName = spriteName;
    }
}