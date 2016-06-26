package com.marceldias.externalsorting;

import java.util.Iterator;
import java.util.LinkedList;

public class SortedList<T extends Sortable> extends LinkedList<T> {

    public boolean add(T obj) {
        LinkedList<String> terms = new LinkedList<>();
        Iterator<T> iter = iterator();
        for ( ; iter.hasNext();) {
            terms.add(iter.next().getTerm());
        }
        terms.add(obj.getTerm());
        Sorter sorter = new Sorter(terms);
        terms = sorter.sort();
        int index = terms.indexOf(obj.getTerm());
        add(index, obj);
        return true;
    }
}
