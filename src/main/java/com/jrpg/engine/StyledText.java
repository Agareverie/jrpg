package com.jrpg.engine;

import java.awt.Color;
import java.awt.Font;

//idk what to name this class
//id much rather use a 3-tuple but java doesn't seem to support those 
public class StyledText {
    private String text;
    private Color color;
    private Font font;

    public String getText() {
        return text;
    }
    public Color getColor() {
        return color;
    }
    public Font getFont() {
        return font;
    }
    public StyledText(String text, Color color, Font font){
        this.text = text;
        this.font = font;
        this.color = color;
    }

    public int getLength(){
        return text.length();
    }
}
