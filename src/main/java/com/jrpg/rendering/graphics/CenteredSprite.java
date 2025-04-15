package com.jrpg.rendering.graphics;

import java.awt.*;

import com.jrpg.rendering.Coordinate;

public class CenteredSprite implements GraphicsObject{
    private Coordinate position;
    private Coordinate dimensions;
    private Image image;
    
    public CenteredSprite(Coordinate position, Coordinate dimensions, Image image){
        this.dimensions = dimensions;
        this.image = image;

        this.position = new Coordinate(position.getX() - dimensions.getX()/2, position.getY() - dimensions.getY()/2);
    }

    public CenteredSprite(Coordinate position, Image image){
        this.dimensions = new Coordinate(image.getWidth(null), image.getHeight(null));
        this.image = image;
        
        this.position = new Coordinate(position.getX() - dimensions.getX()/2, position.getY() - dimensions.getY()/2);
    }

    @Override
    public void draw(Graphics g) {
        g.drawImage(image, position.getX(), position.getY(), dimensions.getX(), dimensions.getY(), null);
    }   
}
