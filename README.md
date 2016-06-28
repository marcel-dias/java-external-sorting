# Java External Sorting

[![Build Status][circle-badge]][circle-link] [![codecov.io][codecov-badge]][codecov-link]

[circle-badge]: https://circleci.com/gh/marceldiass/java-external-sorting.svg?style=svg
[circle-link]:  https://circleci.com/gh/marceldiass/java-external-sorting
[codecov-badge]: https://codecov.io/gh/marceldiass/java-external-sorting/branch/master/graph/badge.svg
[codecov-link]:  https://codecov.io/gh/marceldiass/java-external-sorting

This project is a Java external sorting algorithm implementation. It sorts the input file content into an output file where all the lines are sorted in alphabetic order with case insensitive.

## External sorting
> External sorting is a term for a class of sorting algorithms that can handle massive amounts of data. External sorting is required when the data being sorted do not fit into the main memory of a computing device (usually RAM) and instead they must reside in the slower external memory (usually a hard drive)
> Source: [External sorting](https://en.wikipedia.org/wiki/External_sorting)

The main idea of external sorting algorithms is to split the original file into smaller ones that fits in memory then apply a sorting algorithm and merge these sorted files back.

## This implementation approach
This implementation uses the character sequence of line content to identify the content of the temporary file that will be be sorted. The default maximum size of a temporary file is 10Mb.
And combines [MergeSort](https://en.wikipedia.org/wiki/Merge_sort) and [K-way Merging](https://en.wikipedia.org/wiki/Merge_algorithm#K-way_merging) to sort data. Then merge all temporary files.

#### Why split the original file in that way?
Because we can keep a filenames list and sort it to make the merge phase easier and faster. And with a 10Mb size we make the sort phase faster and can take advantage of parallel processing to sort multiple temporary files at same time.

#### Why MergeSort?
I chose because it is a divide and conquer algorithm. It is not complex to implement and we can take advantage implementing in a recursive way. It can also be implemented using parallel processing. MergeSort has a good average performance.

#### Why K-way Merging?
The split phase generates temporary files according to the line content and more specialized ones when the precedent get full. **Example:**  ```a.txt``` reach 10 Mb then the split phase start creating ```ab.txt, ac.txt ... az.txt``` Since the ```a.txt``` has lines starting with ```ac, ad, az``` characters it needs to be merged with the others ```a*.txt``` files.
I chose the K-way merge with an [PriorityQueue](https://github.com/marceldiass/java-external-sorting/blob/master/src/main/java/com/marceldias/externalsorting/SortedList.java) implementation to handle the merge of these arbitrary number k of sorted input lists. With that approach is easy to get the head among a huge number of lines.
  
## Execution Flow

![External Sorting Execution Flow][image]

[image]: https://raw.githubusercontent.com/marceldiass/java-external-sorting/master/external-sorting.png

## Docker image
To sort a file  

```bash
❯ docker run -ti --rm -v `pwd`:"/home/data" marceldiass/external-sorting
```

To validate the output file

```bash
❯ docker run -ti --rm -v `pwd`:"/home/data" marceldiass/external-sorting validate
```
> The docker -v parameter mounts a data volume between the host and container. To properly work needs to be a folder inside the user home folder.

## Building

* Java JDK 8
* Maven 3

## Running

```bash
❯ ./run.sh
Running External Sorting!
File Splitter took 1185 ms.
Lines processed: 30000
Number of temp files: 26
File Sorter took 216 ms.
K-way Merge took 0 ms.
Merge Files took 33 ms.
External Sorting App took 1491 ms.
External Sorting successfully executed!
```

## Validating
```bash
❯ ./validate.sh
The file data/output.txt is sorted!
AlphabeticalOrderValidator took 57 ms.
```

## Results in my box
The results below  was sorting a file with 807 Mb and 5000000 lines.
My box has 4 cpu (ht) and SSD.

Heap Size | Thread Number | Temp File Size | Total Time
:-------: | :-----------: | :------------: | :---------:
256 Mb | 8 | 10 Mb | 111 sec
384 Mb | 8 | 15 Mb | 103 sec
256 Mb | 6 | 15 Mb | 115 sec
256 Mb | 3 | 20 Mb | 149 sec

## Steps to develop

* Review MergeSort algorithm
* Learn ExternalSort algorithm
* Learn K-way Merging
* Development
  * File Reader
  * File Writer
     * Add splitter logic
  * File Sorter
     * Sort in alphabetical order
  * File K-way Merger
  * File Merger
  * Measure, refactoring and improvements
  * Create docs


