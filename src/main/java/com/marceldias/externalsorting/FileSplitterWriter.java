package com.marceldias.externalsorting;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;

public class FileSplitterWriter implements Runnable {
    private FileSplitter fileSplitter;
    private String tempFilesDir = ExternalSortingProperties.TEMP_FILES_DIR.value();
    private final Boolean append = Boolean.TRUE;

    public FileSplitterWriter(FileSplitter fileSplitter) {
        this.fileSplitter = fileSplitter;
    }

    public void run() {
        try {
            while (!fileSplitter.isReaderDone() || (fileSplitter.isReaderDone() && !fileSplitter.getLinesQueue().isEmpty())) {
                String lineToProcess = fileSplitter.getLinesQueue().take();
                proccessLine(lineToProcess);
                fileSplitter.increment();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected void proccessLine(String line) {
        File file = getFile(line);

        try (BufferedWriter bw = new BufferedWriter(new java.io.FileWriter(file, append))) {

            bw.write(line);
            bw.newLine();
            bw.flush();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

    protected File getFile( String line) {
        return getFile("", line);
    }

    protected File getFile(String prefix, String line) {
        char start = line.charAt(0);
        String filename = (prefix + start).toLowerCase();
        File file = Paths.get(tempFilesDir, filename + ".txt").toFile();

//        Long maxTempFileSize = Long.valueOf(ExternalSortingProperties.MAX_TEMP_FILE_SIZE.value());
//        //todo improve this validation to handle concurrency
//        if (file.length() >= maxTempFileSize) {
//            file = getFile(""+start, line.substring(1));
//        }

        fileSplitter.addTempFile(filename, file);
        return file;
    }
}
