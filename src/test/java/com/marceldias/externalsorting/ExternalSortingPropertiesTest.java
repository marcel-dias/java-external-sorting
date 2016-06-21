package com.marceldias.externalsorting;

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
}
