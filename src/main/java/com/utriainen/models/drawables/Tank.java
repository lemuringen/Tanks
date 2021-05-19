package com.utriainen.models.drawables;

import com.utriainen.models.room.Coordinates;
import com.utriainen.models.room.Vector;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.util.List;

public class Tank extends Entity {
    private double barrelLength;
    private double aimDirection;
    private double firePower = 3.0;
    private boolean isFiring = false;

    private double powerMeter = 0.0;
    private final double powerMeterMax = 100.0;
    private final double powerMeterFactor = 60.0;

    public Tank(int width, int height, Coordinates coordinates, boolean isAffectedByGravity, Color color) {
        super(width, height, isAffectedByGravity, coordinates, new Vector(0, 0), color);
        this.aimDirection = 0;
        this.barrelLength = 20;
    }

    public double getAimDirection() {
        return aimDirection;
    }

    public Coordinates getBarrelTipCoordinates() {
        double x = Math.cos(aimDirection) * barrelLength + getCoordinates().getX() + getWidth() / 2;
        double y = -Math.sin(aimDirection) * barrelLength + getCoordinates().getY();
        return new Coordinates(x, y);
    }

    public void setAimDirection(double aimDirection) {
        this.aimDirection = aimDirection;
    }


    public double actualFirePower() {
        return powerMeter * firePower;
    }

    public void resetPowerMeter() {
        powerMeter = 0.0;
    }

    public void increasePowerMeter(double seconds) {
        powerMeter += powerMeterFactor * seconds;
        if (powerMeter > powerMeterMax) powerMeter = powerMeterMax;
    }

    public Projectile fire() {
        setFiring(false);
        double power = actualFirePower();
        resetPowerMeter();
        return new Projectile(4, true,
                getBarrelTipCoordinates(),
                new Vector(Math.cos(getAimDirection()) * power, -Math.sin(getAimDirection()) * power), Color.BLACK);
    }

    public boolean isFiring() {
        return isFiring;
    }

    public void setFiring(boolean firing) {
        isFiring = firing;
    }

    @Override
    public void draw(GraphicsContext context) {
        context.setFill(getColor());
        context.setStroke(getColor());
        context.fillRect(getCoordinates().getX(), getCoordinates().getY(), getWidth(), getHeight());

        Coordinates barrelTipCoordinates = getBarrelTipCoordinates();
        context.setLineWidth(2);
        context.strokeLine(getCoordinates().getX() + getWidth() / 2,
                getCoordinates().getY(),
                barrelTipCoordinates.getX(),
                barrelTipCoordinates.getY());
    }


    @Override
    public boolean testGroundCollision(Ground ground) {
        return false;
    }
}
