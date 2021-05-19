package com.utriainen.models.drawables;

import com.utriainen.models.room.Coordinates;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class Explosion implements Drawable {
    private Coordinates coordinates;
    private double width;
    private double height;
    private Color color;
    private boolean isDone;

    public boolean isDone() {
        return isDone;
    }

    public Coordinates getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(Coordinates coordinates) {
        this.coordinates = coordinates;
    }

    public double getWidth() {
        return width;
    }

    public void setWidth(double width) {
        this.width = width;
    }

    public double getHeight() {
        return height;
    }

    public void setHeight(double height) {
        this.height = height;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public Explosion(Coordinates coordinates, double width, double height, Color color) {
        this.coordinates = coordinates;
        this.width = width;
        this.height = height;
        this.color = color;
        this.isDone = false;
    }

    public void explode() {
        setWidth(getWidth() - 0.5);
        setHeight(getHeight() - 0.5);
        getCoordinates().setRelativeX(0.25);
        getCoordinates().setRelativeY(0.25);
        if (getWidth() <= 0) isDone = true;
    }

    @Override
    public void draw(GraphicsContext context) {
        context.setFill(Color.YELLOW);
        context.fillOval(getCoordinates().getX() - (getWidth() * 0.5), getCoordinates().getY() - (getWidth() * 0.5), getWidth() * 2, getHeight() * 2);

        context.setFill(getColor());
        context.fillOval(getCoordinates().getX(), getCoordinates().getY(), getWidth(), getHeight());

    }

    @Override
    public Color getColor() {
        return this.color;
    }
}
