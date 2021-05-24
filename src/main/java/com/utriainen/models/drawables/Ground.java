package com.utriainen.models.drawables;

import com.utriainen.models.room.Coordinates;
import com.utriainen.models.room.PolygonVertex;
import com.utriainen.models.room.SortByX;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Polygon;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Ground implements Drawable {
    private Polygon ground;
    //   private List<Coordinates> vertices;
    private List<PolygonVertex> vertices;

    public Polygon getGround() {
        return ground;
    }

    public Ground() {
        this.ground = new Polygon(
                0, 880,
                1920, 880,
                1920, 1080,
                0, 1080);
        List<Coordinates> vertices = new ArrayList<>();
        vertices.add(new Coordinates(0, 880));
        vertices.add(new Coordinates(1920, 880));
        vertices.add(new Coordinates(1920, 1080));
        vertices.add(new Coordinates(0, 1080));
        this.vertices = PolygonVertex.makePolygon(vertices);
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

    public void updateGround() {
        List<PolygonVertex> tmpList = new ArrayList<>();
        boolean isDone = false;
        PolygonVertex currentVertex = vertices.get(0);
        while (!isDone) {
            tmpList.add(currentVertex);
            if (currentVertex.getNextVertex().isFirst()) {
                isDone = true;
            } else {
                currentVertex = currentVertex.getNextVertex();
            }
        }
        vertices = tmpList;
        double[] polygonVertices = new double[vertices.size() * 2];
        int counter = 0;
        for (PolygonVertex vertex : vertices) {
            polygonVertices[counter] = vertex.getThisVertex().getX();
            polygonVertices[counter + 1] = vertex.getThisVertex().getY();
            counter += 2;
        }
        ground = new Polygon(polygonVertices);
    }

    /* possible scenarios with removing vertices:
     * -it is located first (from the seen from the beginning of the polygon) and its
     * previousVertex is outside of circle.
     * -it is located towards the Y-axis but is not first.
     * -it is first and last.
     * -it is last.
     * */
    public void deformGround(Coordinates center, double radius) {
        List<PolygonVertex> newPolygon = new ArrayList<>();
        //todo try making subtraction circle smaller
        Circle circle = new Circle(center.getX(), center.getY(), radius);
        List<PolygonVertex> finalNewPolygon = newPolygon;
        List<PolygonVertex> tmp = vertices.subList(0, vertices.size());
        List<PolygonVertex> pendingRemoval = new ArrayList<>();
         tmp.stream().forEach(polygonVertex ->
        {
            if (!circle.contains(polygonVertex.getThisVertex().getX(), polygonVertex.getThisVertex().getY())) {
                finalNewPolygon.add(polygonVertex);

            } else {
                pendingRemoval.add(polygonVertex);
            }
        });
        List<Coordinates> newVertices = new ArrayList<>();
        PolygonVertex firstVortex = null;
        PolygonVertex lastVortex = null;
        for (PolygonVertex vertex : pendingRemoval) {
            boolean isEdgeStart = !pendingRemoval.contains(vertex.getPreviousVertex());
            //todo need better check
            boolean isEdgeEnd = !pendingRemoval.contains(vertex.getNextVertex());

            if (isEdgeStart) {
                //we need to create new vertex linking outer edge and inner edge
                newVertices.add(findIntersection(vertex.getPreviousVertex().getThisVertex(), vertex.getThisVertex(), circle).get(0));
                firstVortex = vertex.getPreviousVertex();
            }
            if (isEdgeEnd) {
                newVertices.add(findIntersection(vertex.getNextVertex().getThisVertex(), vertex.getThisVertex(), circle).get(1));
                lastVortex = vertex.getNextVertex();
            }
        }
        if (firstVortex == null) {
            for(PolygonVertex vertex: newPolygon){
             if(center.getX()+1 > vertex.getThisVertex().getX() &&
             center.getX()-1 < vertex.getNextVertex().getThisVertex().getX() &&
             center.getY()+1 > vertex.getThisVertex().getY() &&
             center.getY()-1 < vertex.getNextVertex().getThisVertex().getY()){
                 firstVortex = vertex;
                 lastVortex = vertex.getNextVertex();
             }else if(center.getX()+1 > vertex.getThisVertex().getX() &&
                     center.getX()-1 < vertex.getNextVertex().getThisVertex().getX() &&
                     center.getY()+1 < vertex.getThisVertex().getY() &&
                     center.getY()-1 > vertex.getNextVertex().getThisVertex().getY()){
                 firstVortex = vertex;
                 lastVortex = vertex.getNextVertex();
             }
            }

            newVertices.addAll(findIntersection(firstVortex.getThisVertex(), lastVortex.getThisVertex(), circle));
        }
        newVertices.addAll(1, getNewVertices(center, radius));
        vertices = PolygonVertex.insertPolygonLink(newVertices, newPolygon, firstVortex, lastVortex);


        // newPolygon.sort(new SortByX());
        // newPolygon.addAll(vertices.subList(vertices.size() - 3, vertices.size()));
        // vertices = newPolygon;
    }

    //should make sure if/which point is contained in given circle
    public List<Coordinates> findIntersection(Coordinates p0, Coordinates p1, Circle circle) {
        double k = (p1.getY() - p0.getY()) / (p1.getX() - p0.getX());
        double m = p0.getY() - k * p0.getX();
        double C = circle.getCenterX() * circle.getCenterX()
                + circle.getCenterY() * circle.getCenterY()
                - circle.getRadius() * circle.getRadius();
        double a = 1 + k * k;
        double b = 2 * -circle.getCenterX() + 2 * -circle.getCenterY() * k + 2 * k * m;
        double c = 2 * -circle.getCenterY() * m + C + m * m;
        double p = b / a;
        double q = c / a;

        //PQ
        // todo biggest x not always first
        ArrayList<Coordinates> intersections = new ArrayList<>();
        double sqrt = Math.sqrt(p * p / 4 - q);
        double x0 = -p / 2 - sqrt;
        intersections.add(new Coordinates(x0, k * x0 + m));
        double x1 = -p / 2 + sqrt;
        intersections.add(new Coordinates(x1, k * x1 + m));
        return intersections;

    }

    public List<Coordinates> getNewVertices(Coordinates center, double radius) {
        double angle = Math.PI * 2;
        int stepCount = 16;
        double stepSize = angle / stepCount;
        List<Coordinates> newVertices = new ArrayList<>();
        boolean lastCheckSuccessful = false;
        boolean firstCheck = true;
        List<Integer> interruptionIndexes = new ArrayList<>();
        while (angle >= 0) {
            double x;
            double y;
            x = Math.cos(angle) * radius + center.getX();
            y = Math.sin(angle) * radius + center.getY();
            if (ground.contains(x, y)) {
                if (!firstCheck && !lastCheckSuccessful) {
                    interruptionIndexes.add(newVertices.size());
                }
                newVertices.add(new Coordinates(x, y));
                lastCheckSuccessful = true;
            } else {
                lastCheckSuccessful = false;
            }
            firstCheck = false;

            angle -= stepSize;
        }
        List<Coordinates> tmp = newVertices.subList(interruptionIndexes.get(0), newVertices.size());
        tmp.addAll(newVertices.subList(0,interruptionIndexes.get(0)));
        newVertices = tmp;
        return tmp;
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
