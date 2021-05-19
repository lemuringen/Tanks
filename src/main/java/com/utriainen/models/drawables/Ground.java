package com.utriainen.models.drawables;

import javafx.collections.ObservableList;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;

import java.util.ArrayList;
import java.util.List;

public class Ground implements Drawable {
    private Polygon ground;

    public Polygon getGround() {
        return ground;
    }

    public Ground() {
        this.ground = new Polygon(
                0, 880,
                1920, 880,
                1920, 1080,
                0, 1080);
    }

    @Override
    public void draw(GraphicsContext context) {
        context.setFill(getColor());
        //context.fillPolygon(new double[]{0, 1920, 1920, 0}, new double[]{880, 880, 1080, 1080}, 4);
        List<Double> points = ground.getPoints();
        double[] xCoordinates = new double[points.size() / 2];
        double[] yCoordinates = new double[points.size() / 2];

        for (int i = 0; i < points.size(); i++) {
            if (i % 2 == 0) {
                xCoordinates[i / 2 + i % 2] = points.get(i);
            } else {
                yCoordinates[(i / 2 + i % 2) - 1] = points.get(i);
            }
        }
        context.fillPolygon(xCoordinates, yCoordinates, xCoordinates.length);

    }

    public boolean detectCollision(Entity entity) {
        return ground.contains(entity.getCoordinates().getX(), entity.getCoordinates().getY());
    }

    @Override
    public Color getColor() {
        return Color.BEIGE;
    }
}
