package com.marceldias;

import com.marceldias.externalsorting.AlphabeticalOrderValidator;
import com.marceldias.externalsorting.ExternalSortingProperties;
import com.marceldias.externalsorting.FileMerger;
import com.marceldias.externalsorting.FileSorter;
import com.marceldias.externalsorting.FileSplitter;
import com.marceldias.externalsorting.FileWriter;
import com.marceldias.externalsorting.Sorter;
import com.marceldias.externalsorting.TimeMetric;

import java.io.File;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ExternalSortingApplication {

    public static void main(String... args) {

        if (args.length > 0 && args[0].equals("validate")) {
            new AlphabeticalOrderValidator().validate(ExternalSortingProperties.OUTPUT.value());
        } else {

            ExternalSortingApplication app = new ExternalSortingApplication();
            app.validate();
            app.execute();
        }
    }

    protected void validate() {
        ExternalSortingProperties.FILENAME.isValid();
        ExternalSortingProperties.NR_WRITER_THREADS.isValid();
        ExternalSortingProperties.MAX_TEMP_FILE_SIZE.isValid();
    }

    protected void execute() {
        System.out.println(" Running External Sorting!");
        TimeMetric timeMetric = new TimeMetric("External Sorting App");

        FileSplitter fileSplitter = new FileSplitter();
        Map<String, File> tempFiles = fileSplitter.split();

        // multithread read, sort in alphabetic order and override file
        FileSorter fileSorter = new FileSorter();
        fileSorter.sort(new HashSet<>(tempFiles.values()));

        Sorter sorter = new Sorter();
        List<String> orderedFiles = sorter.sort(new LinkedList<>(tempFiles.keySet()));
        orderedFiles = new FileMerger().doMerge(orderedFiles, fileSplitter.getSameFirstCharFilename());

        // read and merge
        List<File> files = getOrderedFileList(orderedFiles, tempFiles);
        FileWriter fileWriter = new FileWriter();
        fileWriter.mergeFiles(files);

        timeMetric.print();
        System.out.println(" External Sorting successfully executed!");
    }

    protected List<File> getOrderedFileList(List<String> filenames, Map<String, File> tempFiles) {
        return filenames.stream().map(tempFiles::get).collect(Collectors.toCollection(() -> new LinkedList<>()));
    }
}
