package com.jrpg.rendering.graphics;

import java.awt.*;

import com.jrpg.rendering.Coordinate;

public class Text implements GraphicsObject{
    private String text;
    private Font font;
    private Coordinate position;

    public Text(String text, Coordinate position, Font font, int fontSize){
        this.text = text;
        this.position = position;
        this.font = font.deriveFont((float) fontSize);
    }
    public Text(String text, Coordinate position, Font font){
        this.text = text;
        this.position = position;
        this.font = font;
    }

    @Override
    public void draw(Graphics g) {
        g.setFont(font);
        g.drawString(text, position.getX(), position.getY());
    }
}
