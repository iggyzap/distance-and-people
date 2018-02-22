This Project
------------

It's a command line tool that will import json data into geospatial H2 database and then output results of running a distance search 
    against the db.
    
Composition
-----

Program consists of number of parts:
1. Input parser that produces a stream of data ready to be inserted into DB
1. Database which takes in data from above 
1. A query interface which extracts data from DB
1. Query result processor which outputs results in _some_ format



