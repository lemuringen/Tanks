package com.utriainen.models.room;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class PolygonChain implements Iterable<PolygonLink> {
    PolygonLink head;

    /**
     * @param vertices List of at least 2 Coordinates objects
     * @throws IllegalArgumentException
     */
    public PolygonChain(List<Coordinates> vertices) throws IllegalArgumentException {
        if (vertices.size() == 1) throw new IllegalArgumentException();
        this.head = new PolygonLink(new PolygonLink(null, vertices.get(vertices.size() - 1), null, false), vertices.get(0), null, true);
        this.head.getPreviousLink().setNextLink(this.head);
        PolygonLink link = this.head;
        for (int i = 1; i < vertices.size() - 1; i++) {
            link = new PolygonLink(link, vertices.get(i), null, false);
            link.getPreviousLink().setNextLink(link);
        }
        link.setNextLink(head.getPreviousLink());
        head.getPreviousLink().setPreviousLink(link);
    }

    public int size() {
        Iterator<PolygonLink> it = this.iterator();
        int i = 0;
        while (it.hasNext()){
            i++;
            it.next();
        }
        return i;
    }

    public PolygonLink getHead() {
        return head;
    }

    @Override
    public Iterator iterator() {
        return new PolygonChainIterator(head);
    }

    class PolygonChainIterator implements Iterator<PolygonLink> {
        private PolygonLink pointer;
        boolean hasFinishedLoop = false;

        private PolygonChainIterator(PolygonLink head) {
            pointer = head;
        }

        @Override
        public boolean hasNext() {
            return !hasFinishedLoop;
        }

        @Override
        public PolygonLink next() {
            PolygonLink oldPointer = pointer;
            pointer = pointer.getNextLink();
            if(pointer.isFirst()) hasFinishedLoop = true;
            return oldPointer;
        }

        @Override
        public void remove() {
            head.getPreviousLink().setNextLink(head.getNextLink());
            head.getNextLink().setPreviousLink(head.getPreviousLink());
        }
    }
}
