package com.marceldias.externalsorting;

import com.marceldias.externalsorting.exception.ExternalSortingException;

import java.io.BufferedReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;

public class FileSplitterReader implements Runnable {
    private FileSplitter fileSplitter;

    public FileSplitterReader(FileSplitter fileSplitter) {
        this.fileSplitter = fileSplitter;
    }

    @Override
    public void run() {
        String filename = ExternalSortingProperties.FILENAME.value();
        Path path = Paths.get(filename);
        try (BufferedReader br = Files.newBufferedReader(path, StandardCharsets.UTF_8)) {
            String line;
            while ((line = br.readLine()) != null ) {
                fileSplitter.getLinesQueue().put(line);
            }
        } catch (NoSuchFileException e) {
            throw new ExternalSortingException("File not Found!", e);
        } catch (Exception e) {
            throw new ExternalSortingException("Unexpected error occured!", e);
        }
        fileSplitter.setIsReaderDone(Boolean.TRUE);
    }
}
