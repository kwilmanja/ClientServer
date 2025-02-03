import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.util.ArrayList;

public interface Client {

  void execute(ArrayList<Message> messages) throws IOException;

  void close() throws IOException;

  public static void main(String[] args) throws IOException {

    InetAddress address = InetAddress.getByName(args[0]);
    int port = Integer.parseInt(args[1]);

    Client client;

//    if(args[2].equals("0")){
//      client = new TCPClient(address, port);
//    } else{
    client = new UDPClient(address, port);
//    }

    ArrayList<Message> messages = new ArrayList<>();

    messages.add(new Message(Type.PUT, "hello", "world"));
    messages.add(new Message(Type.GET, "hello"));
    messages.add(new Message(Type.DELETE, "hello"));

    client.execute(messages);
    client.close();
  }

}
