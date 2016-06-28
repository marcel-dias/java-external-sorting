package com.marceldias.externalsorting;

import com.marceldias.externalsorting.exception.ExternalSortingException;
import org.hamcrest.core.Is;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class ExternalSortingPropertiesTest {

    @Before
    @After
    public void tearDown() {
        System.setProperty(ExternalSortingProperties.MAX_TEMP_FILE_SIZE.getLabel(),
                ExternalSortingProperties.MAX_TEMP_FILE_SIZE.getDefaultValue());
        System.setProperty(ExternalSortingProperties.FILENAME.getLabel(),
                ExternalSortingProperties.FILENAME.getDefaultValue());
        System.setProperty(ExternalSortingProperties.NR_WRITER_THREADS.getLabel(),
                ExternalSortingProperties.NR_WRITER_THREADS.getDefaultValue());
        System.setProperty(ExternalSortingProperties.MAX_TEMP_FILE_SIZE.getLabel(),
                ExternalSortingProperties.MAX_TEMP_FILE_SIZE.getDefaultValue());
    }

    @Test
    public void testGetDefaultValue() {
        String maxSize = "" + (1024 * 1024 * 10);
        Assert.assertThat(ExternalSortingProperties.MAX_TEMP_FILE_SIZE.value(), Is.is(maxSize));
    }

    @Test
    public void testGetUpdatedValue() {
        String maxSize = "1024" + (1024 * 1024 * 100);
        System.setProperty(ExternalSortingProperties.MAX_TEMP_FILE_SIZE.getLabel(), maxSize);
        Assert.assertThat(ExternalSortingProperties.MAX_TEMP_FILE_SIZE.value(), Is.is(maxSize));
    }

    @Test
    public void testValidValues() {
        System.setProperty(ExternalSortingProperties.FILENAME.getLabel(), "data/input.txt");
        ExternalSortingProperties.FILENAME.isValid();
        ExternalSortingProperties.NR_WRITER_THREADS.isValid();
        ExternalSortingProperties.MAX_TEMP_FILE_SIZE.isValid();
    }

    @Test(expected = ExternalSortingException.class)
    public void testValidateWrongInput() {
        System.setProperty(ExternalSortingProperties.FILENAME.getLabel(), "data/notFound.txt");
        ExternalSortingProperties.FILENAME.isValid();
    }

    @Test(expected = ExternalSortingException.class)
    public void testValidateZeroThreads() {
        System.setProperty(ExternalSortingProperties.NR_WRITER_THREADS.getLabel(), "0");
        ExternalSortingProperties.NR_WRITER_THREADS.isValid();
    }

    @Test(expected = ExternalSortingException.class)
    public void testValidateSmallTempFileSize() {
        System.setProperty(ExternalSortingProperties.MAX_TEMP_FILE_SIZE.getLabel(), "1024");
        ExternalSortingProperties.MAX_TEMP_FILE_SIZE.isValid();
    }
}
