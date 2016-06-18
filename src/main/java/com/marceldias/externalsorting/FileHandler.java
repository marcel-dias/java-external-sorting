package com.marceldias.externalsorting;

/**
 * FileHandler interface
 */
public interface FileHandler {

    /**
     * Add file line to queue
     *
     * @param line is a line from a file
     * @throws InterruptedException
     */
    void addLineToQueue(String line) throws InterruptedException;
}
