package com.marceldias.externalsorting;

import org.hamcrest.core.Is;
import org.hamcrest.core.IsEqual;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class FileSplitterWriterTest {

    private FileSplitterWriter fileSplitterWriter;
    private FileSplitter fileSplitter;
    private String tempFilesDir = ExternalSortingProperties.TEMP_FILES_DIR.value();
    private String testFilename = "test.txt";

    @Before
    public void setUp() {
        fileSplitter = new FileSplitter();
        fileSplitterWriter = new FileSplitterWriter( fileSplitter);
    }

    @After
    public void tearDown() throws IOException {
        String[] testFiles = {"a.txt", "ab.txt", "abc.txt", "abcd.txt", testFilename};
        for (String file : testFiles) {
            Files.deleteIfExists(Paths.get(ExternalSortingProperties.TEMP_FILES_DIR.value(), file));
        }
        System.setProperty(ExternalSortingProperties.MAX_TEMP_FILE_SIZE.getLabel(),
                ExternalSortingProperties.MAX_TEMP_FILE_SIZE.getDefaultValue());
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

        fileSplitterWriter.call();

        File aFile = new File(ExternalSortingProperties.TEMP_FILES_DIR.value(), "a.txt");
        File zFile = new File(ExternalSortingProperties.TEMP_FILES_DIR.value(), "z.txt");

        Assert.assertThat(aFile.exists(), Is.is(Boolean.TRUE));
        Assert.assertThat(aFile.length(), Is.is(50l));
        Assert.assertThat(zFile.exists(), Is.is(Boolean.TRUE));
        Assert.assertThat(zFile.length(), Is.is(50l));
    }

    @Test
    public void testGetFile() {
        String line = "abcdefg";
        File file = fileSplitterWriter.getFile(line);
        Assert.assertThat(file.getName(), IsEqual.equalTo("a.txt"));
    }

    @Test
    public void testGetFileWithPrefixAndHugeSize() {
        String fileContent = "abcdefghijklmnoprstuvxyz abcdefghijklmnoprstuvxyz abc\n" +
                "abc defghi jklmnopr stuvxyz abcdefghijklmnoprstuvxyz abc\n" +
                "abc zyxut defghijklmnoprstuvxyz abcdefghijklmnoprstuvxyz";
        writeFile(testFilename, fileContent);
        String filename = tempFilesDir + "/" + testFilename;
        System.setProperty(ExternalSortingProperties.FILENAME.getLabel(), filename);
        System.setProperty(ExternalSortingProperties.MAX_TEMP_FILE_SIZE.getLabel(), "50");

        String line = "abcdefghijklmnoprstuvxyz abcdefghijklmnoprstuvxyz";
        fileSplitter.split();
        File file = fileSplitterWriter.getFile(line);

        Assert.assertThat(file.getName(), IsEqual.equalTo("abcd.txt"));
    }

    private File writeFile(String filename, String fileContent) {
        String tempFilesDir = ExternalSortingProperties.TEMP_FILES_DIR.value();
        File file = Paths.get(tempFilesDir, filename).toFile();
        file.deleteOnExit();
        try (BufferedWriter bw = new BufferedWriter(new java.io.FileWriter(file, false))) {

            bw.write(fileContent);
            bw.newLine();
            bw.flush();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
        return file;
    }
}
