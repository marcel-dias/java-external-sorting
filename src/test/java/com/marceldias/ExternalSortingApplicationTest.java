package com.marceldias;

import com.marceldias.externalsorting.AlphabeticalOrderValidator;
import com.marceldias.externalsorting.ExternalSortingProperties;
import com.marceldias.externalsorting.FileReader;
import com.marceldias.externalsorting.TestQueueHandler;
import com.marceldias.externalsorting.exception.ExternalSortingException;
import org.hamcrest.core.Is;
import org.hamcrest.core.IsEqual;
import org.hamcrest.core.IsNull;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ExternalSortingApplicationTest {

    @After
    public void tearDown() {
        System.setProperty(ExternalSortingProperties.FILENAME.getLabel(),
                ExternalSortingProperties.FILENAME.getDefaultValue());
        System.setProperty(ExternalSortingProperties.NR_WRITER_THREADS.getLabel(),
                ExternalSortingProperties.NR_WRITER_THREADS.getDefaultValue());
        System.setProperty(ExternalSortingProperties.MAX_TEMP_FILE_SIZE.getLabel(),
                ExternalSortingProperties.MAX_TEMP_FILE_SIZE.getDefaultValue());
    }

    @Test
    public void testGetOrderedFileList() {
        List<String> orderedNames = new ArrayList<>(3);
        orderedNames.add("a");
        orderedNames.add("b");
        orderedNames.add("z");

        Map<String, File> files = new HashMap<>(3);
        files.put("z", new File("z"));
        files.put("a", new File("a"));
        files.put("b", new File("b"));

        ExternalSortingApplication app = new ExternalSortingApplication();
        List<File> orderedFiles = app.getOrderedFileList(orderedNames, files);

        Assert.assertThat(orderedFiles, IsNull.notNullValue());
        Assert.assertThat(orderedFiles.size(), Is.is(3));
        for (int i = 0; i < 3; i++) {
            Assert.assertThat(orderedFiles.get(i).getName(), IsEqual.equalTo(orderedNames.get(i)));
        }
    }

    @Test
    public void testExecute() {
        System.setProperty(ExternalSortingProperties.FILENAME.getLabel(), "data/input.txt");
        System.setProperty(ExternalSortingProperties.OUTPUT.getLabel(), "data/output.txt");
        ExternalSortingApplication app = new ExternalSortingApplication();
        app.execute();

        File output = new File("data", "output.txt");
        output.deleteOnExit();
        AlphabeticalOrderValidator validator = new AlphabeticalOrderValidator();
        Boolean response = validator.validate(output.getAbsolutePath());
        Assert.assertThat(response, IsEqual.equalTo(Boolean.TRUE));
    }

    @Test
     public void testValidate() {
        System.setProperty(ExternalSortingProperties.FILENAME.getLabel(), "data/input.txt");
        ExternalSortingApplication app = new ExternalSortingApplication();
        app.validate();
    }

    @Test(expected = ExternalSortingException.class)
    public void testValidateWrongInput() {
        System.setProperty(ExternalSortingProperties.FILENAME.getLabel(), "data/notFound.txt");
        ExternalSortingApplication app = new ExternalSortingApplication();
        app.validate();
    }

    @Test(expected = ExternalSortingException.class)
    public void testValidateZeroThreads() {
        System.setProperty(ExternalSortingProperties.FILENAME.getLabel(), "data/input.txt");
        System.setProperty(ExternalSortingProperties.NR_WRITER_THREADS.getLabel(), "0");
        ExternalSortingApplication app = new ExternalSortingApplication();
        app.validate();
    }

    @Test(expected = ExternalSortingException.class)
    public void testValidateSmallTempFileSize() {
        System.setProperty(ExternalSortingProperties.FILENAME.getLabel(), "data/input.txt");
        System.setProperty(ExternalSortingProperties.MAX_TEMP_FILE_SIZE.getLabel(), "1024");
        ExternalSortingApplication app = new ExternalSortingApplication();
        app.validate();
    }

    @Test
    public void testMainMethod() {
        System.setProperty(ExternalSortingProperties.FILENAME.getLabel(), "data/input.txt");
        ExternalSortingApplication.main("");
    }

    @Test
    public void testMainMethodValidate() {
        System.setProperty(ExternalSortingProperties.FILENAME.getLabel(), "data/input.txt");
        ExternalSortingApplication.main("");
        ExternalSortingApplication.main("validate");
    }


}
