package com.jrpg.engine;

import java.awt.Font;
import java.util.ArrayList;
import java.util.List;
import java.util.AbstractMap.SimpleEntry;

//TODO a more descriptive name
//this class represents 1 line of dialogue in the camera's dialogue box
//the line contains string-font pairs which are then draw horizontally in order by the camera
//ps currently the camera's dialogue box does not support wrap around so text needs to be shorted accordingly
public class DialogueLine {
    //idk if this is the best type for this
    //i just need a tuple and this is what i found
    private List<SimpleEntry<String, Font>> stringFontPairs = new ArrayList<SimpleEntry<String, Font>>();

    //for vertical spacing
    private int maxFontSize = Integer.MIN_VALUE;

    public List<SimpleEntry<String, Font>> getStringFontPairs() {
        return stringFontPairs;
    }

    public int getMaxFontSize() {
        return maxFontSize;
    }

    //for ease of use when making 1 font dialogues
    public DialogueLine(){}
    public DialogueLine(String string, Font font){
        this.add(string, font);
    }
    public DialogueLine(SimpleEntry<String, Font> stringFontPair){
        this.add(stringFontPair);
    }

    public void add(String string, Font font){
        this.add(new SimpleEntry<String, Font>(string, font));
    }

    public void add(SimpleEntry<String, Font> stringFontPair){
        int fontSize = stringFontPair.getValue().getSize();
        if(fontSize > maxFontSize) maxFontSize = fontSize;
        this.stringFontPairs.add(stringFontPair);
    }
}
