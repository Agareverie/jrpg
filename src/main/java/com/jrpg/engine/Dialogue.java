package com.jrpg.engine;

import java.awt.Color;
import java.awt.Font;
import java.util.ArrayList;
import java.util.List;

public class Dialogue {
    static Color defaultColor = Color.black;
    static Font defaultFont = new Font("Serif", Font.PLAIN, 20);
    private List<DialogueLine> lines = new ArrayList<DialogueLine>();
    private int length;

    public static Font getDefaultFont() {
        return defaultFont;
    }

    public static Color getDefaultColor() {
        return defaultColor;
    }
    
    public List<DialogueLine> getLines() {
        return lines;
    }

    public int getLength() {
        return length;
    }

    //for generating a dialogue with identical color and font for all the text fragments
    public static Dialogue fromString(String string, Color color, Font font){
        Dialogue dialogue = new Dialogue();

        for(String text : string.split("\\n")){
            DialogueLine dialogueLine = new DialogueLine(text, color, font);
            dialogue.addLine(dialogueLine);
        }

        return dialogue;
    }

    public static Dialogue fromString(String string, Color color){
        return fromString(string, color, defaultFont);
    }

    public static Dialogue fromString(String string){
        return fromString(string, defaultColor, defaultFont);
    }


    public void addLine(DialogueLine line){
        this.lines.add(line);
        this.length += line.getLength();
    }
}
