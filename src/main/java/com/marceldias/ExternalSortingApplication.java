package com.marceldias;

import com.marceldias.externalsorting.FileSorter;
import com.marceldias.externalsorting.FileSplitter;
import com.marceldias.externalsorting.FileWriter;

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
        long startTime = System.currentTimeMillis();
        long startTotalTime = System.currentTimeMillis();
        //validate ExternalSortingProperties.FILENAME.value();

        FileSplitter fileSplitter = new FileSplitter();
        Map<String, File> tempFiles = fileSplitter.split();

        long endTime = System.currentTimeMillis();
        long elapsedTimeInMillis = (endTime - startTime);
        System.out.println("Splitter elapsed time: " + elapsedTimeInMillis + " ms");
        System.out.println("Lines processed: " + fileSplitter.getCount().get());
        System.out.println("Number of temp files: " + tempFiles.size());

        startTime = System.currentTimeMillis();
        // asynchronous
        // read, sort in alphabetic order and override file
        FileSorter sorter = new FileSorter();
        List<String> orderedFiles = sorter.sort(tempFiles);

        endTime = System.currentTimeMillis();
        elapsedTimeInMillis = (endTime - startTime);
        System.out.println("Sorter elapsed time: " + elapsedTimeInMillis + " ms");

        //synchronous
        // maybe read, sort and merge
//      sort in alphabetic order (filelist);
//      for each file in filelist

        // read and merge
        startTime = System.currentTimeMillis();
        List<File> files = getOrderedFileList(orderedFiles, tempFiles);
        FileWriter.mergeFiles(files);

        endTime = System.currentTimeMillis();
        elapsedTimeInMillis = (endTime - startTime);
        System.out.println("Merge Files elapsed time: " + elapsedTimeInMillis + " ms");

        //######################################
        endTime = System.currentTimeMillis();
        elapsedTimeInMillis = (endTime - startTotalTime);
        System.out.println("Total elapsed time: " + elapsedTimeInMillis + " ms");
        System.exit(0);
        System.out.println(" External Sorting successfully executed!");
    }

    private List<File> getOrderedFileList(List<String> filenames, Map<String, File> tempFiles) {
        List<File> orderedFiles = new LinkedList<>();
        for (String filename : filenames) {
            orderedFiles.add(tempFiles.get(filename));
        }
        return orderedFiles;
    }
}
