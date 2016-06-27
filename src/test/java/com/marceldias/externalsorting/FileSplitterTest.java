package com.marceldias.externalsorting;

import org.hamcrest.CoreMatchers;
import org.hamcrest.core.Is;
import org.hamcrest.core.IsNull;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map;
import java.util.Set;

public class FileSplitterTest {

    private FileSplitter fileSplitter;
    private String tempFilesDir = ExternalSortingProperties.TEMP_FILES_DIR.value();
    private String testFilename = "test.txt";

    @Before
    public void setUp() {
        fileSplitter = new FileSplitter();
        System.setProperty(ExternalSortingProperties.MAX_TEMP_FILE_SIZE.getLabel(),
                ExternalSortingProperties.MAX_TEMP_FILE_SIZE.getDefaultValue());
    }

    @After
    public void tearDown() throws IOException {
        String[] testFiles = {"a.txt", "ab.txt", "ac.txt", testFilename};
        for (String file : testFiles) {
            Files.deleteIfExists(Paths.get(ExternalSortingProperties.TEMP_FILES_DIR.value(), file));
        }
        System.setProperty(ExternalSortingProperties.MAX_TEMP_FILE_SIZE.getLabel(),
                ExternalSortingProperties.MAX_TEMP_FILE_SIZE.getDefaultValue());
    }

    @Test
    public void testSplit() {
        writeFile();
        String filename = tempFilesDir + "/" + testFilename;
        System.setProperty(ExternalSortingProperties.FILENAME.getLabel(), filename);

        Map<String, File> tempFiles = fileSplitter.split();

        Assert.assertThat(fileSplitter.getLinesQueue().size(), Is.is(0));
        Assert.assertThat(fileSplitter.isReaderDone(), Is.is(Boolean.TRUE));
        Assert.assertThat(tempFiles, IsNull.notNullValue());
        Assert.assertThat(tempFiles.size(), Is.is(1));
        Assert.assertThat(tempFiles.keySet(), CoreMatchers.hasItem("a"));
    }

    @Test
    public void testSplitWithMoreTempFiles() {
        writeFile();
        String filename = tempFilesDir + "/" + testFilename;
        System.setProperty(ExternalSortingProperties.FILENAME.getLabel(), filename);
        System.setProperty(ExternalSortingProperties.MAX_TEMP_FILE_SIZE.getLabel(), "50");

        Map<String, File> tempFiles = fileSplitter.split();

        Assert.assertThat(fileSplitter.getLinesQueue().size(), Is.is(0));
        Assert.assertThat(fileSplitter.isReaderDone(), Is.is(Boolean.TRUE));
        Assert.assertThat(tempFiles, IsNull.notNullValue());
        Assert.assertThat(tempFiles.size(), Is.is(3));
        Assert.assertThat(tempFiles.keySet(), CoreMatchers.hasItems("a", "ab", "ac"));

        Map<String, Set<File>> sameFirstCharFilename = fileSplitter.getSameFirstCharFilename();
        Assert.assertThat(sameFirstCharFilename.size(), Is.is(1));
        Assert.assertThat(sameFirstCharFilename.keySet(), CoreMatchers.hasItems("a"));
        Assert.assertThat(sameFirstCharFilename.get("a").size(), Is.is(3));
    }

    private void writeFile() {
        String fileContent = "abcdefghijklmnoprstuvxyz abcdefghijklmnoprstuvxyz abc\n" +
                "abc defghi jklmnopr stuvxyz abcdefghijklmnoprstuvxyz abc\n" +
                "acb zyxut defghijklmnoprstuvxyz abcdefghijklmnoprstuvxyz";

        String tempFilesDir = ExternalSortingProperties.TEMP_FILES_DIR.value();
        File file = Paths.get(tempFilesDir, testFilename).toFile();
        try (BufferedWriter bw = new BufferedWriter(new java.io.FileWriter(file, false))) {

            bw.write(fileContent);
            bw.newLine();
            bw.flush();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }

    }
}
