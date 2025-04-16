package com.jrpg.rendering;

import com.jrpg.rendering.graphics.Drawable;

import javax.swing.*;
import java.awt.*;
import java.util.concurrent.CopyOnWriteArrayList;

class GraphicsPanel extends JPanel {
    private CopyOnWriteArrayList<Drawable> drawables;

    public GraphicsPanel(CopyOnWriteArrayList<Drawable> drawables) {
        super();
        this.drawables = drawables;
    }

    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        drawables.forEach((drawable) -> {
            drawable.draw(g);
        });
    }
}
