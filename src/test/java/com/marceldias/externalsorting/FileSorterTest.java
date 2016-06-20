package com.marceldias.externalsorting;

import org.hamcrest.collection.IsIterableContainingInOrder;
import org.hamcrest.core.Is;
import org.hamcrest.core.IsNull;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

public class FileSorterTest {

    private FileSorter fileSorter;

    @Before
    public void setUp() {
        fileSorter = new FileSorter();
    }

    @After
    public void tearDown() throws IOException {
        String[] testFiles = {"a.txt", "b.txt", "z.txt","test-a.txt", "test-b.txt", "test-z.txt","output.txt"};
        for (String file : testFiles) {
            Files.deleteIfExists(Paths.get(ExternalSortingProperties.TEMP_FILES_DIR.value(), file));
        }
    }

    @Test
    public void testSort() {
        Map<String, File> files = writeFiles();
        String tempFilesDir = ExternalSortingProperties.TEMP_FILES_DIR.value();
        System.setProperty(ExternalSortingProperties.OUTPUT.getLabel(), tempFilesDir + "/output.txt");
        fileSorter = new FileSorter(files);
        fileSorter.sort();

        //read file
        //check content

    }

    @Test
    public void testIsLeftPrecedent() {
        String left = "left";
        String right = "right";
        Boolean isLeftPrecedent = fileSorter.isLeftPrecedent(left, right);

        Assert.assertThat(isLeftPrecedent, Is.is(Boolean.TRUE));
    }

    @Test
    public void testCompare() {
        LinkedList<String> list = new LinkedList<>();
        list.add("right");
        list.add("left");
        list.add("Abcd");
        list.add("zzzzz");
        list.add("abcd");
        LinkedList<String> result = fileSorter.compare(list);

        Assert.assertThat(result, IsNull.notNullValue());
        Assert.assertThat(result.size(), Is.is(5));
        Assert.assertThat(result, IsIterableContainingInOrder.contains("Abcd","abcd","left","right","zzzzz"));
    }

    @Test
    public void testMerge() {
        LinkedList<String> left = new LinkedList<>();
        left.add("left");
        LinkedList<String> right = new LinkedList<>();
        right.add("right");
        LinkedList<String> result = fileSorter.merge(left, right);

        Assert.assertThat(result, IsNull.notNullValue());
        Assert.assertThat(result.size(), Is.is(2));
        Assert.assertThat(result, IsIterableContainingInOrder.contains("left", "right"));
    }

    @Test
    public void testAddLineToQueue() throws InterruptedException {
        String left = "left";
        String right = "right";

        fileSorter.addLineToQueue(left);
        fileSorter.addLineToQueue(right);

        Assert.assertThat(fileSorter.getQueue(), IsNull.notNullValue());
        Assert.assertThat(fileSorter.getQueue().size(), Is.is(2));
        Assert.assertThat(fileSorter.getQueue(), IsIterableContainingInOrder.contains("left", "right"));
    }

    private Map<String, File> writeFiles() {
        String tempFilesDir = ExternalSortingProperties.TEMP_FILES_DIR.value();
        File a = Paths.get(tempFilesDir, "test-a.txt").toFile();
        File b = Paths.get(tempFilesDir, "test-b.txt").toFile();
        File z = Paths.get(tempFilesDir, "test-z.txt").toFile();
        FileWriter.appendLine("abcdefghijklmnoprstuvxy", a);
        FileWriter.appendLine("bcdefghijklmnoprstuvxyz", b);
        FileWriter.appendLine("zyxutabcdefghijklmnoprs", z);
        Map<String, File> files = new HashMap<>(3);
        files.put("test-a", a);
        files.put("test-b", b);
        files.put("test-z", z);
        return files;
    }
}
