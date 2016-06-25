package com.marceldias.externalsorting;

import java.io.File;
import java.util.LinkedList;
import java.util.concurrent.Callable;

/**
 * FileSorterTask implements Callable to be possible sort multiple files at same time
 */
public class FileSorterTask implements Callable<Boolean>, QueueHandler {

    private LinkedList<String> queue = new LinkedList<>();
    private File file;

    /**
     * @param file file to be sorted
     */
    public FileSorterTask(File file) {
        this.file = file;
    }

    /**
     * This method is used by the executors to run multiple sorters at the same time.
     *
     * @return @{code true} if it runs ok
     * @throws Exception
     */
    @Override
    public Boolean call() throws Exception {
        new FileReader(this, file.getAbsolutePath()).execute();

        Sorter sorter = new Sorter(queue);
        queue = sorter.sort();
        FileWriter.writeLines(queue, file, false);
        return Boolean.TRUE;
    }

    @Override
    public void addLineToQueue(String line) throws InterruptedException {
        queue.add(line);
    }
}
