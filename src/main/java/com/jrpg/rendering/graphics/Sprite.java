package com.jrpg.rendering.graphics;

import java.awt.*;

import com.jrpg.rendering.Coordinate;

public class Sprite implements GraphicsObject{
    private Coordinate position;
    private Coordinate dimensions;
    private Image image;
    
    public Sprite(Coordinate position, Coordinate dimensions, Image image){
        this.position = position;
        this.dimensions = dimensions;
        this.image = image;
    }

    public Sprite(Coordinate position, Image image){
        this.position = position;
        this.image = image;

        this.dimensions = new Coordinate(image.getWidth(null), image.getHeight(null));
    }

    @Override
    public void draw(Graphics g) {
        g.drawImage(image, position.getX(), position.getY(), dimensions.getX(), dimensions.getY(), null);
    }   
}
