package com.jrpg.rendering.graphics;

import java.awt.*;

import com.jrpg.rendering.Coordinate;

public record Text(String text, Coordinate position, Color color, Font font) implements Drawable {

    public Text(String text, Coordinate position, Color color, Font font, float fontSize) {
        this(text, position, color, font.deriveFont(fontSize));
    }

    public Text(String text, Coordinate position, Color color, Font font, int style) {
        this(text, position, color, font.deriveFont(style));
    }

    public Text(String text, Coordinate position, Color color, Font font, float fontSize, int style) {
        this(text, position, color, font.deriveFont(style, fontSize));
    }

    @Override
    public void draw(Graphics g) {
        g.setColor(color);
        g.setFont(font);
        g.drawString(text, position.getX(), position.getY());
    }
}