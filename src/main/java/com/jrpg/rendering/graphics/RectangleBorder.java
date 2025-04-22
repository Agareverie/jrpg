package com.jrpg.rendering.graphics;

import java.awt.*;

import com.jrpg.rendering.Coordinate;


public class RectangleBorder extends Rectangle {
    private final int strokeWidth;

    public RectangleBorder(Coordinate position, Coordinate dimensions, int strokeWidth, Color color) {
        super(position, dimensions, color);
        this.strokeWidth = strokeWidth;
    }

    @Override
    public void draw(Graphics g) {
        g.setColor(color);
        if (g instanceof Graphics2D) ((Graphics2D)g).setStroke(new BasicStroke(strokeWidth));
        g.drawRect(position.getX(), position.getY(), dimensions.getX(), dimensions.getY());
    }
}
