package com.jrpg.rendering.graphics;
import java.awt.*;

import com.jrpg.rendering.Coordinate;

public record Rectangle(Coordinate position, Coordinate dimensions, Color color) implements Drawable {

    @Override
    public void draw(Graphics g) {
        g.setColor(color);
        g.fillRect(position.getX(), position.getY(), dimensions.getX(), dimensions.getY());
    }
}
