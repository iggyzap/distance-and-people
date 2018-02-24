This Project
------------

It's a command line tool that will import json data into geospatial H2 database and then output results of running a distance search 
    against the db.
This tool consumes standart input as multi-json values, hence it's read behaviour (and wait behaviour) is highly dependent
on how your shell buffers input etc (meaning it will wait for first 8k of input or EOF before starting actual processing).
    
Composition
-----

Program consists of number of parts:
1. Input parser that produces a stream of data ready to be inserted into DB
1. Database which takes in data from above 
1. A query interface which extracts data from DB
1. Query result processor which outputs results in _simple_ format

Project structure
-----

Standard maven project, building will produce runnable jar file in target/distance-people-search-<version>.jar which 
    can be run as command-line utility via
```$bash

cat <customers.json> | java -jar target/distance-people-search-<version>.jar <query parameters>

```

Command - line help can be obtained via invoking 
```$bash
java -jar target/distance-people-search-<version>.jar --help
```

This project also contains code coverage report, which will be located in target/site/jacoco/index.html after project was built with maven

Joke
-------

- What is polar bear ?
- It's cartesian bear after coordinate system transformation.


