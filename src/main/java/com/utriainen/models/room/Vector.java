package com.utriainen.models.room;

public class Vector {
    public double getVelocityX() {
        return velocityX;
    }

    public void setVelocityX(double velocityX) {
        this.velocityX = velocityX;
    }

    public double getVelocityY() {
        return velocityY;
    }

    public void setVelocityY(double velocityY) {
        this.velocityY = velocityY;
    }
    public Vector clone(){
        return new Vector(velocityX, velocityY);
    }
    public void copy(Vector copyFrom){
        setVelocityX(copyFrom.getVelocityX());
        setVelocityY(copyFrom.getVelocityY());
    }
    private double velocityX;
    private double velocityY;

    public Vector(double velocityX, double velocityY){
        this.velocityX = velocityX;
        this.velocityY = velocityY;
    }

}
