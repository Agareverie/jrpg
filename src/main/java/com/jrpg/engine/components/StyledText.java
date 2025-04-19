package com.jrpg.engine.components;

import java.awt.Color;
import java.awt.Font;

public record StyledText(String text, Color color, Font font) {
    public int length() {
        return text.length();
    }
}
