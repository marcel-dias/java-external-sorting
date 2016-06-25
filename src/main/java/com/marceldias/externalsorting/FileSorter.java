package com.marceldias.externalsorting;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class FileSorter {

    private static Integer NR_WRITER_THREADS = Integer.valueOf(ExternalSortingProperties.NR_WRITER_THREADS.value());

    private FileSorter(){}

    public static List<String> sort(Map<String, File> files) {
        TimeMetric timeMetric = new TimeMetric("File Sorter");
        Sorter sorter = new Sorter(new LinkedList<>(files.keySet()));
        List<String> orderedFiles = sorter.sort();

        ExecutorService writerPool = Executors.newFixedThreadPool(NR_WRITER_THREADS);
        List<Future> futures = new ArrayList();
        for (String filename : orderedFiles) {
            futures.add(writerPool.submit(new FileSorterTask(files.get(filename))));
        }
        writerPool.shutdown();
        FutureHelper.waitExecution(futures);
        timeMetric.print();
        return orderedFiles;
    }
}
