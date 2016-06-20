package com.marceldias.externalsorting;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicLong;

public class FileSplitter implements FileHandler {

    private BlockingQueue<String> linesQueue = new ArrayBlockingQueue<>(30);
    private Map<String, File> tempFiles = new ConcurrentHashMap<>();
    private boolean isReaderDone = false;
    private static AtomicLong count = new AtomicLong(0);
    private static Integer NR_WRITER_THREADS = 1;

    public Map<String, File> split() {
        ExecutorService readerPool = Executors.newFixedThreadPool(1);
        Future<Boolean> readerFuture = readerPool.submit(getFileSplitterReader());

        ExecutorService writerPool = Executors.newFixedThreadPool(NR_WRITER_THREADS);
        List<Future> futures = new ArrayList();
        for (int i = 0; i < NR_WRITER_THREADS; i++) {
            futures.add(writerPool.submit(getFileSplitterWriter()));
        }
        readerPool.shutdown();
        writerPool.shutdown();

        waitExecution(readerFuture, futures);
        return tempFiles;
    }

    private void waitExecution(Future<Boolean> readerFuture, List<Future> futures) {
        try {
            setIsReaderDone( readerFuture.get());
            for (Future f : futures) {
                if (!f.isDone()) {
                    f.get();
                }
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }

    private FileSplitterReader getFileSplitterReader() {
        String filename = ExternalSortingProperties.FILENAME.value();
        return new FileSplitterReader(this, filename);
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

    @Override
    public void addLineToQueue(String line) throws InterruptedException {
        linesQueue.put(line);
    }
}
