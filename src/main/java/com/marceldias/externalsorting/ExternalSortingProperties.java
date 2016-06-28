package com.marceldias.externalsorting;

import com.marceldias.externalsorting.exception.ExternalSortingException;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

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
     * Represents the path to the file with data to be sorted
     * filename java property
     * default value = input.txt
     */
    FILENAME("filename", "input.txt") {
        public void isValid() {
            File input = new File(this.value());
            if (! input.exists()) {
                throw new ExternalSortingException("The file to be sorted do not exist. Path: "+input.getAbsolutePath(),
                        null);
            }
        }
    },
    /**
     * Represents the path to the sorted file
     * output java property
     * default value = output.txt
     */
    OUTPUT("output", "output.txt"){
        public void isValid() {
        }
    },
    /**
     * Represents the path where the temporary files will be saved
     * tempFilesDir java property
     * default value = <code>Files.createTempDirectory("externalsorting")</code>
     */
    TEMP_FILES_DIR("tempFilesDir", getTmp() ){
        public void isValid() {
        }
    },
    /**
     * Represents the maximum size of a temporary file in bytes
     * maxTempFilesSize java property
     * default value = 10Mb
     */
    MAX_TEMP_FILE_SIZE("maxTempFilesSize", "" + (1024 * 1024 * 10)) {
        public void isValid() {
            Integer size = Integer.valueOf(this.value());
            if ( size < (1024 * 1024 * 1)) {
                throw new ExternalSortingException("MAX_TEMP_FILE_SIZE cannot be smaller than 1Mb", null);
            }
            if ( size > (1024 * 1024 * 10)) {
                System.out.println("========================================");
                System.out.println("             WARNING!!");
                System.out.println("To run the external sorting with temporary files larger than 10Mb");
                System.out.println(" you need to estimate the heap size and number of wwriter threads");
                System.out.println(" Tested Examples: ");
                System.out.println("   * HeapSize = 256Mb, Nr Threads = 8, TempFileSize = 10Mb ");
                System.out.println("   * HeapSize = 384Mb, Nr Threads = 8, TempFileSize = 15Mb ");
                System.out.println("   * HeapSize = 256Mb, Nr Threads = 6, TempFileSize = 15Mb ");
                System.out.println("   * HeapSize = 256Mb, Nr Threads = 3, TempFileSize = 20Mb ");
                System.out.println("========================================");
            }
        }
    },
    /**
     * Represents the number of threads used to write temporary files and to execute sort routine
     * nrWriterThreads java property
     * default value = 1
     */
    NR_WRITER_THREADS("nrWriterThreads", "1"){
        public void isValid() {
            if (Integer.valueOf(this.value()) < 1) {
                throw new ExternalSortingException("NR_WRITER_THREADS cannot be less than 1", null);
            }
        }
    };

    private String label;
    private String defaultValue;

    ExternalSortingProperties(String label, String defaultValue) {
        this.label = label;
        this.defaultValue = defaultValue;
    }

    public abstract void isValid();

    private static String getTmp() {
        String tempDir = null;
        try {
            tempDir = Files.createTempDirectory("externalsorting").toString();
        } catch (IOException e) {
            throw new ExternalSortingException("Unable to create temp dir", e);
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

    public String getDefaultValue() {
        return defaultValue;
    }
}
