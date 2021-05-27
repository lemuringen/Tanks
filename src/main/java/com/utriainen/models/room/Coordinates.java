package com.utriainen.models.room;

public class Coordinates {

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public Coordinates getRelativeCoordinates(double deltaY, double deltaX) {
        return new Coordinates(getX() + deltaX, getY() + deltaY);
    }
    public void setRelativeY(double y){
        setY(getY()+y);
    }
    public void setRelativeX(double x){
        setX(getX()+x);
    }
    public void copy(Coordinates copyFrom){
        setX(copyFrom.getX());
        setY(copyFrom.getY());
        }
    public Coordinates clone() {
        return new Coordinates(getX(), getY());
    }

    private double x;
    private double y;

    public Coordinates(double x, double y) {
        this.x = x;
        this.y = y;
    }
public boolean equals(Object o){
        if(!(o instanceof Coordinates)) return false;
        Coordinates coordinates = (Coordinates) o;
        if(coordinates.getY() == getY() && coordinates.getX()==getX())return true;
        return false;
}
}
