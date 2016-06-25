package com.marceldias;

import com.marceldias.externalsorting.FileSorter;
import com.marceldias.externalsorting.FileSplitter;
import com.marceldias.externalsorting.FileWriter;
import com.marceldias.externalsorting.TimeMetric;

import java.io.File;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class ExternalSortingApplication {

    public static void main(String... args) {
        new ExternalSortingApplication().execute();
    }

    private void execute() {
        System.out.println(" Running External Sorting!");
        TimeMetric timeMetric = new TimeMetric("External Sorting App");

        FileSplitter fileSplitter = new FileSplitter();
        Map<String, File> tempFiles = fileSplitter.split();

        // asynchronous
        // read, sort in alphabetic order and override file
        List<String> orderedFiles = FileSorter.sort(tempFiles);

        // read and merge
        List<File> files = getOrderedFileList(orderedFiles, tempFiles);
        FileWriter.mergeFiles(files);

        timeMetric.print();
        System.out.println(" External Sorting successfully executed!");
        System.exit(0);
    }

    protected List<File> getOrderedFileList(List<String> filenames, Map<String, File> tempFiles) {
        List<File> orderedFiles = new LinkedList<>();
        for (String filename : filenames) {
            orderedFiles.add(tempFiles.get(filename));
        }
        return orderedFiles;
    }
}
