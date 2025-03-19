# Project 2

There are two services in the project:

Client: The client connects to the specified server using Java's RMI library.
It locates the registry and looks up the "Store" service, keeping reference to the stub object to handle requests.
Then, it sends a combination of PUT, GET, and DELETE requests. For each request, it awaits a response.
The client tests the threading of the server by instantiating many threads to bombard the server
with requests.

Server: The server operates a store and communicates with a client to carry out PUT, GET, and DELETE requests.
The server creates a registry and binds an CoordinatorStore instance to the registry under the service "Store".
When a client makes an RMI call, the server handles it in a thread and carries out the request.
The store uses a ConcurrentHashMap to handle concurrency issues, as well as synchronized blocks
for methods that reference the map more than once (GET/DELETE).

First, to compile the files:

```
javac RMIServer.java
javac RMIClient.java
```

Then, to run the programs:

```
java RMIServer <port>
java RMIClient <ip> <port>
```
