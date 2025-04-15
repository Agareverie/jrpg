package com.jrpg.renderer;

import java.util.concurrent.CopyOnWriteArrayList;

import javax.swing.*;

import com.jrpg.renderer.graphics.GraphicsObject;

import java.awt.*;

public class Renderer {
    private JFrame frame;
    private CopyOnWriteArrayList<GraphicsObject> graphicsObjects;
    private Dimension dimensions = new Dimension(1200, 675);
    public Renderer(JFrame frame) {
        this.frame = frame;

        //TODO not hardcode this maybe??? idk ¯\_(ツ)_/¯
        frame.setResizable(false);

        this.graphicsObjects = new CopyOnWriteArrayList<GraphicsObject>();
        GraphicsPanel graphicsPanel = new GraphicsPanel(graphicsObjects);
        graphicsPanel.setPreferredSize(dimensions);
        frame.getContentPane().add(graphicsPanel);

        frame.pack();
        frame.setVisible(true);
    }

    public void add(GraphicsObject graphicsObject){
        graphicsObjects.add(graphicsObject);
    }

    public void clear() {
        this.graphicsObjects.clear();
    }

    public void render() {
        frame.repaint();
    }
}

//i can't think of a good name for this
class GraphicsPanel extends JPanel {
    private CopyOnWriteArrayList<GraphicsObject> graphicsObjects;

    public GraphicsPanel(CopyOnWriteArrayList<GraphicsObject> graphicsObjects) {
        super();
        this.graphicsObjects = graphicsObjects;
    }

    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        graphicsObjects.forEach((graphicsObject) -> {
            graphicsObject.draw(g);
        });
    }
}