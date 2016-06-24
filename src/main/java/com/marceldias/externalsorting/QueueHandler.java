package com.marceldias.externalsorting;

/**
 * QueueHandler interface
 */
public interface QueueHandler {

    /**
     * Add file line to queue
     *
     * @param line is a line from a file
     * @throws InterruptedException
     */
    void addLineToQueue(String line) throws InterruptedException;
}
