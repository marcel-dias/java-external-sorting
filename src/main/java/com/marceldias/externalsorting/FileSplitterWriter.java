package com.marceldias.externalsorting;

import java.io.File;
import java.nio.file.Paths;
import java.util.concurrent.Callable;

public class FileSplitterWriter extends FileWriter implements Callable<Boolean> {
    private FileSplitter fileSplitter;
    private String tempFilesDir = ExternalSortingProperties.TEMP_FILES_DIR.value();

    public FileSplitterWriter(FileSplitter fileSplitter) {
        this.fileSplitter = fileSplitter;
    }

    @Override
    public Boolean call() {
        try {
            while (!fileSplitter.isReaderDone() || (fileSplitter.isReaderDone() && !fileSplitter.getLinesQueue().isEmpty())) {
                String lineToProcess = fileSplitter.getLinesQueue().take();
                proccessLine(lineToProcess);
                fileSplitter.increment();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return Boolean.FALSE;
        }
        return Boolean.TRUE;
    }

    protected void proccessLine(String line) {
        File file = getFile(line);
        appendLine(line, file);
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
