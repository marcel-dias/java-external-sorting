package com.marceldias.externalsorting;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;

public class FileWriter {

    private static final Boolean append = Boolean.TRUE;

    public static void appendLine(String line, File file) {
        try (BufferedWriter bw = new BufferedWriter(new java.io.FileWriter(file, append))) {
            bw.write(line);
            bw.newLine();
            bw.flush();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }
}
