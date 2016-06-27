package com.marceldias.externalsorting;

import com.marceldias.externalsorting.exception.ExternalSortingException;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Paths;
import java.util.Set;
import java.util.concurrent.Callable;

/**
 * FileMergerTask implements Callable to be possible merge multiple temporary fragments at same time
 */
public class FileMergerTask implements Callable<Boolean> {

    private FileWriter fileWriter;
    private Set<File> fragments;
    private String filename;

    public FileMergerTask(Set<File> fragments, String filename) {
        this.fileWriter = new FileWriter();
        this.fragments = fragments;
        this.filename = filename;
    }


    @Override
    public Boolean call() throws Exception {
        executeKWayMerge(fragments, filename);
        return Boolean.TRUE;
    }

    /**
     * Use K-way merge to merge the files in a sorted way
     *
     * @param files files list with content to be merged
     * @param outputfile file to merge the content of files list
     */
    public void executeKWayMerge(Set<File> files, String outputfile) {
        TimeMetric timeMetric = new TimeMetric("FileMergerTask k-way merge");
        //create a temporary file
        File dir = Paths.get(ExternalSortingProperties.TEMP_FILES_DIR.value(), "k-way").toFile();
        dir.mkdirs();
        File output = new File(dir, outputfile);

        try (BufferedWriter bw = new BufferedWriter(new java.io.FileWriter(output, false))) {
            SortedList<BufferedReaderWrapper> autoOrderedList = getAutoSortedList(files);
            mergeSortedLines(bw, autoOrderedList);
            bw.flush();
        } catch (NoSuchFileException e) {
            throw new ExternalSortingException("File not Found!", e);
        } catch (Exception e) {
            throw new ExternalSortingException("Unexpected error occured!", e);
        }
        fileWriter.delete(files);

        //move to outputfile
        File destine = Paths.get(ExternalSortingProperties.TEMP_FILES_DIR.value(), outputfile+".txt").toFile();
        fileWriter.move(output, destine);
        dir.delete();
        timeMetric.print();
    }

    /**
     * Writes lines to the output file in a sorted way.
     * The SortedList keeps always the sorted next line on head to write to the file.
     *
     * @see SortedList
     *
     * @param bw BufferedWriter instance to the output file
     * @param autoOrderedList SortedList instance
     * @throws IOException
     */
    private void mergeSortedLines( BufferedWriter bw, SortedList<BufferedReaderWrapper> autoOrderedList) throws IOException {
        Integer rowCount = 0;
        while (autoOrderedList.size() > 0) {
            BufferedReaderWrapper bfw = autoOrderedList.poll();
            String r = bfw.poll();
            bw.write(r);
            bw.newLine();
            rowCount++;
            if (bfw.isEmpty()) {
                bfw.close();
            } else {
                // add it back
                autoOrderedList.add(bfw);
            }
            if (rowCount % 1000 == 0) {
                bw.flush();
            }
        }
    }

    /**
     * Creates the sorted list based on files to be merged
     * 
     * @param files
     * @return
     * @throws IOException
     */
    private SortedList<BufferedReaderWrapper> getAutoSortedList(Set<File> files) throws IOException {
        SortedList<BufferedReaderWrapper> autoOrderedList = new SortedList();
        for (File file : files) {
            BufferedReader br = Files.newBufferedReader(file.toPath(), StandardCharsets.UTF_8);
            autoOrderedList.add(new BufferedReaderWrapper(br));
        }
        return autoOrderedList;
    }
}
