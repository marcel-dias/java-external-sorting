package com.marceldias.externalsorting;

import java.io.File;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Callable;

public class Sorter implements FileHandler, Callable<Boolean> {

    private File file;
    private LinkedList<String> queue = new LinkedList<>();

    public Sorter(File file) {
        this.file = file;
    }

    public Sorter() {
    }

    @Override
    public Boolean call() throws Exception {
        sort();
        return Boolean.TRUE;
    }

    public void sort() {
        new FileSplitterReader(this, file.getAbsolutePath()).execute();
        //order file content
        queue = compare(queue);
        //append file content to final file
        append();
    }

    private void append() {
        FileWriter.writeLines(queue, file, false);
    }

    protected LinkedList<String> compare(LinkedList<String> list) {

        if (list.size() == 1) {
            return list;
        }
        int listSize = list.size();
        int middle = listSize / 2;
        LinkedList<String> left = new LinkedList<>();
        left.addAll(list.subList(0, middle));
        LinkedList<String> right = new LinkedList<>();
        right.addAll(list.subList(middle, listSize));

        left = compare(left);
        right = compare(right);
        return merge(left, right);
    }

    protected LinkedList<String> merge(LinkedList<String> left, LinkedList<String> right) {
        LinkedList<String> result = new LinkedList<>();
        int totalLength = left.size() + right.size();

//        for (int i = 0; i < a.length; i++) {
//            if (rightIndex >= right.length || (leftIndex < left.length && left[leftIndex] < right[rightIndex])) {
//                a[i] = left[leftIndex];
//                leftIndex++;
//            } else {
//                a[i] = right[rightIndex];
//                rightIndex++;
//            }
//        }

        for (int i = 0; i < totalLength; i++) {
            if (right.isEmpty() || (!left.isEmpty() && isLeftPrecedent(left.peekFirst(), right.peekFirst()))) {
                result.offerLast(left.pollFirst());
            } else {
                result.offerLast(right.pollFirst());
            }
        }

//        while (!left.isEmpty() && !right.isEmpty()) {
//            if (isLeftPrecedent(left.peekFirst(), right.peekFirst())) {
//                result.offerLast(left.pollFirst());
//            } else {
//                result.offerLast(right.pollFirst());
//            }
//        }
//        while (!left.isEmpty()) {
//            result.offerLast(left.pollFirst());
//        }
//        while (!right.isEmpty()) {
//            result.offerLast(right.pollFirst());
//        }
        return result;
    }

    protected boolean isLeftPrecedent(String left, String right) {
        int leftIndex = 0;
        int rightIndex = 0;

        boolean isLeftPrecedent = true;
        while (leftIndex < left.length() && rightIndex < right.length()) {
            if (left.charAt(leftIndex) < right.charAt(rightIndex)) {
                break;
            } else if (left.charAt(leftIndex) > right.charAt(rightIndex)) {
                isLeftPrecedent = false;
                break;
            }
            leftIndex++;
            rightIndex++;
        }
        return isLeftPrecedent;
    }

    public List<String> sortFilenames(Set<String> filenames) {
        LinkedList<String> result = compare(new LinkedList<>(filenames));
        return result;
    }

    @Override
    public void addLineToQueue(String line) throws InterruptedException {
        queue.offer(line);
    }

    protected LinkedList<String> getQueue() {
        return queue;
    }
}
