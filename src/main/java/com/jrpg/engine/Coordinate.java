package com.jrpg.engine;

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

    public Coordinate(int x, int y){
        this.x = x;
        this.y = y;
    }

    public Coordinate copy(){
        return new Coordinate(x, y);
    }
    
    public Coordinate add(int x, int y){
        return new Coordinate(this.x + x, this.y + y);
    }

    public Coordinate add(Coordinate other){
        return this.add(other.getX(), other.getY());
    }

    public Coordinate sub(int x, int y){
        return new Coordinate(this.x - x, this.y - y);
    }

    public Coordinate sub(Coordinate other){
        return this.sub(other.getX(), other.getY());
    }
}
