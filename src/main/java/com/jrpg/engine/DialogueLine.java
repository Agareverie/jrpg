package com.jrpg.engine;

import java.awt.Color;
import java.awt.Font;
import java.util.ArrayList;
import java.util.List;

//ps currently the camera's dialogue box does not support wrap around so text needs to be shorted accordingly
public class DialogueLine {

    //list of all the "text fragments" that compose the dialogue line
    private List<StyledText> textFragments = new ArrayList<StyledText>();

    //for vertical spacing
    private int maxFontSize = Integer.MIN_VALUE;

    public List<StyledText> getTextFragments() {
        return textFragments;
    }

    public int getMaxFontSize() {
        return maxFontSize;
    }

    public DialogueLine(){}
    //for ease of use when making 1 font dialogues
    public DialogueLine(String string, Color color, Font font){
        this.add(string, color, font);
    }
    public DialogueLine(StyledText textFragment){
        this.add(textFragment);
    }

    public void add(String string, Color color, Font font){
        this.add(new StyledText(string, color, font));
    }

    public void add(StyledText textFragment){
        int fontSize = textFragment.getFont().getSize();
        if(fontSize > maxFontSize) maxFontSize = fontSize;
        this.textFragments.add(textFragment);
    }
}
