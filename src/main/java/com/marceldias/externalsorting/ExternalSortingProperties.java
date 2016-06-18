package com.marceldias.externalsorting;

import java.io.IOException;
import java.nio.file.Files;

/**
 * External Sorting Application properties
 *
 * The properties are used to configure the Application
 * Default values:
 *
 * You can modify the default value passing java properties like:
 * -Dfilename=/tmp/otherfile.txt
 */
public enum ExternalSortingProperties {

    /**
     * filename java property
     * default value = input.txt
     */
    FILENAME("filename", "input.txt"),
    /**
     * output java property
     * default value = output.txt
     */
    OUTPUT("output", "output.txt"),
    /**
     * tempFilesDir java property
     * default value = <code>Files.createTempDirectory("externalsorting")</code>
     */
    TEMP_FILES_DIR("tempFilesDir", getTmp() ),

    /**
     * maxTempFilesSize java property
     * default value = 100Mb
     */
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
     * @return {@code System.getProperty(label)} if it is not null or the {@code defaultValue}
     */
    public String value() {
        return System.getProperty(label, defaultValue);
    }

    public String getLabel() {
        return label;
    }

}
