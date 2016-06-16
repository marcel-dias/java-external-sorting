package com.marceldias;

import com.marceldias.externalsorting.FileSplitter;

import java.io.File;
import java.util.Map;

public class ExternalSortingApplication {

    public static void main(String... args) {
        System.out.println(" Running External Sorting!");

        //validate ExternalSortingProperties.FILENAME.value();

        FileSplitter fileSplitter = new FileSplitter();
        Map<String, File> tempFiles = fileSplitter.split();

//      sort in alphabetic order (filelist);
//      for each file in filelist

        // asynchronous
        // read, sort in alphabetic order and override file
        // read and merge

        //synchronous
        // maybe read, sort and merge

        System.out.println(" External Sorting successfully executed!");
    }
}
