package com.marceldias.externalsorting;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class FileSorter {

    private static Integer NR_WRITER_THREADS = 4;

    public static List<String> sort(Map<String, File> files) {
        Sorter sorter = new Sorter();
        List<String> orderedFiles = sorter.sortFilenames(files.keySet());

        ExecutorService writerPool = Executors.newFixedThreadPool(NR_WRITER_THREADS);
        List<Future> futures = new ArrayList();
        for (String filename : orderedFiles) {
            futures.add(writerPool.submit(new Sorter(files.get(filename))));
        }
        writerPool.shutdown();
        FutureHelper.waitExecution(futures);
        return orderedFiles;
    }
}
