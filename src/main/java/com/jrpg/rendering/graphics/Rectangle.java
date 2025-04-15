package com.jrpg.rendering.graphics;
import java.awt.*;

import com.jrpg.rendering.Coordinate;
public class Rectangle implements GraphicsObject {

    private Coordinate position;
    private Coordinate dimensions;
    private Color color;

    public Rectangle(Coordinate position, Coordinate dimensions, Color color){
        this.position = position;
        this.dimensions = dimensions;
        this.color = color;
    }

    @Override
    public void draw(Graphics g) {
        g.setColor(color);
        g.fillRect(position.getX(), position.getY(), dimensions.getX(), dimensions.getY());
    }
}
