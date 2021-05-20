package com.utriainen.models.room;

import javafx.scene.shape.Polygon;

import java.util.List;

public class PolygonPerimeterPoint {
    private Coordinates preVertex;
    private Coordinates perimeterPoint;
    private Coordinates nextVertex;

    public PolygonPerimeterPoint(Coordinates preVertex, Coordinates perimeterPoint, Coordinates nextVertex) {
        this.preVertex = preVertex;
        this.perimeterPoint = perimeterPoint;
        this.nextVertex = nextVertex;
    }
    /*
    public static PolygonPerimeterPoint getPolygonPerimeterPoint(Coordinates perimeterPoint, Polygon polygon){
        List<Double> points = polygon.getPoints();
        Coordinates preVertex;
        Coordinates nextVertex;
        double x0;
        double y0;
        double x1;
        double y1;
        boolean forwards;
        boolean upwards;
        for(int i = 0; i < points.size()-2; i+=2){
            x0 = points.get(i);
            y0 = points.get(i+1);
            x1 = points.get(i+2);
            y1 = points.get(i+3);

            forwards = x1 > x0;
            upwards = y1 < y0;

            if(forwards){
                if(perimeterPoint.getX() > x0 && perimeterPoint.getX() < x1){

                }
            }

        }
        return null;
    }*/
    public Coordinates getPreVertex() {
        return preVertex;
    }

    public void setPreVertex(Coordinates preVertex) {
        this.preVertex = preVertex;
    }

    public Coordinates getPerimeterPoint() {
        return perimeterPoint;
    }

    public void setPerimeterPoint(Coordinates perimeterPoint) {
        this.perimeterPoint = perimeterPoint;
    }

    public Coordinates getNextVertex() {
        return nextVertex;
    }

    public void setNextVertex(Coordinates nextVertex) {
        this.nextVertex = nextVertex;
    }
}
