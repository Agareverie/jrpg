package com.jrpg.rendering;

import com.jrpg.rendering.graphics.Drawable;

import javax.swing.*;
import java.awt.*;
import java.util.concurrent.CopyOnWriteArrayList;



public class GraphicsRenderer {
    private JFrame frame;
    private CopyOnWriteArrayList<Drawable> drawables = new CopyOnWriteArrayList<Drawable>();
    public GraphicsRenderer(JFrame frame, Dimension dimensions) {
        this.frame = frame;
        frame.setResizable(false);

        GraphicsPanel graphicsPanel = new GraphicsPanel(drawables);
        graphicsPanel.setPreferredSize(dimensions);
        frame.getContentPane().add(graphicsPanel);

        frame.pack();
        frame.setVisible(true);
    }

    public void add(Drawable drawable){
        drawables.add(drawable);
    }

    public void clear() {
        this.drawables.clear();
    }

    public void render() {
        frame.repaint();
    }
}