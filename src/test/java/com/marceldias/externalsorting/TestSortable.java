package com.marceldias.externalsorting;

public class TestSortable implements Sortable {

    private String line;

    public TestSortable(String line) {
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
