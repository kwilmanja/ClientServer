# Homework Set 1

There are two classes in the project:

Client: connects to the server, awaits user input, sends first line read from standard input to the server, prints the server's response, closes down sockets and exits

Server: waits for a client to connect, reads input over the socket, reverses the word and switches capitalization, sends new word back over socket, closes down sockets and exits


First, to compile the files:

```
javac Server.java
javac Client.java
```

Then, to run the programs:

```
java Server <port>
java Client <ip> <port>
```