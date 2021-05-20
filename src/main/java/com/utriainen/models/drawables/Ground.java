package com.utriainen.models.drawables;

import com.utriainen.models.room.Coordinates;
import com.utriainen.models.room.SortByX;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Polygon;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class Ground implements Drawable {
    private Polygon ground;
    private List<Coordinates> vertices;

    public Polygon getGround() {
        return ground;
    }

    public Ground() {
        this.ground = new Polygon(
                0, 880,
                1920, 880,
                1920, 1080,
                0, 1080);
        this.vertices = new ArrayList<>();
        this.vertices.add(new Coordinates(0, 880));
        this.vertices.add(new Coordinates(1920, 880));
        this.vertices.add(new Coordinates(1920, 1080));
        this.vertices.add(new Coordinates(0, 1080));
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
                xCoordinates[i / 2] = points.get(i);
            } else {
                yCoordinates[i / 2] = points.get(i);
            }
        }
        context.fillPolygon(xCoordinates, yCoordinates, xCoordinates.length);

    }
    public void updateGround(){
        double[] polygonVertices = new double[vertices.size()*2];
        int counter = 0;
        for(Coordinates vertex : vertices){
            polygonVertices[counter]=vertex.getX();
            polygonVertices[counter+1]=vertex.getY();
            counter += 2;
        }
        ground = new Polygon(polygonVertices);
    }
    public void deformGround(Coordinates center, double radius) {
        List<Coordinates> newPolygon = new ArrayList<>();
        newPolygon.addAll(vertices.subList(0, vertices.size()-3));
        Circle circle = new Circle(center.getX(),center.getY(),radius);
        newPolygon = newPolygon.stream().filter(coordinates -> !circle.contains(coordinates.getX(),coordinates.getY())).collect(Collectors.toList());
        newPolygon.addAll(getNewVertices(center, radius));
        Collections.sort(newPolygon, new SortByX());
        newPolygon.addAll(vertices.subList(vertices.size()-3, vertices.size()));
        vertices = newPolygon;


    }

    public List<Coordinates> getNewVertices(Coordinates center, double radius) {
        double angle = Math.PI * 2;
        int stepCount = 16;
        double stepSize = angle / stepCount;
        List<Coordinates> newVertices = new ArrayList<>();
        while (angle >= 0) {
            double x;
            double y;
            x = Math.cos(angle) * radius + center.getX();
            y = Math.sin(angle) * radius + center.getY();
            if (ground.contains(x, y)) {
                newVertices.add(new Coordinates(x, y));
            }
            angle -= stepSize;
        }

        return newVertices;
    }

    public List<Double> getClosestVertices(Coordinates point, Polygon polygon) {
        List<Double> vertices = polygon.getPoints();
        double x0;
        double y0;
        double x1;
        double y1;
        boolean upwards;
        for (int i = 0; i < vertices.size() - 2; i += 2) {
            x0 = vertices.get(i);
            y0 = vertices.get(i + 1);
            x1 = vertices.get(i + 2);
            y1 = vertices.get(i + 3);
            //note does not work with negative path direction(ie backwards)
            if (point.getX() > x0 && point.getX() < x1) {
                upwards = y1 < y0;
                if ((upwards && point.getY() < y0 && point.getY() > y1)
                        ||
                        (!upwards && point.getY() > y0 && point.getY() < y1)) {
                    List<Double> closestVertices = new ArrayList<>();
                    closestVertices.add(x0);
                    closestVertices.add(y0);
                    closestVertices.add(x1);
                    closestVertices.add(y1);
                    return closestVertices;
                }
            }
        }
        return null;
    }


    public boolean detectCollision(Entity entity) {
        return ground.contains(entity.getCoordinates().getX(), entity.getCoordinates().getY());
    }

    @Override
    public Color getColor() {
        return Color.BEIGE;
    }
}
