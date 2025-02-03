import java.io.*;
import java.net.*;
import java.util.ArrayList;

public class TCPClient implements Client{

  public Socket socket;
  public int requestCount;

  public TCPClient(InetAddress address, int port) throws IOException {
    this.socket = new Socket(address, port);
    this.requestCount = 0;
  }

  public void execute(ArrayList<Message> messages) throws IOException {

    PrintWriter out = new PrintWriter(this.socket.getOutputStream(), true);
    BufferedReader in = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));

    for(Message message : messages){
      //Send message
//      out.println(this.requestCount + message);
    }

    out.close();
    in.close();
  }

  public void close() throws IOException {
    this.socket.close();
  }

}
