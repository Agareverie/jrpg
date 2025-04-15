package com.jrpg.rendering;

import java.util.function.IntUnaryOperator;

public class Coordinate {
    private int x;
    private int y;

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public void setX(IntUnaryOperator function) {
        this.x = function.applyAsInt(this.x);
    }

    public void setY(IntUnaryOperator function) {
        this.y = function.applyAsInt(this.y);
    }

    public Coordinate(int x, int y) {
        this.x = x;
        this.y = y;
    }
    
    public Coordinate add(int x, int y) {
        return new Coordinate(this.x + x, this.y + y);
    }

    public Coordinate add(Coordinate other) {
        return add(other.getX(), other.getY());
    }

    public Coordinate sub(int x, int y) {
        return new Coordinate(this.x - x, this.y - y);
    }

    public Coordinate sub(Coordinate other) {
        return sub(other.getX(), other.getY());
    }

    public Coordinate copy() {
        return new Coordinate(x, y);
    }
}
