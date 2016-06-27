package com.marceldias.externalsorting;

/**
 * TimeMetric
 *
 * Utility class to print the elapsed time of a step.
 */
public class TimeMetric {

    private String name;
    private Long start;

    public TimeMetric(String name) {
        this.name = name;
        this.start = System.currentTimeMillis();
    }

    public void print() {
        Long end = System.currentTimeMillis();
        System.out.println(name + " took " + (end-start) + " ms.");
    }
}
