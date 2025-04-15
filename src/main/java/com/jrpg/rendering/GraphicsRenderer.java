package com.jrpg.rendering;

import com.jrpg.dimensions.Dimensions;
import com.jrpg.rendering.graphics.Drawable;

import javax.swing.*;
import java.awt.*;
import java.util.concurrent.CopyOnWriteArrayList;

public class GraphicsRenderer {
    private final JFrame frame;
    private final CopyOnWriteArrayList<Drawable> drawables = new CopyOnWriteArrayList<>();
    private static final Dimension dimensions = new Dimension(Dimensions.WIDTH, Dimensions.HEIGHT);

    public GraphicsRenderer(JFrame frame) {
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