package com.utriainen.models.room;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

public class PolygonLink {
    private PolygonLink previousLink;
    private Coordinates coordinates;
    private PolygonLink nextLink;
    private boolean isFirst;

    public PolygonLink(PolygonLink previousLink, Coordinates coordinates, PolygonLink nextLink, boolean isFirst) {
        this.previousLink = previousLink;
        this.coordinates = coordinates;
        this.nextLink = nextLink;
        this.isFirst = isFirst;
    }/*
    static void insertPolygonLink(List<Coordinates> newVertices, PolygonLink linkBeforeInsertion, PolygonLink linkAfterInsertion) {
        List<PolygonLink> newPolygon = PolygonLink.makePolygon(newVertexCoordinates);
        newPolygon.get(0).setPreviousLink(firstConnection);
        newPolygon.get(0).setFirst(false);
        firstConnection.setNextLink(newPolygon.get(0));
        newPolygon.get(newPolygon.size()-1).setNextLink(lastConnection);
        lastConnection.setPreviousLink(newPolygon.get(newPolygon.size()-1));

    }*/
    public void insertLinksAfter(List<Coordinates> newVertices){
        PolygonLink link = this;
        PolygonLink endLink = getNextLink();
        for(Coordinates vertex : newVertices){
            link.setNextLink(new PolygonLink(link, vertex, null, false));
            link = link.getNextLink();
        }
        link.setNextLink(endLink);
        endLink.setPreviousLink(link);
    }

    public void insertLinkAfter(PolygonLink link){
       link.setNextLink(nextLink);
       nextLink.setPreviousLink(link);
       nextLink = link;
       link.setPreviousLink(this);
    }
    public void insertLinkBefore(PolygonLink link){
        previousLink.setNextLink(link);
        link.setPreviousLink(previousLink);
        link.setNextLink(this);
        previousLink = link;
    }
    public PolygonLink getPreviousLink() {
        return previousLink;
    }

    public void setPreviousLink(PolygonLink previousLink) {
        this.previousLink = previousLink;
    }

    public Coordinates getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(Coordinates coordinates) {
        this.coordinates = coordinates;
    }

    public PolygonLink getNextLink() {
        return nextLink;
    }

    public void setNextLink(PolygonLink nextLink) {
        this.nextLink = nextLink;
    }

    public boolean isFirst() {
        return isFirst;
    }

    public void setFirst(boolean first) {
        isFirst = first;
    }

}
