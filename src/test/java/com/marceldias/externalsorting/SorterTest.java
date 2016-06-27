package com.marceldias.externalsorting;

import org.hamcrest.collection.IsIterableContainingInOrder;
import org.hamcrest.core.Is;
import org.hamcrest.core.IsNull;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.LinkedList;

public class SorterTest {

    private Sorter sorter;

    @Before
    public void setUp() {
        sorter = new Sorter();
    }

    @After
    public void tearDown() throws IOException {
        String[] testFiles = {"a.txt", "b.txt", "z.txt","test-a.txt", "test-b.txt", "test-z.txt","output.txt"};
        for (String file : testFiles) {
            Files.deleteIfExists(Paths.get(ExternalSortingProperties.TEMP_FILES_DIR.value(), file));
        }
    }

    @Test
    public void testSort() throws Exception {
        LinkedList<String> list = new LinkedList<>();
        list.add("right");
        list.add("left");
        list.add("Abcd");
        list.add("zzzzz");
        list.add("abcd");
        sorter = new Sorter();
        LinkedList<String> result = sorter.sort(list);

        Assert.assertThat(result, IsNull.notNullValue());
        Assert.assertThat(result.size(), Is.is(5));
        Assert.assertThat(result, IsIterableContainingInOrder.contains("Abcd","abcd","left","right","zzzzz"));
    }

    @Test
    public void testIsLeftPrecedent() {
        String left = "left";
        String right = "right";
        Boolean isLeftPrecedent = sorter.isLeftPrecedent(left, right);
        Assert.assertThat(isLeftPrecedent, Is.is(Boolean.TRUE));
    }

    @Test
    public void testFalseIsLeftPrecedent() {
        String left = "zyxabcdxpto";
        String right = "zyxabcdxpta";
        Boolean isLeftPrecedent = sorter.isLeftPrecedent(left, right);
        Assert.assertThat(isLeftPrecedent, Is.is(Boolean.FALSE));
    }

    @Test
    public void testCompare() {
        LinkedList<String> list = new LinkedList<>();
        list.add("right");
        list.add("left");
        list.add("Abcd");
        list.add("zzzzz");
        list.add("abcd");
        LinkedList<String> result = sorter.compare(list);

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
        LinkedList<String> result = new LinkedList<>();
        result = sorter.merge(left, right, result);

        Assert.assertThat(result, IsNull.notNullValue());
        Assert.assertThat(result.size(), Is.is(2));
        Assert.assertThat(result, IsIterableContainingInOrder.contains("left", "right"));
    }
}
