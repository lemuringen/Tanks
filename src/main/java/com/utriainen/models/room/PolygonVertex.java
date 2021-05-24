package com.utriainen.models.room;

import java.util.ArrayList;
import java.util.List;

public class PolygonVertex {
    private PolygonVertex previousVertex;
    private Coordinates thisVertex;
    private PolygonVertex nextVertex;
    private boolean isFirst;

    private PolygonVertex(PolygonVertex previousVertex, Coordinates thisVertex, PolygonVertex nextVertex, boolean isFirst) {
        this.previousVertex = previousVertex;
        this.thisVertex = thisVertex;
        this.nextVertex = nextVertex;
        this.isFirst = isFirst;
    }

    public static List<PolygonVertex> makePolygon(List<Coordinates> vertices) {
        if (vertices == null) return null;
        List<PolygonVertex> polygonVertices = new ArrayList<>();
        // polygonVertices.add(new PolygonVertex(vertices.get(vertices.size()-1), vertices.get(0), vertices.get(1), true));
        polygonVertices.add(new PolygonVertex(null, vertices.get(0), null, true));
        PolygonVertex currentVertex;
        for (int i = 1; i < vertices.size()-1; i++) {
            currentVertex = new PolygonVertex(polygonVertices.get(i - 1), vertices.get(i), null, false);
            polygonVertices.add(currentVertex);
            currentVertex.getPreviousVertex().setNextVertex(currentVertex);
        }
        currentVertex = new PolygonVertex(polygonVertices.get(polygonVertices.size() - 1), vertices.get(vertices.size() - 1), polygonVertices.get(0), false);
        polygonVertices.add(currentVertex);
        currentVertex.getPreviousVertex().setNextVertex(currentVertex);
        polygonVertices.get(0).setPreviousVertex(currentVertex);
        return polygonVertices;
    }
//does not work before first vertex
    static public List<PolygonVertex> insertPolygonLink(List<Coordinates> newVertexCoordinates, List<PolygonVertex> oldPolygon, PolygonVertex startConnection, PolygonVertex endConnection) {
        List<PolygonVertex> newPolygon = PolygonVertex.makePolygon(newVertexCoordinates);
        int i0 = oldPolygon.indexOf(startConnection);
        newPolygon.get(0).setPreviousVertex(oldPolygon.get(i0));
        oldPolygon.get(i0).setNextVertex(newPolygon.get(0));
        newPolygon.get(0).setFirst(false);
        int i1 = oldPolygon.indexOf(endConnection);
        newPolygon.get(newPolygon.size()-1).setNextVertex(oldPolygon.get(i1));
        oldPolygon.get(i1).setPreviousVertex(newPolygon.get(newPolygon.size()-1));
        newPolygon.addAll(oldPolygon.subList(i1,oldPolygon.size()));
        newPolygon.addAll(0,oldPolygon.subList(0,i0+1));
        return newPolygon;
    }

    public PolygonVertex getPreviousVertex() {
        return previousVertex;
    }

    public void setPreviousVertex(PolygonVertex previousVertex) {
        this.previousVertex = previousVertex;
    }

    public Coordinates getThisVertex() {
        return thisVertex;
    }

    public void setThisVertex(Coordinates thisVertex) {
        this.thisVertex = thisVertex;
    }

    public PolygonVertex getNextVertex() {
        return nextVertex;
    }

    public void setNextVertex(PolygonVertex nextVertex) {
        this.nextVertex = nextVertex;
    }

    public boolean isFirst() {
        return isFirst;
    }

    public void setFirst(boolean first) {
        isFirst = first;
    }

}