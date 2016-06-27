package com.marceldias.externalsorting;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicLong;

public class FileSplitter implements QueueHandler {

    private BlockingQueue<String> linesQueue = new ArrayBlockingQueue<>(30);
    private Map<String, File> tempFiles = new ConcurrentHashMap<>();
    private final Set<String> exhaustedTempFiles = ConcurrentHashMap.newKeySet();
    private final Map<String, Set<File>> sameFirstCharFilename = new ConcurrentHashMap<>();
    private boolean isReaderDone = false;
    private TimeMetric timeMetric;
    private static AtomicLong count = new AtomicLong(0);
    private static Integer NR_WRITER_THREADS = Integer.valueOf(ExternalSortingProperties.NR_WRITER_THREADS.value());

    {
        timeMetric = new TimeMetric("File Splitter");
    }

    public Map<String, File> split() {
        String filename = ExternalSortingProperties.FILENAME.value();
        ExecutorService readerPool = Executors.newFixedThreadPool(1);
        Future<Boolean> readerFuture = readerPool.submit(getFileReader(filename));
        readerPool.shutdown();

        ExecutorService writerPool = Executors.newFixedThreadPool(NR_WRITER_THREADS);
        List<Future> futures = new ArrayList();
        for (int i = 0; i < NR_WRITER_THREADS; i++) {
            futures.add(writerPool.submit(getFileSplitterWriter()));
        }
        writerPool.shutdown();

        setIsReaderDone(FutureHelper.waitExecution(readerFuture));
        FutureHelper.waitExecution(futures);
        print();
        return tempFiles;
    }

    protected synchronized void checkSameFirstCharFilename(File file) {
        String firstChar = ""+file.getName().charAt(0);
        Set<File> temp = sameFirstCharFilename.get(firstChar);
        if (temp == null) {
            temp = ConcurrentHashMap.newKeySet();
        }
        temp.add(file);
        sameFirstCharFilename.put(firstChar, temp);
    }

    private void print() {
        timeMetric.print();
        System.out.println("Lines processed: " + getCount().get());
        System.out.println("Number of temp files: " + tempFiles.size());
    }

    private FileReader getFileReader(String filename) {
        return new FileReader(this, filename);
    }

    private FileSplitterWriter getFileSplitterWriter() {
        return new FileSplitterWriter(this);
    }

    public BlockingQueue<String> getLinesQueue() {
        return linesQueue;
    }

    public void setIsReaderDone(boolean isReaderDone) {
        this.isReaderDone = isReaderDone;
    }

    public boolean isReaderDone() {
        return isReaderDone;
    }

    public void increment() {
        count.incrementAndGet();
    }

    public static AtomicLong getCount() {
        return count;
    }

    public void addTempFile(String filename, File file) {
        if (!tempFiles.containsKey(filename)) {
            tempFiles.put(filename, file);
        }
    }

    public Map<String, File> getTempFiles() {
        return tempFiles;
    }

    @Override
    public void addLineToQueue(String line) throws InterruptedException {
        linesQueue.put(line);
    }

    public void addExhaustedFile(String filename) {
        exhaustedTempFiles.add(filename);
    }

    public boolean isFileExhausted(String filename) {
        return exhaustedTempFiles.contains(filename);
    }

    public Map<String, Set<File>> getSameFirstCharFilename() {
        return sameFirstCharFilename;
    }
}
