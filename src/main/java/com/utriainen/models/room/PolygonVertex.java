package com.utriainen.models.room;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

public class PolygonVertex {
    private PolygonVertex previousVertex;
    private Coordinates coordinates;
    private PolygonVertex nextVertex;
    private boolean isFirst;

    public PolygonVertex(PolygonVertex previousVertex, Coordinates coordinates, PolygonVertex nextVertex, boolean isFirst) {
        this.previousVertex = previousVertex;
        this.coordinates = coordinates;
        this.nextVertex = nextVertex;
        this.isFirst = isFirst;
    }

    public static List<PolygonVertex> makePolygon(List<Coordinates> vertices) {
        if (vertices == null) return null;
        List<PolygonVertex> polygonVertices = new ArrayList<>();
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
    static public void insertPolygonLink(List<Coordinates> newVertexCoordinates, PolygonVertex firstConnection, PolygonVertex lastConnection) {
        List<PolygonVertex> newPolygon = PolygonVertex.makePolygon(newVertexCoordinates);
        newPolygon.get(0).setPreviousVertex(firstConnection);
        newPolygon.get(0).setFirst(false);
        firstConnection.setNextVertex(newPolygon.get(0));
        newPolygon.get(newPolygon.size()-1).setNextVertex(lastConnection);
        lastConnection.setPreviousVertex(newPolygon.get(newPolygon.size()-1));

    }
static public void insertPolygonLink(LinkedHashMap<Coordinates, PolygonVertex> mapFromInterceptionPoints, List<Coordinates> newVertices ){

}
    public PolygonVertex getPreviousVertex() {
        return previousVertex;
    }

    public void setPreviousVertex(PolygonVertex previousVertex) {
        this.previousVertex = previousVertex;
    }

    public Coordinates getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(Coordinates coordinates) {
        this.coordinates = coordinates;
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


    public int getDistanceToHead(){
        int i = 1;
        if(isFirst()) return i;
        return getPreviousVertex().getDistanceToHead(++i);
    }
    private int getDistanceToHead(int i){
        if(isFirst()) return i;
        return getPreviousVertex().getDistanceToHead(++i);
    }
    public PolygonVertex getHead(){
        if(isFirst()) return this;
        return previousVertex.getHead();
    }
}
