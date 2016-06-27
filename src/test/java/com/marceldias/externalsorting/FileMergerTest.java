package com.marceldias.externalsorting;

import org.hamcrest.collection.IsIterableContainingInOrder;
import org.hamcrest.core.Is;
import org.hamcrest.core.IsEqual;
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
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class FileMergerTest {

    @After
    public void tearDown() throws IOException {
        String[] testFiles = {"a.txt", "ab.txt", "ac.txt", "z.txt"};
        for (String file : testFiles) {
            Files.deleteIfExists(Paths.get(ExternalSortingProperties.TEMP_FILES_DIR.value(), file));
        }
    }

    @Test
    public void testDoMerge() {
        File a = writeFile("a.txt", "aaaaa\nabccc\nazyyy");
        File ab = writeFile("ab.txt", "abaaa\nabddd");
        File ac = writeFile("ac.txt", "acaaa\nacfff");
        File z = writeFile("z.txt", "zazc");

        List<String> sortedFiles = new ArrayList<>(4);
        sortedFiles.add("a");
        sortedFiles.add("ab");
        sortedFiles.add("ac");
        sortedFiles.add("z");

        Set<File> files = new HashSet<>(3);
        files.add(a);
        files.add(ab);
        files.add(ac);
        Set<File> zfiles = new HashSet<>(1);
        zfiles.add(z);

        Map<String, Set<File>> filesToMerge = new HashMap<>(3);
        filesToMerge.put("a", files);
        filesToMerge.put("z", zfiles);

        FileMerger merger = new FileMerger();
        sortedFiles = merger.doMerge(sortedFiles, filesToMerge);

        Assert.assertThat(sortedFiles, IsNull.notNullValue());
        Assert.assertThat(sortedFiles.size(), Is.is(2));

        List<String> orderedContent = readFile(a);
        System.out.println(orderedContent);
        Assert.assertThat(orderedContent, IsNull.notNullValue());
        Assert.assertThat(orderedContent.isEmpty(), IsEqual.equalTo(Boolean.FALSE));
        Assert.assertThat(orderedContent.size(), Is.is(7));
        Assert.assertThat(orderedContent, IsIterableContainingInOrder.contains("aaaaa", "abaaa", "abccc", "abddd",
                "acaaa", "acfff", "azyyy"));
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
