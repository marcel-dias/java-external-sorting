package com.marceldias.externalsorting;

import com.marceldias.externalsorting.exception.ExternalSortingException;
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

public class AlphabeticalOrderValidatorTest {

    private String testFilename = "unsorted.txt";
    private String testFilenameSorted = "sorted.txt";
    private String fileContent = "a\nb\nz\nm\nc\nac\nab";
    private String fileContentSorted = "a\nab\nac\nb\nd\ne\nz";
    private File sorted;
    private File unsorted;

    @Before
    public void setUp() throws IOException {
        unsorted = writeFile(testFilename, fileContent);
        sorted = writeFile(testFilenameSorted, fileContentSorted);
    }

    @After
    public void tearDown() throws IOException {
        String[] testFiles = {testFilename, testFilenameSorted};
        for (String file : testFiles) {
            Files.deleteIfExists(Paths.get(ExternalSortingProperties.TEMP_FILES_DIR.value(), file));
        }
    }

    @Test
    public void testValidateTrue() {
        AlphabeticalOrderValidator validator = new AlphabeticalOrderValidator();
        Boolean response = validator.validate(sorted.getAbsolutePath());
        Assert.assertThat(response, IsEqual.equalTo(Boolean.TRUE));
    }

    @Test(expected = ExternalSortingException.class)
    public void testValidateFalse() {
        AlphabeticalOrderValidator validator = new AlphabeticalOrderValidator();
        validator.validate(unsorted.getAbsolutePath());
    }

    @Test(expected = ExternalSortingException.class)
    public void testValidateFileNotFound() {
        File notFound = new File(ExternalSortingProperties.TEMP_FILES_DIR.value(), "notFound.txt");
        AlphabeticalOrderValidator validator = new AlphabeticalOrderValidator();
        validator.validate(notFound.getAbsolutePath());
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
