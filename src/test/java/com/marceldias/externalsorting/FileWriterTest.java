package com.marceldias.externalsorting;

import com.marceldias.externalsorting.exception.ExternalSortingException;
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
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class FileWriterTest {

    @After
    public void tearDown() throws IOException {
        String[] testFiles = {"test-a.txt", "test-b.txt", "test-z.txt","output.txt"};
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

        new FileWriter().mergeFiles(files);

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
        new FileWriter().move(source, destine);

        Assert.assertThat(destine.exists(), Is.is(Boolean.TRUE));
        Assert.assertThat(destine.isFile(), Is.is(Boolean.TRUE));

        Assert.assertThat(source.exists(), Is.is(Boolean.FALSE));
    }

    @Test(expected = ExternalSortingException.class)
    public void testMoveInexistentFile() {
        String tempFilesDir = ExternalSortingProperties.TEMP_FILES_DIR.value();
        File source = Paths.get(tempFilesDir, "source").toFile();
        File destine = Paths.get(tempFilesDir, "destine").toFile();
        new FileWriter().move(source, destine);
    }

    @Test
    public void testDelete() {
        File toDelete = writeFile("test-a.txt", "a\nb\nz\nm");
        File toDelete2 = writeFile("test-b.txt", "a\nb\nz\nm");
        File toDelete3 = writeFile("test-z.txt", "a\nb\nz\nm");
        Set<File> files = new HashSet<>(3);
        files.add(toDelete);
        files.add(toDelete2);
        files.add(toDelete3);

        new FileWriter().delete(files);

        Assert.assertThat(toDelete.exists(),  Is.is(Boolean.FALSE));
        Assert.assertThat(toDelete2.isFile(), Is.is(Boolean.FALSE));
        Assert.assertThat(toDelete3.exists(), Is.is(Boolean.FALSE));
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
