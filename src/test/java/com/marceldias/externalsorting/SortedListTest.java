package com.marceldias.externalsorting;

import org.hamcrest.collection.IsArrayContainingInOrder;
import org.hamcrest.collection.IsIterableContainingInOrder;
import org.hamcrest.core.Is;
import org.hamcrest.core.IsEqual;
import org.junit.Assert;
import org.junit.Test;

public class SortedListTest {

    @Test
    public void testAddWithPoll() {
        SortedList<SortableTest> result = new SortedList();
        result.add(new SortableTest("bbbb"));
        result.add(new SortableTest("aaaa"));

        Assert.assertThat(result.size(), Is.is(2));
        Assert.assertThat(result.poll().getTerm(), IsEqual.equalTo("aaaa"));
        Assert.assertThat(result.poll().getTerm(), IsEqual.equalTo("bbbb"));

        result.add(new SortableTest("cccc"));
        result.add(new SortableTest("zzzz"));
        result.add(new SortableTest("aaaa"));
        result.add(new SortableTest("bbbb"));

        Assert.assertThat(result.size(), Is.is(4));
        Assert.assertThat(result.poll().getTerm(), IsEqual.equalTo("aaaa"));
        Assert.assertThat(result.poll().getTerm(), IsEqual.equalTo("bbbb"));
        Assert.assertThat(result.poll().getTerm(), IsEqual.equalTo("cccc"));
        Assert.assertThat(result.poll().getTerm(), IsEqual.equalTo("zzzz"));
    }

    @Test
    public void testAddWithPeek() {
        SortedList<SortableTest> result = new SortedList();
        result.add(new SortableTest("bbbb"));
        result.add(new SortableTest("aaaa"));

        Assert.assertThat(result.size(), Is.is(2));
        Assert.assertThat(result.peek().getTerm(), IsEqual.equalTo("aaaa"));
        Assert.assertThat(result.size(), Is.is(2));
    }
}
