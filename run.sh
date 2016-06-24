#!/usr/bin/env bash
if [ "x$FILENAME" = "x" ]; then
    FILENAME="data/input.txt"
fi
if [ "x$OUTPUT" = "x" ]; then
    OUTPUT="data/output.txt"
fi
if [ "x$NR_WRITER_THREADS" = "x" ]; then
    NR_WRITER_THREADS="8"
fi
if [ "x$TEMP_FILES_DIR" = "x" ]; then
    TEMP_FILES_DIR="/tmp/"
fi
jarfile=$(ls -1t external-sorting.jar | head -n 1)
if [ "x$jarfile" = "x" ]; then
    jarfile="target/external-sorting.jar"
fi

#-agentlib:hprof=format=b,file=ext.hprof,cpu=samples,thread=y
java -server -Xmx256m -Xms256m -XX:+TieredCompilation -Dfilename=$FILENAME -DnrWriterThreads=$NR_WRITER_THREADS -Doutput=$OUTPUT -DtempFilesDir=$TEMP_FILES_DIR -jar $jarfile
