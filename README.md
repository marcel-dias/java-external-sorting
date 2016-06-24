# Java External Sorting

[![Build Status][circle-badge]][circle-link] [![codecov.io][codecov-badge]][codecov-link]

[circle-badge]: https://circleci.com/gh/marceldiass/java-external-sorting.svg?style=svg
[circle-link]:  https://circleci.com/gh/marceldiass/java-external-sorting
[codecov-badge]: https://codecov.io/gh/marceldiass/java-external-sorting/branch/master/graph/badge.svg
[codecov-link]:  https://codecov.io/gh/marceldiass/java-external-sorting


## Steps to develop

* Review MergeSort algorithm
* Learn ExternalSort algorithm
* Development
  * File Reader
  * File Writer
    * Add splitter logic
  * File Sorter
    * Sort in alphabetical order
  * File Merger
  * Measure, refactoring and improvments
  * Create docs


## Docker image

```bash
docker run -ti --rm -v `pwd`:"/home/data" marceldiass/external-sorting
```
