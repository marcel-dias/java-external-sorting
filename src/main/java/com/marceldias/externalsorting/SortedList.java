package com.marceldias.externalsorting;

import java.util.Iterator;
import java.util.LinkedList;

/**
 * SortedList
 *
 * Is a sorted list that keeps itself sorted according to the Sorter.
 * @see Sorter
 *
 * @param <T> T class that extends Sortable
 */
public class SortedList<T extends Sortable> extends LinkedList<T> {

    private LinkedList<String> terms = new LinkedList<>();

    public boolean add(T obj) {
        terms.clear();
        Iterator<T> iter = iterator();
        for ( ; iter.hasNext();) {
            terms.add(iter.next().getTerm());
        }
        terms.add(obj.getTerm());
        Sorter sorter = new Sorter();
        terms = sorter.sort(terms);
        int index = terms.indexOf(obj.getTerm());
        add(index, obj);
        return true;
    }
}
