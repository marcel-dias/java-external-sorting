package com.marceldias.externalsorting;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicLong;

public class FileSplitter {

    private BlockingQueue<String> linesQueue = new ArrayBlockingQueue<>(30);
    private Map<String, File> tempFiles = new ConcurrentHashMap<>();
    private boolean isReaderDone = false;
    private static AtomicLong count = new AtomicLong(0);
    private static Integer NR_WRITER_THREADS = 1;

    public Map<String, File> split() {
        ExecutorService readerPool = Executors.newFixedThreadPool(1);
        List<Future> futures = new ArrayList();
        futures.add(readerPool.submit(getFileReader()));
        // called
        // create a pool of consumer threads to parse the lines read
        ExecutorService writerPool = Executors.newFixedThreadPool(NR_WRITER_THREADS);
        for (int i = 0; i < NR_WRITER_THREADS; i++) {
            futures.add(writerPool.submit(getFileWriter()));
        }
        readerPool.shutdown();
        writerPool.shutdown();
        for (Future f : futures) {
            try {
                if (!f.isDone()) {
                    f.get();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }
//        while (!readerPool.isTerminated() && !writerPool.isTerminated()) {
//        }
        return tempFiles;
    }

    protected FileReader getFileReader() {
        return new FileReader(this);
    }

    protected FileWriter getFileWriter() {
        return new FileWriter(this);
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

    public void addTempFile(String filename, File file) {
        if (!tempFiles.containsKey(filename)) {
            tempFiles.put(filename, file);
        }
    }

    protected Map<String, File> getTempFiles() {
        return tempFiles;
    }
}
