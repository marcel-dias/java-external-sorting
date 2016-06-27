package com.marceldias.externalsorting;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * FileSorter
 *
 * Manages the execution of FileSorterTasks
 */
public class FileSorter {

    private static Integer NR_WRITER_THREADS = Integer.valueOf(ExternalSortingProperties.NR_WRITER_THREADS.value());

    /**
     * Iterates each file and create a FileSorterTask to sort it.
     * Runs the Thread Executor with size defined in the ExternalSortingProperties.NR_WRITER_THREADS
     * @see ExternalSortingProperties#NR_WRITER_THREADS
     *
     * @param files list of files to be sorted
     */
    public void sort(Set<File> files) {
        TimeMetric timeMetric = new TimeMetric("File Sorter");

        ExecutorService writerPool = Executors.newFixedThreadPool(NR_WRITER_THREADS);
        List<Future> futures = new ArrayList();
        for (File file : files) {
            futures.add(writerPool.submit(new FileSorterTask(file)));
        }
        writerPool.shutdown();
        FutureHelper.waitExecution(futures);
        timeMetric.print();
    }
}
