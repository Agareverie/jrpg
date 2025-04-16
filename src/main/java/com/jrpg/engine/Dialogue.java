package com.jrpg.engine;

import java.awt.Color;
import java.awt.Font;
import java.util.ArrayList;
import java.util.List;

public class Dialogue {
    static Color defaultColor = Color.black;
    static Font defaultFont = new Font("Serif", Font.PLAIN, 20);
    private List<DialogueLine> lines = new ArrayList<DialogueLine>();

    public List<DialogueLine> getLines() {
        return lines;
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

    public void addLine(DialogueLine line){
        this.lines.add(line);
    }
}
