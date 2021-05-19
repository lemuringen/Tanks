package com.utriainen.models.drawables;

import com.utriainen.TankApp;
import com.utriainen.models.room.Coordinates;
import com.utriainen.models.room.Vector;
import javafx.scene.paint.Color;

import java.util.List;

public abstract class Entity implements Drawable {
    //public abstract void testCollisions(List<Entity> colliders);
    public abstract boolean testGroundCollision(Ground ground);

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

    public boolean isAffectedByGravity() {
        return isAffectedByGravity;
    }

    public void setAffectedByGravity(boolean affectedByGravity) {
        isAffectedByGravity = affectedByGravity;
    }

    public Coordinates getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(Coordinates coordinates) {
        this.coordinates = coordinates;
    }

    public Vector getVector() {
        return vector;
    }

    public void setVector(Vector vector) {
        this.vector = vector;
    }

    @Override
    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    private boolean isAffectedByGravity;
    private Coordinates coordinates;
   // private Coordinates oldCoordinates;
    private Vector vector;
    //private Vector oldVector;
    private double width;
    private double height;
    private Color color;

    public Entity(double width, double height, boolean isAffectedByGravity, Coordinates coordinates, Vector vector, Color color) {
        this.isAffectedByGravity = isAffectedByGravity;
        this.coordinates = coordinates;
      //  this.oldCoordinates = this.coordinates.clone();
        this.vector = vector;
      //  this.oldVector = vector.clone();
        this.width = width;
        this.height = height;
        this.color = color;
    }

    public void updatePosition(double timeDelta) {
      //  oldVector.copy(vector);
       // oldCoordinates.copy(coordinates);
        //apply acceleration from gravity to vector - velocityY
        if (isAffectedByGravity()) getVector().setVelocityY(getVector().getVelocityY() + TankApp.GRAVITY_CONSTANT);
        double xDelta = vector.getVelocityX() * timeDelta;
        double yDelta = vector.getVelocityY() * timeDelta;
        getCoordinates().setX(getCoordinates().getX() + xDelta);
        getCoordinates().setY(getCoordinates().getY() + yDelta);
    }
}
