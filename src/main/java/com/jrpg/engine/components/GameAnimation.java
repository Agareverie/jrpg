package com.jrpg.engine.components;

public interface GameAnimation {
    boolean isFinished();
    void tick(double time);
}
