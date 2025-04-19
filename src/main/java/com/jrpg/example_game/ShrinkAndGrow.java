package com.jrpg.example_game;

import com.jrpg.engine.GameAnimation;
import com.jrpg.engine.GameObject;
import com.jrpg.engine.Vector2D;

//animation system demonstration
public class ShrinkAndGrow implements GameAnimation {

    private GameObject gameObject;
    private Vector2D originalSize;
    private double speed;
    private double factor;
    private boolean active = true;

    public void setActive(boolean active) {
        this.active = active;
    }

    public ShrinkAndGrow(GameObject gameObject){
        this(gameObject, 1, 0.1);
    }
    
    public ShrinkAndGrow(GameObject gameObject, double speed){
        this(gameObject, speed, 0.1);
    }

    public ShrinkAndGrow(GameObject gameObject, double speed, double factor){
        this.gameObject = gameObject;
        this.speed = speed;
        this.factor = 0.1;
        originalSize = gameObject.getDimensions();
    }

    @Override
    public void tick(double time) {
        gameObject.setDimensions(originalSize.scale(1 + (factor*Math.sin(time*speed))));
    }

    @Override
    public boolean isFinished() {
        return !active;
    }
}
