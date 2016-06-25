package com.marceldias;

import org.hamcrest.core.Is;
import org.hamcrest.core.IsEqual;
import org.hamcrest.core.IsNull;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ExternalSortingApplicationTest {

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
}
