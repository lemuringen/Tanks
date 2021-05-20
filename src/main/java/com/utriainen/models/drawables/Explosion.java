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
        if (getWidth() <= 0) isDone = true;
    }

    @Override
    public void draw(GraphicsContext context) {
        context.setFill(Color.YELLOW);
        context.fillOval(getCoordinates().getX() - (getWidth() * 0.5), getCoordinates().getY() - (getHeight() * 0.5), getWidth(), getHeight());

        context.setFill(getColor());
        context.fillOval(getCoordinates().getX() - (getWidth() * 0.6 * 0.5), getCoordinates().getY() - (getHeight()* 0.6 * 0.5), getWidth()*0.6, getHeight()*0.6);

    }

    @Override
    public Color getColor() {
        return this.color;
    }
}
