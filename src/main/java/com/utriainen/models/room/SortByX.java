package com.utriainen.models.room;

import java.util.Comparator;

public class SortByX implements Comparator<Coordinates> {
    @Override
    public int compare(Coordinates p0, Coordinates p1) {
        if(p0.getX() > p1.getX()) return 1;
        if(p0.getX() < p1.getX()) return -1;
        return 0;
    }
}
