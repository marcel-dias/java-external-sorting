package com.marceldias.externalsorting;

import com.marceldias.externalsorting.exception.ExternalSortingException;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

/**
 * FileWriter centralizes the write operations to the files.
 */
public class FileWriter {

    private static final Boolean append = Boolean.TRUE;

    /**
     * Write content to a file.
     *
     * @param line content to be write
     * @param file file destine
     * @param append if @{code false} override the file content
     *               if @{code true} append lines to end of the file
     */
    public void writeLine(String line, File file, Boolean append) {
        writeLines(Arrays.asList(line), file, append);
    }

    /**
     * Write content to a file.
     *
     * @param line content to be write
     * @param file file destine
     */
    public void appendLine(String line, File file) {
        writeLine(line, file, append);
    }

    /**
     * Write content to a file.
     *
     * @param lines content to be write
     * @param file file destine
     * @param append if @{code false} override the file content
     *               if @{code true} append lines to end of the file
     */
    public void writeLines(List<String> lines, File file, Boolean append) {
        try (BufferedWriter bw = new BufferedWriter(new java.io.FileWriter(file, append))) {
            for (String line : lines) {
                bw.write(line);
                bw.newLine();
            }
            bw.flush();
        } catch (IOException e) {
            throw new ExternalSortingException(e.getMessage(), e);
        }
    }

    /**
     * Merge a list of file to the output file.
     * @see ExternalSortingProperties#OUTPUT
     *
     * @param files list of files to be merged
     */
    public void mergeFiles(List<File> files) {
        TimeMetric timeMetric = new TimeMetric("Merge Files");
        File output = Paths.get(ExternalSortingProperties.OUTPUT.value()).toFile();
        try (BufferedWriter bw = new BufferedWriter(new java.io.FileWriter(output, false))) {
            for (File file : files) {
                try (BufferedReader br = Files.newBufferedReader(file.toPath(), StandardCharsets.UTF_8)) {
                    String line;
                    while ((line = br.readLine()) != null ) {
                        bw.write(line);
                        bw.newLine();
                    }
                } catch (Exception e) {
                    continue;
                }
                bw.flush();
            }
        } catch (IOException e) {
            throw new ExternalSortingException(e.getMessage(), e);
        }
        timeMetric.print();
    }

    /**
     * Move files from source path to destine path
     *
     * @param source
     * @param destine
     */
    public void move(File source, File destine) {
        try {
            Files.move(source.toPath(), destine.toPath(), StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            throw new ExternalSortingException(e.getMessage(), e);
        }
    }

    /**
     * Delete the files
     *
     * @param files list of files to be deleted
     */
    public void delete(Set<File> files) {
        for (File file : files) {
            file.delete();
        }
    }
}
