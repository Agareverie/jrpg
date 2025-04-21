package com.jrpg.engine.components;

import java.awt.Color;
import java.awt.Font;
import java.util.ArrayList;
import java.util.List;

public class Dialogue {
    private static final Color defaultColor = Color.BLACK;
    private static final Font defaultFont = new Font("Serif", Font.PLAIN, 20);
    private final List<DialogueLine> lines = new ArrayList<>();

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

    // For generating a dialogue with identical color and font for all the text fragments
    public static Dialogue fromString(String string, Color color, Font font) {
        Dialogue dialogue = new Dialogue();

        for (String text : string.split("\\n")) {
            DialogueLine dialogueLine = new DialogueLine(text, color, font);
            dialogue.addLine(dialogueLine);
        }

        return dialogue;
    }

    public static Dialogue fromString(String string, Color color) {
        return fromString(string, color, defaultFont);
    }

    public static Dialogue fromString(String string) {
        return fromString(string, defaultColor);
    }

    public static Dialogue empty() {
        return fromString("");
    }


    public void addLine(DialogueLine line) {
        this.lines.add(line);
        this.length += line.getLength();
    }
}
