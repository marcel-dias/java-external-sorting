package com.marceldias.externalsorting;

import org.hamcrest.core.Is;
import org.hamcrest.core.IsEqual;
import org.junit.Assert;
import org.junit.Test;

public class SortedListTest {

    @Test
    public void testAddWithPoll() {
        SortedList<TestSortable> result = new SortedList();
        result.add(new TestSortable("bbbb"));
        result.add(new TestSortable("aaaa"));

        Assert.assertThat(result.size(), Is.is(2));
        Assert.assertThat(result.poll().getTerm(), IsEqual.equalTo("aaaa"));
        Assert.assertThat(result.poll().getTerm(), IsEqual.equalTo("bbbb"));

        result.add(new TestSortable("cccc"));
        result.add(new TestSortable("zzzz"));
        result.add(new TestSortable("aaaa"));
        result.add(new TestSortable("bbbb"));

        Assert.assertThat(result.size(), Is.is(4));
        Assert.assertThat(result.poll().getTerm(), IsEqual.equalTo("aaaa"));
        Assert.assertThat(result.poll().getTerm(), IsEqual.equalTo("bbbb"));
        Assert.assertThat(result.poll().getTerm(), IsEqual.equalTo("cccc"));
        Assert.assertThat(result.poll().getTerm(), IsEqual.equalTo("zzzz"));
    }

    @Test
    public void testAddWithPeek() {
        SortedList<TestSortable> result = new SortedList();
        result.add(new TestSortable("bbbb"));
        result.add(new TestSortable("aaaa"));

        Assert.assertThat(result.size(), Is.is(2));
        Assert.assertThat(result.peek().getTerm(), IsEqual.equalTo("aaaa"));
        Assert.assertThat(result.size(), Is.is(2));
    }
}
