package com.marceldias.externalsorting;

import java.io.File;
import java.nio.file.Paths;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

/**
 * FileSplitterWriter is a queue consumer.
 * Defines for which temporary file will write the line based on the line content and size of temporary file.
 *
 * Implements Callable to run multiples consumers at same time.
 */
public class FileSplitterWriter extends FileWriter implements Callable<Boolean> {
    private FileSplitter fileSplitter;
    private String tempFilesDir = ExternalSortingProperties.TEMP_FILES_DIR.value();

    public FileSplitterWriter(FileSplitter fileSplitter) {
        this.fileSplitter = fileSplitter;
    }

    /**
     * Consumes the fileSplitter polling lines from it with the 500 milliseconds of timeout.
     * Consumes the queue until the FileReader has finished.
     *
     * @see FileSplitter#getLinesQueue()
     * @see FileReader
     *
     * @return @{code true} if runs well
     *         @{code true} if get a exception
     */
    @Override
    public Boolean call() {
        try {
            while (!fileSplitter.isReaderDone() || (fileSplitter.isReaderDone() && !fileSplitter.getLinesQueue().isEmpty())) {
                String lineToProcess = fileSplitter.getLinesQueue().poll(500L, TimeUnit.MILLISECONDS);
                if (lineToProcess != null) {
                    proccessLine(lineToProcess);
                    fileSplitter.increment();
                }
            }
        } catch (Exception e) {
            return Boolean.FALSE;
        }
        return Boolean.TRUE;
    }

    /**
     * Process the line.
     * Get the file and append contente line to it.
     * @param line
     */
    protected void proccessLine(String line) {
        File file = getFile(line);
        appendLine(line, file);
    }

    protected File getFile( String line) {
        return getFile("", line);
    }

    /**
     * Define the temporary file based on the line content and temporary file size.
     * If a temporary file get full then it will add the one more char to a new temporary file.
     * Ex: 'a.txt' temporary file get full and the actual line is 'abcdefg' the new file will be 'ab.txt'
     *
     * @param prefix a prefix to the temporary file name
     * @param line the line content to define the temporary file
     * @return the temporary file to write the line
     */
    protected File getFile(String prefix, String line) {
        char start = line.charAt(0);
        String filename = (prefix + start).toLowerCase();

        File file = fileSplitter.getTempFiles().get(filename);
        if (file == null) {
            file = Paths.get(tempFilesDir, filename + ".txt").toFile();
            file.deleteOnExit();
            fileSplitter.addTempFile(filename, file);
        } else {
            Long maxTempFileSize = Long.valueOf(ExternalSortingProperties.MAX_TEMP_FILE_SIZE.value());
            if (fileSplitter.isFileExhausted(filename) || file.length() >= maxTempFileSize) {
                fileSplitter.addExhaustedFile(filename);
                file = getFile(filename, line.substring(1));
            }
        }
        fileSplitter.checkSameFirstCharFilename(file);
        return file;
    }
}
