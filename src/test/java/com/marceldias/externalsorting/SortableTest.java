package com.marceldias.externalsorting;

public class SortableTest implements Sortable {

    private String line;

    public SortableTest(String line) {
        this.line = line;
    }

    @Override
    public String getTerm() {
        return line;
    }

    @Override
    public String toString() {
        return line;
    }
}
