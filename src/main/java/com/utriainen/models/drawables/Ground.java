package com.utriainen.models.drawables;

import com.utriainen.models.room.*;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Polygon;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Ground implements Drawable {
    private Polygon ground;
    private PolygonChain polygonChain;

    public Polygon getGround() {
        return ground;
    }

    public Ground() {
        this.ground = new Polygon(
                -500, 880,
                2420, 880,
                2420, 1580,
                0, 1580);
        List<Coordinates> vertices = new ArrayList<>();
        vertices.add(new Coordinates(-500, 880));
        vertices.add(new Coordinates(2420, 880));
        vertices.add(new Coordinates(2420, 1580));
        vertices.add(new Coordinates(-500, 1580));
        this.polygonChain = new PolygonChain(vertices);
    }


    public void updateGround() {
        ground = buildPolygon();
    }

    public Polygon buildPolygon() {
        double[] polygonVertices = new double[polygonChain.size() * 2];
        PolygonLink pointer;
        int counter = 0;
        Iterator<PolygonLink> it = polygonChain.iterator();
        while (it.hasNext()) {
            pointer = it.next();
            polygonVertices[counter] = pointer.getCoordinates().getX();
            polygonVertices[counter + 1] = pointer.getCoordinates().getY();
            counter += 2;
        }
        return new Polygon(polygonVertices);
    }

    /*TODO since no vertices should be within explosion radius we can exclude som comparisons based on the radius*/
    public List<PolygonLink> findEdgeVertices(Coordinates edgePoint) {
        List<PolygonLink> neighbourVertices = new ArrayList<>();
        Iterator<PolygonLink> it = polygonChain.iterator();
        PolygonLink link;
        double edgePointX = edgePoint.getX();
        double edgePointY = edgePoint.getY();

        do {
            link = it.next();
            double currX = link.getCoordinates().getX();
            double currY = link.getCoordinates().getY();
            double nextX = link.getNextLink().getCoordinates().getX();
            double nextY = link.getNextLink().getCoordinates().getY();

            if (Math.min(currX, nextX) < edgePointX && edgePointX < Math.max(currX, nextX)
                    && Math.min(currY, nextY) < edgePointY + 10 && edgePointY - 10 < Math.max(currY, nextY)) {
                neighbourVertices.add(link);
                neighbourVertices.add(link.getNextLink());
                return neighbourVertices;
            }
        } while (it.hasNext());
        return null; //throw exception?
    }

    public List<PolygonLink> findCircleGroundIntersections2(Circle circle) {
        List<PolygonLink> intersectionLinks = new ArrayList<>();
        Iterator<PolygonLink> it = polygonChain.iterator();
        PolygonLink link;
        PolygonLink preLink;
        PolygonLink nextLink;
        double currX;
        double currY;
        double preX;
        double preY;
        double nextX;
        double nextY;
        while (it.hasNext()) {
            link = it.next();
            preLink = link.getPreviousLink();
            nextLink = link.getNextLink();
            currX = link.getCoordinates().getX();
            currY = link.getCoordinates().getY();
            preX = link.getPreviousLink().getCoordinates().getX();
            preY = link.getPreviousLink().getCoordinates().getY();
            nextX = link.getNextLink().getCoordinates().getX();
            nextY = link.getNextLink().getCoordinates().getY();
            if (circle.contains(currX, currY)) {
                if (!circle.contains(preX, preY)) {
                    Coordinates intersection = findClosestCoordinate(new Line(preLink.getCoordinates(), link.getCoordinates()).findIntersection(circle), preLink.getCoordinates());
                    intersectionLinks.add(new PolygonLink(preLink, intersection, null, false));
                }
                if (!circle.contains(nextX, nextY)) {
                    Coordinates intersection = findClosestCoordinate(new Line(link.getCoordinates(), nextLink.getCoordinates()).findIntersection(circle), nextLink.getCoordinates());
                    intersectionLinks.add(new PolygonLink(null, intersection, nextLink, false));
                }
            }
        }
        if (intersectionLinks.size() == 0) { //todo
            List<PolygonLink> edgeVertices = findEdgeVertices(new Coordinates(circle.getCenterX(), circle.getCenterY()));
            List<Coordinates> intersections = new Line(edgeVertices.get(0).getCoordinates(), edgeVertices.get(1).getCoordinates()).findIntersection(circle);
            Coordinates first = intersections.get(1);
            Coordinates last = intersections.get(0);
            if (findClosestCoordinate(intersections, edgeVertices.get(0).getCoordinates()).equals(intersections.get(0))) {
                first = intersections.get(0);
                last = intersections.get(1);
            }
            intersectionLinks.add(new PolygonLink(edgeVertices.get(0), first, null, false));
            intersectionLinks.add(new PolygonLink(null, last, edgeVertices.get(1), false));
        }
        return intersectionLinks;
    }

    public static <T> List<T> startListAtIndex(List<T> oldList, int index) {
        List<T> list = oldList.subList(index, oldList.size());
        list.addAll(oldList.subList(0, index));
        return list;
    }

    public void addNewLinksToGround(List<PolygonLink> intersections, List<Coordinates> perimeterPoints, Circle circle) {
        for (int i = 0; i < intersections.size(); i += 2) {
            intersections.get(i).getPreviousLink().setNextLink(intersections.get(i));
            intersections.get(i + 1).getNextLink().setPreviousLink(intersections.get(i + 1));
            intersections.get(i).setNextLink(intersections.get(i + 1));
            intersections.get(i + 1).setPreviousLink(intersections.get(i));

        }
        int intersectionsCounter = 0;
        boolean withinTeeth = false;
        List<Coordinates> teeth = new ArrayList<>();
        double currX;
        double currY;
        for (Coordinates perimeterPoint : perimeterPoints) {
            currX = perimeterPoint.getX();
            currY = perimeterPoint.getY();

            if (ground.contains(currX, currY)) {
                withinTeeth = true;
            } else {
                if (!teeth.isEmpty() && withinTeeth) {
                    intersections.get(intersectionsCounter).insertLinksAfter(teeth);
                    teeth = new ArrayList<>();
                }
                withinTeeth = false;
                if (intersectionsCounter + 2 < intersections.size()) {
                    intersectionsCounter += 2;
                }
            }
            if (withinTeeth) {
                teeth.add(perimeterPoint);
            }
        }

    }

    public void subtractCircle(Coordinates center, double radius) {
        Circle circle = new Circle(center.getX(), center.getY(), radius);
        List<PolygonLink> intersections = findCircleGroundIntersections2(circle);
        List<Coordinates> circlePerimeterPoints = getCirclePerimeterPoints(center, radius);
        Coordinates closestToFirstIntersection = findClosestCoordinate(circlePerimeterPoints, intersections.get(0).getCoordinates());
        circlePerimeterPoints = startListAtIndex(circlePerimeterPoints, circlePerimeterPoints.indexOf(closestToFirstIntersection));
        addNewLinksToGround(intersections, circlePerimeterPoints, circle);
    }

    /*assumes no negative coordinates*/
    public Coordinates findClosestCoordinate(List<Coordinates> listToSearch, Coordinates referencePoint) {
        double distance;
        double minDistance = Math.abs(referencePoint.getX() - listToSearch.get(0).getX()) + Math.abs(referencePoint.getY() - listToSearch.get(0).getY());
        Coordinates closestCoordinates = listToSearch.get(0);
        for (int i = 1; i < listToSearch.size(); i++) {
            distance = Math.abs(referencePoint.getX() - listToSearch.get(i).getX()) + Math.abs(referencePoint.getY() - listToSearch.get(i).getY());
            if (distance < minDistance) {
                minDistance = distance;
                closestCoordinates = listToSearch.get(i);
            }
        }
        return closestCoordinates;
    }

    public List<Coordinates> getCirclePerimeterPoints(Coordinates center, double radius) {
        double angle = Math.PI * 2;
        int stepCount = 32;
        double stepSize = angle / stepCount;
        List<Coordinates> perimeterPoints = new ArrayList<>();
        while (angle >= 0) {
            double x;
            double y;
            x = Math.cos(angle) * radius + center.getX();
            y = Math.sin(angle) * radius + center.getY();
            perimeterPoints.add(new Coordinates(x, y));
            angle -= stepSize;
        }
        return perimeterPoints;
    }

    @Override
    public Color getColor() {
        return Color.BEIGE;
    }

    @Override
    public void draw(GraphicsContext context) {
        context.setFill(getColor());
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
}
