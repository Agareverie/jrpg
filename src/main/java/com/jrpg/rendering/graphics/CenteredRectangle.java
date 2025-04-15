package com.jrpg.rendering.graphics;
import java.awt.*;

import com.jrpg.rendering.Coordinate;
public class CenteredRectangle implements GraphicsObject {

    private Coordinate position;
    private Coordinate dimensions;
    private Color color;

    public CenteredRectangle(Coordinate position, Coordinate dimensions, Color color){
        this.dimensions = dimensions;
        this.color = color;

        this.position = new Coordinate(position.getX() - dimensions.getX()/2, position.getY() - dimensions.getY()/2);
    }

    @Override
    public void draw(Graphics g) {
        g.setColor(color);
        g.fillRect(position.getX(), position.getY(), dimensions.getX(), dimensions.getY());
    }
}
