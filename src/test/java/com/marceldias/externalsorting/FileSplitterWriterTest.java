package com.marceldias.externalsorting;

import org.hamcrest.core.Is;
import org.hamcrest.core.IsEqual;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class FileSplitterWriterTest {

    private FileSplitterWriter fileSplitterWriter;
    private FileSplitter fileSplitter;

    @Before
    public void setUp() {
        fileSplitter = new FileSplitter();
        fileSplitterWriter = new FileSplitterWriter( fileSplitter);
    }

    @After
    public void tearDown() throws IOException {
        String[] testFiles = {"a.txt", "ab.txt"};
        for (String file : testFiles) {
            Files.deleteIfExists(Paths.get(ExternalSortingProperties.TEMP_FILES_DIR.value(), file));
        }
    }

    @Test
    public void testGetFile() {
        String line = "abcdefg";
        File file = fileSplitterWriter.getFile(line);
        Assert.assertThat(file.getName(), IsEqual.equalTo("a.txt"));
    }

    @Test
    public void testGetFileWithPrefixAndHugeSize() {
        System.setProperty(ExternalSortingProperties.MAX_TEMP_FILE_SIZE.getLabel(), "50");
        String line = "abcdefghijklmnoprstuvxyz abcdefghijklmnoprstuvxyz";
        fileSplitterWriter.proccessLine(line);

        File file = fileSplitterWriter.getFile(line);
        Assert.assertThat(file.getName(), IsEqual.equalTo("ab.txt"));
    }

    @Test
    public void testRun() {
        String line = "abcdefghijklmnoprstuvxyz abcdefghijklmnoprstuvxyz";
        String reverseLine = new StringBuilder(line).reverse().toString();
        fileSplitter.setIsReaderDone(Boolean.TRUE);
        try {
            fileSplitter.getLinesQueue().put(line);
            fileSplitter.getLinesQueue().put(reverseLine);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        fileSplitterWriter.run();

        File aFile = new File(ExternalSortingProperties.TEMP_FILES_DIR.value(), "a.txt");
        File zFile = new File(ExternalSortingProperties.TEMP_FILES_DIR.value(), "z.txt");

        Assert.assertThat(aFile.exists(), Is.is(Boolean.TRUE));
        Assert.assertThat(aFile.length(), Is.is(50l));
        Assert.assertThat(zFile.exists(), Is.is(Boolean.TRUE));
        Assert.assertThat(zFile.length(), Is.is(50l));
    }

}
