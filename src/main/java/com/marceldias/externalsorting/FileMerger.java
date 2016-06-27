package com.marceldias.externalsorting;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * FileMerger
 *
 * Manages the execution of FileMergerTasks
 *
 */
public class FileMerger {

    private static Integer NR_WRITER_THREADS = Integer.valueOf(ExternalSortingProperties.NR_WRITER_THREADS.value());

    public List<String> doMerge(List<String> orderedFiles, Map<String, Set<File>> filesToMerge) {
        TimeMetric timeMetric = new TimeMetric("K-way Merge");
        List<String> toRemove = new ArrayList<>();

        ExecutorService mergerPool = Executors.newFixedThreadPool(NR_WRITER_THREADS/2);
        List<Future> futures = new ArrayList();

        for (String filename : orderedFiles) {
            Set<File> fragments = filesToMerge.get(filename);
            if (fragments != null) {
                if (fragments.size() > 1) {
                    futures.add(mergerPool.submit(new FileMergerTask(fragments, filename)));
                }
            } else {
                toRemove.add(filename);
            }
        }
        mergerPool.shutdown();
        FutureHelper.waitExecution(futures);
        orderedFiles.removeAll(toRemove);
        timeMetric.print();
        return orderedFiles;
    }

}
