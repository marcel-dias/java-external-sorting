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

    /**
     * Iterates the ordered files list looking for those that has fragments.
     * Fragment is a sub-file that share that same first char. Ex: 'a.txt' is the main file and 'ab.txt' is a sub-file.
     *
     * Creates a FileMergerTask for those files that has fragments to execute a K-way merge
     *
     * @param sortedFiles list with all sorted files
     * @param filesToMerge map files to merge where key is first char 'a'
     *                     and value is a set with all fragments 'ab','ac'
     * @return list of sorted files without the fragments
     */
    public List<String> doMerge(List<String> sortedFiles, Map<String, Set<File>> filesToMerge) {
        TimeMetric timeMetric = new TimeMetric("K-way Merge");
        List<String> toRemove = new ArrayList<>();

        ExecutorService mergerPool = Executors.newFixedThreadPool(NR_WRITER_THREADS/2);
        List<Future> futures = new ArrayList();

        for (String filename : sortedFiles) {
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
        sortedFiles.removeAll(toRemove);
        timeMetric.print();
        return sortedFiles;
    }

}
