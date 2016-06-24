package com.marceldias.externalsorting;

import java.io.File;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

public class FileSplitterWriter extends FileWriter implements Callable<Boolean> {
    private FileSplitter fileSplitter;

    public FileSplitterWriter(FileSplitter fileSplitter) {
        this.fileSplitter = fileSplitter;
    }

    @Override
    public Boolean call() {
        try {
            while (!fileSplitter.isReaderDone() || (fileSplitter.isReaderDone() && !fileSplitter.getLinesQueue().isEmpty())) {
                String lineToProcess = fileSplitter.getLinesQueue().poll(500l, TimeUnit.MILLISECONDS);
                if (lineToProcess != null) {
                    proccessLine(lineToProcess);
                    fileSplitter.increment();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return Boolean.FALSE;
        }
        return Boolean.TRUE;
    }

    protected void proccessLine(String line) {
        File file = fileSplitter.getFile(line);
        appendLine(line, file);
    }
}
