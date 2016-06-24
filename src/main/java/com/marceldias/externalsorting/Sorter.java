package com.marceldias.externalsorting;

import java.util.LinkedList;

/**
 * Sorter
 *
 * Sort an unsorted list using merge sort algorithm
 *
 */
public class Sorter {

    private LinkedList<String> queue;

    public Sorter(LinkedList<String> queue) {
        this.queue = queue;
    }

    protected Sorter() {
    }

    /**
     * Starts the sorting process
     *
     * @return
     */
    public LinkedList<String> sort() {
        return compare(queue);
    }

    /**
     * Gets an unsorted list and sort it using merge sort algorithm
     *
     * @param list a list to be sorted
     * @return a sorted list
     */
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
        return merge(left, right, list);
    }

    /**
     * Gets left and right ordered lists and merge them into the result list
     *
     * @param left an ordered list
     * @param right an ordered list
     * @param result list where left and right will be merged
     * @return a result list with the content of left and right combined
     */
    protected LinkedList<String> merge(LinkedList<String> left, LinkedList<String> right, LinkedList<String> result) {
        result.clear();
        int totalLength = left.size() + right.size();

        for (int i = 0; i < totalLength; i++) {
            if (right.isEmpty() || (!left.isEmpty() && isLeftPrecedent(left.peekFirst(), right.peekFirst()))) {
                result.offerLast(left.pollFirst());
            } else {
                result.offerLast(right.pollFirst());
            }
        }

        return result;
    }

    /**
     * Compare strings char by char to anwser if left string is precedent or not
     *
     * @param left string
     * @param right string
     * @return @{code true} if left string is alphabetically precedent than right string
     *         and @{code false} if right is precedent
     */
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
}
