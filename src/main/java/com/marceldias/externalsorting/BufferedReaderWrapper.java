package com.marceldias.externalsorting;

import com.marceldias.externalsorting.exception.ExternalSortingException;

import java.io.BufferedReader;
import java.io.IOException;

/**
 * A wrapper to BufferedReader that caches the last read line.
 */
public class BufferedReaderWrapper implements Sortable {

    private BufferedReader bufferedReader;
    private String line;

    public BufferedReaderWrapper(BufferedReader bufferedReader) {
        this.bufferedReader = bufferedReader;
        readNextLine();
    }

    /**
     * Closes the bufferedReader.
     * @throws IOException
     */
    public void close() throws IOException {
        this.bufferedReader.close();
    }

    /**
     * Verifies if the line attribute is null what means that bufferedReader read all lines of file.
     * @return
     */
    public boolean isEmpty() {
        return this.line == null;
    }

    /**
     * Returns the actual line and keep it.
     * @return the actual line
     */
    public String peek() {
        return this.line;
    }

    /**
     * Returns the actual line and get the next line from the bufferedReader.
     * @return the actual line
     * @throws IOException
     */
    public String poll() throws IOException {
        String answer = peek();
        readNextLine();
        return answer;
    }

    /**
     * Gets the bufferedReader next line and caches it to the line attribute.
     */
    private void readNextLine() {
        try {
            this.line = bufferedReader.readLine();
        } catch (IOException e) {
            throw new ExternalSortingException(e.getMessage(), e);
        }
    }

    /**
     * Define the term to be sorted
     * @return the line attribute.
     */
    @Override
    public String getTerm() {
        return line;
    }
}
