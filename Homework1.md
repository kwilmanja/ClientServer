# Project 1

There are two classes in the project:

OldSocketImplementation.Client: The client connects to the specified server using either TCP or UDP. It then sends over requests to populate the store. 
Then, it sends a combination of PUT, GET, and DELETE requests. For each request, it awaits a response. 
If no response comes in 3 seconds, then it will log the timeout and continue on with the next request. 
If it receives any malformed packets, it will log the error and continue waiting for the expected packet 
to arrive (until another timeout occurs or the correct packet arrives).

OldSocketImplementation.Server: The server operates a store and communicates with a client to carry out PUT, GET, and DELETE requests.
The server can be instantiated with either TCP or UDP as its communication protocol.
Upon receiving a request, the server ends back a response with the requestID, status (1 for OK, 0 for ERROR),
type of request it is answering, and a value. If the server receives malformed packets,
it will not issue a response and log the event.

First, to compile the files:

```
javac OldSocketImplementation.Server.java
javac OldSocketImplementation.Client.java
```

Then, to run the programs:

```
java OldSocketImplementation.Server <port> <useTCP>
java OldSocketImplementation.Client <ip> <port> <useTCP>
```
with the variable useTCP set to 1 if the client/server should use TCP, and 0 if the client/server should use UDP