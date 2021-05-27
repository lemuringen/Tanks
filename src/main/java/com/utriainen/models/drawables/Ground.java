package com.utriainen.models.drawables;

import com.utriainen.models.room.Coordinates;
import com.utriainen.models.room.Line;
import com.utriainen.models.room.PolygonVertex;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Polygon;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Ground implements Drawable {
    private Polygon ground;
    private List<PolygonVertex> vertices;

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
        this.vertices = PolygonVertex.makePolygon(vertices);
    }



    public void updateGround() {
        List<PolygonVertex> tmpList = new ArrayList<>();
        PolygonVertex currentVertex = vertices.get(0);
        boolean isHeadNext = false;
        while (!isHeadNext) {
            tmpList.add(currentVertex);
            if (currentVertex.getNextVertex().isFirst()) {
                isHeadNext = true;
            } else {
                currentVertex = currentVertex.getNextVertex();
            }
        }
        vertices = tmpList;

        ground = buildPolygon(vertices.get(0).getHead());
    }

    public Polygon buildPolygon(PolygonVertex polygonHead) {
        double[] polygonVertices = new double[polygonHead.getPreviousVertex().getDistanceToHead() * 2];
        PolygonVertex pointer = polygonHead;
        int counter = 0;
        do {
            polygonVertices[counter] = pointer.getCoordinates().getX();
            polygonVertices[counter + 1] = pointer.getCoordinates().getY();
            counter += 2;
            pointer = pointer.getNextVertex();
        } while (!pointer.isFirst());
        return new Polygon(polygonVertices);
    }

    public List<PolygonVertex> findEdgeVertices(Coordinates edgePoint) {
        PolygonVertex pointer = vertices.get(0).getHead();
        double edgePointX = edgePoint.getX();
        double edgePointY = edgePoint.getY();

        List<PolygonVertex> edgeNeighbours = new ArrayList<>();
        do {
            double currX = pointer.getCoordinates().getX();
            double currY = pointer.getCoordinates().getY();
            double nextX = pointer.getNextVertex().getCoordinates().getX();
            double nextY = pointer.getNextVertex().getCoordinates().getY();

            if (Math.min(currX, nextX) < edgePointX && edgePointX < Math.max(currX, nextX)
                    && Math.min(currY, nextY) < edgePointY + 10 && edgePointY - 10 < Math.max(currY, nextY)) {
                edgeNeighbours.add(pointer);
                edgeNeighbours.add(pointer.getNextVertex());
                return edgeNeighbours;
            }
            pointer = pointer.getNextVertex();
        } while (!pointer.isFirst());
        return null; //throw exception?
    }

    private void sortOutToBeRemovedVertices(List<PolygonVertex> toBeKept, List<PolygonVertex> toBeRemoved, PolygonVertex head, Circle circle) {
        PolygonVertex pointer = head;
        do {
            if (!circle.contains(pointer.getCoordinates().getX(), pointer.getCoordinates().getY())) {
                toBeKept.add(pointer);
            } else {
                toBeRemoved.add(pointer);
            }
            pointer = pointer.getNextVertex();
        } while (!pointer.isFirst());
    }

    /*todo which intersection should i choose*/
    /*todo DOES MODIFY POLYGON not clear*/
    public List<PolygonVertex> findCircleGroundIntersections(List<PolygonVertex> pointsInsideCircle, Circle circle) {
        List<PolygonVertex> interceptionPoints = new ArrayList<>();
        for (int i = 0; i < pointsInsideCircle.size(); i++) {
            PolygonVertex vertex = pointsInsideCircle.get(i);
            boolean isEdgeStart = !pointsInsideCircle.contains(vertex.getPreviousVertex());
            boolean isEdgeEnd = !pointsInsideCircle.contains(vertex.getNextVertex());
            if (isEdgeStart || isEdgeEnd) {
                if (isEdgeStart) {
                    Line l = new Line(vertex.getPreviousVertex().getCoordinates(), vertex.getCoordinates());
                    List<Coordinates> interceptions = l.findIntersection(circle);
                    Coordinates intersection = findClosestCoordinate(interceptions, vertex.getPreviousVertex().getCoordinates());
                    interceptionPoints.add(new PolygonVertex(vertex.getPreviousVertex(), intersection, null, false));
                } else if (isEdgeEnd) {
                    Line l = new Line(vertex.getCoordinates(), vertex.getNextVertex().getCoordinates());
                    List<Coordinates> interceptions = l.findIntersection(circle);
                    Coordinates intersection = findClosestCoordinate(interceptions, vertex.getNextVertex().getCoordinates());
                    interceptionPoints.add(new PolygonVertex(null, intersection, vertex.getNextVertex(), false));
                }
            }
            pointsInsideCircle.get(0).getPreviousVertex().setNextVertex(interceptionPoints.get(0));
            pointsInsideCircle.get(pointsInsideCircle.size() - 1).getNextVertex().setPreviousVertex(interceptionPoints.get(interceptionPoints.size() - 1));

        }
        return interceptionPoints;
    }

    public void subtractCircle(Coordinates center, double radius) {
        Circle circle = new Circle(center.getX(), center.getY(), radius);
        List<PolygonVertex> toBeRemoved = new ArrayList<>();
        List<PolygonVertex> toBeKept = new ArrayList<>();
        sortOutToBeRemovedVertices(toBeKept, toBeRemoved, vertices.get(0), circle);

        List<PolygonVertex> interceptionPoints;
        if (toBeRemoved.size() != 0) {
            interceptionPoints = findCircleGroundIntersections(toBeRemoved, circle);
        } else {
            interceptionPoints = findEdgeVertices(center);
        }
        List<Coordinates> perimeterPoints = getCirclePerimeterPoints(center, radius);
        List<Integer> emptyIndices = new ArrayList<>();
        List<Coordinates> includedPerimeterPoints = new ArrayList<>();
        for (Coordinates point : perimeterPoints) {
            if (ground.contains(point.getX(), point.getY())) {
                includedPerimeterPoints.add(point);
            } else {
                emptyIndices.add(perimeterPoints.indexOf(point));
            }
        }
        Coordinates closestCoordinate = findClosestCoordinate(includedPerimeterPoints, interceptionPoints.get(0).getCoordinates());//todo what if no vertices toBeRemoved?
        int i = perimeterPoints.indexOf(closestCoordinate);
        Iterator<PolygonVertex> it = interceptionPoints.iterator();
        PolygonVertex vertex = interceptionPoints.get(0);
        boolean lastWasEmpty = true;
        int stopIndex = perimeterPoints.size();

        for (int o = i; o < stopIndex; o++) {
            if (!emptyIndices.contains(o)) {
                if (lastWasEmpty) {
                    vertex = it.next();
                }
                vertex.setNextVertex(new PolygonVertex(vertex, perimeterPoints.get(o), null, false));
                vertex = vertex.getNextVertex();
                lastWasEmpty = false;
            } else if (emptyIndices.contains(o)) {
                if (!lastWasEmpty && it.hasNext()) {
                    PolygonVertex tmp = it.next();
                    vertex.setNextVertex(tmp);
                    tmp.setPreviousVertex(vertex);
                    vertex = tmp; //should do nothing?
                }
                lastWasEmpty = true;
            }
            if (o == perimeterPoints.size() - 1) {
                stopIndex = i;
                o = -1;
            }
        }
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
