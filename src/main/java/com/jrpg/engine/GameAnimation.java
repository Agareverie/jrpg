package com.jrpg.engine;

public interface GameAnimation {
    boolean isFinished();
    void tick(double time);
}
