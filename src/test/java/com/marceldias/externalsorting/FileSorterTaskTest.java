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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class FileSorterTaskTest {

    private String testFilename = "test.txt";

    @After
    public void tearDown() throws IOException {
        String[] testFiles = {"a.txt", "z.txt","z-test.txt", "test-z.txt","output.txt", testFilename};
        for (String file : testFiles) {
            Files.deleteIfExists(Paths.get(ExternalSortingProperties.TEMP_FILES_DIR.value(), file));
        }
    }

    @Test
    public void testCall() throws Exception {
        File file = writeFile(testFilename);
        Map<String, File> files = new HashMap<>();
        files.put(testFilename, file);

        ExecutorService sorterPool = Executors.newFixedThreadPool(1);
        Future<Boolean> sorterFuture = sorterPool.submit(new FileSorterTask(file));
        sorterPool.shutdown();

        Assert.assertThat(sorterFuture.get(), Is.is(Boolean.TRUE));
        List<String> orderedContent = readFile(file);
        Assert.assertThat(orderedContent, IsNull.notNullValue());
        Assert.assertThat(orderedContent.size(), Is.is(7));
        Assert.assertThat(orderedContent, IsIterableContainingInOrder.contains("a", "ab", "ac", "b", "c", "m", "z"));
    }

    private List<String> readFile(File file) {
        TestQueueHandler qh = new TestQueueHandler();
        new FileReader(qh, file.getAbsolutePath()).execute();
        return qh.getQueue();
    }

    private File writeFile(String filename) {
        String fileContent = "a\nb\nz\nm\nc\nac\nab";

        String tempFilesDir = ExternalSortingProperties.TEMP_FILES_DIR.value();
        File file = Paths.get(tempFilesDir, filename).toFile();
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
