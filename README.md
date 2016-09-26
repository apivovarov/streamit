# streamit

## Build the project
```
sbt package
```

## Start MockServer
```
bin/start-server.sh [port, 7799 default]
```
The server will print numbers to a socket every 100ms

## Start spark streaming
```
bin/start-spark.sh
```

Spark will get numbers from the socket and print sum of positive and negative numbers every 10 sec
