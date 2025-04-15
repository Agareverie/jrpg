package com.jrpg.rendering.graphics;

import java.awt.*;

import com.jrpg.rendering.Coordinate;

public record Text(String text, Coordinate position, Font font, Color color) implements Drawable {

    public Text(String text, Coordinate position, Font font, int fontSize, Color color) {
        this(text, position, font.deriveFont((float) fontSize), color);
    }

    @Override
    public void draw(Graphics g) {
        g.setColor(color);
        g.setFont(font);
        g.drawString(text, position.getX(), position.getY());
    }
}
