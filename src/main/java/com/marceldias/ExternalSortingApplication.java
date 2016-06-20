package com.marceldias;

import com.marceldias.externalsorting.FileSorter;
import com.marceldias.externalsorting.FileSplitter;

import java.io.File;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class ExternalSortingApplication {

    public static void main(String... args) {
        System.out.println(" Running External Sorting!");
        long startTime = System.currentTimeMillis();
        long startTotalTime = System.currentTimeMillis();
        //validate ExternalSortingProperties.FILENAME.value();

        FileSplitter fileSplitter = new FileSplitter();
        Map<String, File> tempFiles = fileSplitter.split();

        long endTime = System.currentTimeMillis();
        long elapsedTimeInMillis = (endTime - startTime);
        System.out.println("Splitter elapsed time: " + elapsedTimeInMillis + " ms");
        elapsedTimeInMillis = TimeUnit.SECONDS.convert(elapsedTimeInMillis, TimeUnit.MILLISECONDS);
        System.out.println("Splitter elapsed time: " + elapsedTimeInMillis + " sec");
        System.out.println("Lines processed: " + fileSplitter.getCount().get());

        startTime = System.currentTimeMillis();
        //synchronous
        // maybe read, sort and merge
        FileSorter sorter = new FileSorter(tempFiles);
        sorter.sort();

        endTime = System.currentTimeMillis();
        elapsedTimeInMillis = (endTime - startTime);
        System.out.println("Sorter elapsed time: " + elapsedTimeInMillis + " ms");
        elapsedTimeInMillis = TimeUnit.SECONDS.convert(elapsedTimeInMillis, TimeUnit.MILLISECONDS);
        System.out.println("Sorter elapsed time: " + elapsedTimeInMillis + " sec");

//      sort in alphabetic order (filelist);
//      for each file in filelist

        // asynchronous
        // read, sort in alphabetic order and override file
        // read and merge

        endTime = System.currentTimeMillis();
        elapsedTimeInMillis = (endTime - startTotalTime);
        System.out.println("Total elapsed time: " + elapsedTimeInMillis + " ms");
        elapsedTimeInMillis = TimeUnit.SECONDS.convert(elapsedTimeInMillis, TimeUnit.MILLISECONDS);
        System.out.println("Total elapsed time: " + elapsedTimeInMillis + " sec");
        System.exit(0);
        System.out.println(" External Sorting successfully executed!");
    }
}
