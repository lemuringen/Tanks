package com.utriainen.models.room;

import javafx.scene.shape.Circle;

import java.util.ArrayList;
import java.util.List;

public class Line {
    private Coordinates p0;
    private Coordinates p1;
    private final double slope;
    private final double yInterception;


    public Line(Coordinates p0, Coordinates p1) {
        this.p0 = p0;
        this.p1 = p1;
        slope = (p1.getY() - p0.getY()) / (p1.getX() - p0.getX());
        yInterception = p0.getY() - slope * p0.getX();
    }

    public Coordinates getP0() {
        return p0;
    }

    public void setP0(Coordinates p0) {
        this.p0 = p0;
    }

    public Coordinates getP1() {
        return p1;
    }

    public void setP1(Coordinates p1) {
        this.p1 = p1;
    }

    public double getSlope() {
        return slope;
    }

    public double getYInterception() {
        return yInterception;
    }

    //todo only return intersection located between points
    public List<Coordinates> findIntersection(Circle circle) {
        double k = getSlope();
        double m = getYInterception();
        double C = circle.getCenterX() * circle.getCenterX()
                + circle.getCenterY() * circle.getCenterY()
                - circle.getRadius() * circle.getRadius();
        double a = 1 + k * k;
        double b = 2 * -circle.getCenterX() + 2 * -circle.getCenterY() * k + 2 * k * m;
        double c = 2 * -circle.getCenterY() * m + C + m * m;
        double p = b / a;
        double q = c / a;
        return solveSecondGradeForX(p, q, m, k);
    }
    //todo only return intersection located between points
    public Coordinates findIntersection(Line line) {
        double k = getSlope();
        double m = getYInterception();
        double b = line.getSlope();
        double d = getYInterception();
        double x = (d-m)/(k-b);
        double y = k * x + m;
        return new Coordinates(x,y);
    }

    public static List<Coordinates> solveSecondGradeForX(double p, double q, double m, double k) {
        ArrayList<Coordinates> intersections = new ArrayList<>();
        double sqrt = Math.sqrt(p * p / 4 - q);
        double x0 = -p / 2 - sqrt;
        intersections.add(new Coordinates(x0, k * x0 + m));
        double x1 = -p / 2 + sqrt;
        intersections.add(new Coordinates(x1, k * x1 + m));
        return intersections;
    }
}
