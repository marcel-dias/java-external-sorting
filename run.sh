#!/bin/bash
./clear.sh
 java -server -XX:+TieredCompilation -DtempFilesDir=/tmp/externalsorting -jar ./target/external-sorting.jar
