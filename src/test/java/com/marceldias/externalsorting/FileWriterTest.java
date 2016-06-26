package com.marceldias.externalsorting;

import org.hamcrest.collection.IsIterableContainingInOrder;
import org.hamcrest.core.Is;
import org.hamcrest.core.IsNull;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class FileWriterTest {

    @After
    public void tearDown() throws IOException {
        String[] testFiles = {"test-a.txt", "test-z.txt","output.txt"};
        for (String file : testFiles) {
            Files.deleteIfExists(Paths.get(ExternalSortingProperties.TEMP_FILES_DIR.value(), file));
        }

        System.setProperty(ExternalSortingProperties.OUTPUT.getLabel(), ExternalSortingProperties.OUTPUT.getDefaultValue());
    }

    @Test
    public void testMergeFiles() {
        List<File> files = new ArrayList();
        files.add( writeFile("test-a.txt", "a\nb\nz\nm"));
        files.add( writeFile("test-z.txt", "c\nd\ne\nf"));

        File output = Paths.get(ExternalSortingProperties.TEMP_FILES_DIR.value(), "output.txt").toFile();
        System.setProperty(ExternalSortingProperties.OUTPUT.getLabel(), output.getAbsolutePath());

        FileWriter.mergeFiles(files);

        List<String> outputContent = readFile(output);
        Assert.assertThat(outputContent, IsNull.notNullValue());
        Assert.assertThat(outputContent.size(), Is.is(8));
        Assert.assertThat(outputContent, IsIterableContainingInOrder.contains("a", "b", "z", "m", "c", "d", "e", "f"));
    }

    @Test
    public void testMove() {
        File source = writeFile("test-a.txt", "a\nb\nz\nm");
        String tempFilesDir = ExternalSortingProperties.TEMP_FILES_DIR.value();
        File destine = Paths.get(tempFilesDir, "test-z.txt").toFile();
        FileWriter.move(source, destine);

        Assert.assertThat(destine.exists(), Is.is(Boolean.TRUE));
        Assert.assertThat(destine.isFile(), Is.is(Boolean.TRUE));

        Assert.assertThat(source.exists(), Is.is(Boolean.FALSE));
    }

    private List<String> readFile(File file) {
        TestQueueHandler qh = new TestQueueHandler();
        new FileReader(qh, file.getAbsolutePath()).execute();
        return qh.getQueue();
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
