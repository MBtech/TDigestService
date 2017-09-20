# TDigestService

TDigest is a service for utilizing TDigest remotely. 
The API let's you create a TDigest Server that let your 
TDigest Client create a tdigest, add an element or array of elements and query a percentile from the created tdigest.

In order to use the library. Clone the git repo do `mvn clean install` and then use the library in your programs.

In order to start up the server from command line use 
`mvn package`

and then you can start the server using 
`java -cp <path to tdigest jar> com.tdigestserver.TServer <port>`

Work in progress. Usage examples will be added soon
