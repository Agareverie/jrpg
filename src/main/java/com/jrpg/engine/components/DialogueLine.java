package com.jrpg.engine.components;

import java.awt.Color;
import java.awt.Font;
import java.util.ArrayList;
import java.util.List;

// P.S. Currently the camera's dialogue box does not support wrap around so text needs to be shortened accordingly
public class DialogueLine {

    // List of all the "text fragments" that compose the dialogue line
    private final List<StyledText> textFragments = new ArrayList<>();

    // For vertical spacing
    private int maxFontSize = Integer.MIN_VALUE;
    private int length = 0;

    public List<StyledText> getTextFragments() {
        return textFragments;
    }

    public int getMaxFontSize() {
        return maxFontSize;
    }

    public int getLength() {
        return length;
    }

    public DialogueLine() {}

    // For ease of use when making 1 font dialogues
    public DialogueLine(String string, Color color, Font font) {
        this.add(string, color, font);
    }

    public DialogueLine(StyledText textFragment) {
        this.add(textFragment);
    }

    public void add(String string, Color color, Font font) {
        this.add(new StyledText(string, color, font));
    }

    public void add(StyledText textFragment) {
        int fontSize = textFragment.font().getSize();

        if(fontSize > maxFontSize) {
            maxFontSize = fontSize;
        }

        this.length += textFragment.text().length();
        this.textFragments.add(textFragment);
    }
}
