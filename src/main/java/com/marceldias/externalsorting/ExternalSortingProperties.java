package com.marceldias.externalsorting;

import java.io.IOException;
import java.nio.file.Files;

/**
 * External Sorting Application properties
 *
 * The properties are used to configure the Application
 * Default values:
 *
 * #FILENAME = input.txt
 * #OUTPUT = output.txt
 * #TEMP_FILES_DIR = /tmp/externalsorting
 * #MAX_TEMP_FILE_SIZE = 100 Mb
 *
 * You can modify the default value passing java properties like:
 * -Dfilename=/tmp/otherfile.txt
 */
public enum ExternalSortingProperties {

    FILENAME("filename", "input.txt"),
    OUTPUT("output", "output.txt"),
    TEMP_FILES_DIR("tempFilesDir", getTmp() ),
    //100 Mb
    MAX_TEMP_FILE_SIZE("maxTempFilesSize", "" + (1024 * 1024 * 100));

    private String label;
    private String defaultValue;

    ExternalSortingProperties(String label, String defaultValue) {
        this.label = label;
        this.defaultValue = defaultValue;
    }

    private static String getTmp() {
        String tempDir = null;
        try {
            tempDir = Files.createTempDirectory("externalsorting").toString();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return tempDir;
    }

    /**
     * Get the property value from the System properties
     * @return system property value or default value
     */
    public String value() {
        return System.getProperty(label, defaultValue);
    }

    public String getLabel() {
        return label;
    }

}
