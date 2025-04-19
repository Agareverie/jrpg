package com.jrpg.engine;

import com.jrpg.rendering.Coordinate;

public class Vector2D {
    private double x;
    private double y;

    public double getX() {
        return x;
    }
    public double getY() {
        return y;
    }
    public void setX(double x) {
        this.x = x;
    }
    public void setY(double y) {
        this.y = y;
    }

    public static Vector2D zero(){
        return new Vector2D(0, 0);
    }

    public static double getDistance(Vector2D v0, Vector2D v1){
        return v0.sub(v1).getLength();
    }

    public Vector2D(double x, double y){
        this.x = x;
        this.y = y;
    }

    public Vector2D add(double x, double y){
        return new Vector2D(this.x + x, this.y + y);
    }

    public Vector2D add(Vector2D other){
        return this.add(other.getX(), other.getY());
    }

    public Vector2D sub(double x, double y){
        return new Vector2D(this.x - x, this.y - y);
    }

    public Vector2D sub(Vector2D other){
        return this.sub(other.getX(), other.getY());
    }

    public Vector2D scale(double x, double y){
        return new Vector2D(this.x * x, this.y * y);
    }

    public Vector2D scale(double c){
        return this.scale(c,c);
    }

    public Vector2D scale(Vector2D scalingVector){
        return this.scale(scalingVector.getX(), scalingVector.getY());
    }

    public double getLength(){
        return Math.sqrt(x*x + y*y);
    }

    public Vector2D getNormalized(){
        return this.scale(1/this.getLength());
    }

    /**
     * toCoordinate
     * @return Coordinate instance with x and y rounded from the Vector's x and y
     */
    public Coordinate toCoordinate(){
        return new Coordinate((int) Math.round(x), (int) Math.round(y));
    }

    public String toString(){
        return x + " " + y;
    }
}

