package com.marceldias.externalsorting;

import com.marceldias.externalsorting.exception.ExternalSortingException;
import org.hamcrest.core.IsEqual;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

public class BufferedReaderWrapperTest {

    private String testFilename = "test.txt";
    private String fileContent = "a\nb\nz\nm\nc\nac\nab";
    private File file;
    private BufferedReader br;
    private BufferedReaderWrapper wrapper;

    @Before
    public void setUp() throws IOException {
        file = writeFile(testFilename, fileContent);
        br = Files.newBufferedReader(file.toPath(), StandardCharsets.UTF_8);
        wrapper = new BufferedReaderWrapper(br);
    }

    @After
    public void tearDown() throws IOException {
        String[] testFiles = {testFilename};
        for (String file : testFiles) {
            Files.deleteIfExists(Paths.get(ExternalSortingProperties.TEMP_FILES_DIR.value(), file));
        }
        if (br != null) {
            br.close();
        }
    }

    @Test
    public void testPeek() throws IOException {
        Assert.assertThat(wrapper.peek(), IsEqual.equalTo("a"));
        Assert.assertThat(wrapper.peek(), IsEqual.equalTo("a"));
    }

    @Test
    public void testPeekAndPoll() throws IOException {
        Assert.assertThat(wrapper.peek(), IsEqual.equalTo("a"));
        Assert.assertThat(wrapper.poll(), IsEqual.equalTo("a"));
        Assert.assertThat(wrapper.peek(), IsEqual.equalTo("b"));
        Assert.assertThat(wrapper.poll(), IsEqual.equalTo("b"));
    }

    @Test
    public void testPoll() throws IOException {
        Assert.assertThat(wrapper.poll(), IsEqual.equalTo("a"));
        Assert.assertThat(wrapper.poll(), IsEqual.equalTo("b"));
        Assert.assertThat(wrapper.poll(), IsEqual.equalTo("z"));
        Assert.assertThat(wrapper.poll(), IsEqual.equalTo("m"));
    }

    @Test
    public void testGetTerm() throws IOException {
        Assert.assertThat(wrapper.peek(), IsEqual.equalTo(wrapper.getTerm()));
        Assert.assertThat(wrapper.getTerm(), IsEqual.equalTo("a"));
        wrapper.poll();
        Assert.assertThat(wrapper.peek(), IsEqual.equalTo(wrapper.getTerm()));
        Assert.assertThat(wrapper.getTerm(), IsEqual.equalTo("b"));
    }

    @Test
    public void testIsEmpty() throws IOException {
        Assert.assertThat(wrapper.isEmpty(), IsEqual.equalTo(Boolean.FALSE));
    }

    @Test
    public void testIsEmptyTrue() throws IOException {
        while (!wrapper.isEmpty()) {
            wrapper.poll();
        }
        Assert.assertThat(wrapper.isEmpty(), IsEqual.equalTo(Boolean.TRUE));
    }

    @Test(expected = ExternalSortingException.class)
    public void testPollAfterClose() throws IOException {
        wrapper.close();
        wrapper.poll();
    }

    @Test
    public void testClose() throws IOException {
        wrapper.close();
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
