[![Build Status](https://travis-ci.com/st-spring-samples/spring-batch-async-processing.svg?branch=master)](https://travis-ci.com/st-spring-samples/spring-batch-async-processing)
[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=com.sudhirt.practice.batch%3Aspring-batch-async-processing&metric=alert_status)](https://sonarcloud.io/dashboard?id=com.sudhirt.practice.batch%3Aspring-batch-async-processing)
# Spring batch - async processing
Sample application that demonstrates asynchronous processing to improve batch job performance.

## Prerequisites
-  [JDK 11](https://adoptopenjdk.net/releases.html?variant=openjdk11&jvmVariant=hotspot)
-  [Maven 3.x](https://maven.apache.org/download.cgi)
-  [Git client](https://git-scm.com/download)
-  Linux OS (or WSL2 if you are using Windows 10) to run the batch job with actual dataset

## How to start
Clone this repo
```
git clone git@github.com:st-spring-samples/spring-batch-async.git
```

## Functional description
This batch job processes 'New York City Taxi and Limousine Commission (TLC) Trip Record Data' for the month of Jan 2019. More details about this dataset can be found [here](https://registry.opendata.aws/nyc-tlc-trip-records-pds/).

Batch job reads information available in the csv file and writes the same to a database.

## Run unit tests
    mvn clean test

## Run the batch job
Execute [run.sh](./ruh.sh). This script performs below actions
- Download the dataset
- Download H2 database
- Start H2 database
- Execute the batch job to process downloaded dataset

### Working directory
All downloaded file and data for h2 database are stored in working directory. By default, script uses user home directory as working directory. This can be changed by modifying the value of `WORK_DIR` property in [run.sh](./ruh.sh).

### Thread count
To control the number of threads used by batch job, edit `thread.count` value in [run.sh](./ruh.sh).

### Performance observations
During tests on my pc, I observed that increasing thread count upto 4 results in improved performance. But beyond with thread count more than 4, performance improvement is negligible. Depending on number of available cores on your pc/server, your mileage may vary.

## Source formatting
[Spring Java Format](https://github.com/spring-io/spring-javaformat) plugin is used in this sample for source code formatting. If changes are made to source code, run `mvn spring-javaformat:apply` to reformat code.