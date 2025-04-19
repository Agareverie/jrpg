package com.jrpg.example_game;

import com.jrpg.engine.components.*;

// Animation system demonstration
public class ShrinkAndGrow implements GameAnimation {
    private final GameObject gameObject;
    private final Vector2D originalSize;
    private final double speed;
    private final double factor;
    private boolean active = true;

    public void setActive(boolean active) {
        this.active = active;
    }

    public ShrinkAndGrow(GameObject gameObject) {
        this(gameObject, 1);
    }
    
    public ShrinkAndGrow(GameObject gameObject, double speed) {
        this(gameObject, speed, 0.1);
    }

    public ShrinkAndGrow(GameObject gameObject, double speed, double factor) {
    public ShrinkAndGrow(GameObject gameObject, double speed, double factor) {
        this.gameObject = gameObject;
        this.speed = speed;
        this.factor = factor;
        originalSize = gameObject.getDimensions();
    }

    @Override
    public void tick(double time) {
        gameObject.setDimensions(originalSize.scale(1 + (factor * Math.sin(speed * time))));
    }

    @Override
    public boolean isFinished() {
        return !active;
    }
}
