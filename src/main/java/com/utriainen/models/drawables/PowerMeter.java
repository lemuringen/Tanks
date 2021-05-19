package com.utriainen.models.drawables;

import com.utriainen.models.room.Coordinates;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class PowerMeter implements Drawable {
    private Coordinates start;
    private Coordinates top;
    private int width;
    private double power;
    private Color color;

    public double getPower() {
        return power;
    }

    public void setPower(double power) {
        this.power = power;
    }

    public PowerMeter(Coordinates tankCoordinates, int width, double power, Color color) {
        this.start = tankCoordinates.getRelativeCoordinates(-40, 0);
        this.top = this.start.clone();
        this.width = width;
        this.power = power;
        this.color = color;
    }

    @Override
    public void draw(GraphicsContext context) {
        context.setFill(getColor());
        context.setStroke(getColor());
        context.setLineWidth(width);
        context.strokeLine(start.getX(), start.getY(), top.getX(), top.getY());
    }

    @Override
    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public void update(Coordinates tankCoordinates, double power) {
        start.setX(tankCoordinates.getX());
        start.setY(tankCoordinates.getY() - 40);
        top.setX(start.getX());
        top.setY(start.getY()-power);

    }
}
