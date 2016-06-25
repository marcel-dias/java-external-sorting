package com.marceldias.externalsorting;

import java.util.ArrayList;
import java.util.List;

public class TestQueueHandler implements QueueHandler {
    List queue = new ArrayList<>();

    @Override
    public void addLineToQueue(String line) throws InterruptedException {
        queue.add(line);
    }

    public List getQueue() {
        return queue;
    }
}
