import java.io.IOException;
import java.net.InetAddress;
import java.util.ArrayList;

public interface Server {


  void execute() throws IOException;

  public static void main(String[] args) throws IOException {

    int port = Integer.parseInt(args[0]);
    Server server;

//    if(args[2].equals("0")){
//      client = new TCPClient(address, port);
//    } else{
    server = new UDPServer(port);
//    }

    server.execute();
  }

}
