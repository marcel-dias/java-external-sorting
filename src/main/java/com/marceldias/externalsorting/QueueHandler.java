package com.marceldias.externalsorting;

/**
 * QueueHandler interface
 *
 * Define a behavior to add a line to a Queue.
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
