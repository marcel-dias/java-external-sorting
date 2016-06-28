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
    TEMP_FILES_DIR="/tmp/externalsorting"
fi
jarfile="external-sorting.jar"
if [ ! -e external-sorting.jar ]; then
    jarfile="target/external-sorting.jar"
fi

#-agentlib:hprof=format=b,file=ext.hprof,cpu=samples,thread=y
java -server -Xmx256m -Xms256m -XX:+TieredCompilation -Dfilename=$FILENAME -DnrWriterThreads=$NR_WRITER_THREADS -Doutput=$OUTPUT -DtempFilesDir=$TEMP_FILES_DIR -jar $jarfile $1
