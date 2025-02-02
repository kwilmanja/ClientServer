import java.io.*;
import java.net.*;

public class TCPClient{
  public static void main(String[] args) throws IOException {

    String ip = args[0];
    int port = Integer.parseInt(args[1]);

    Socket s = new Socket(ip, port);
    PrintWriter out = new PrintWriter(s.getOutputStream(), true);
    BufferedReader in = new BufferedReader(new InputStreamReader(s.getInputStream()));

    BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));


    out.println("1-P-hello-world");
    System.out.println(in.readLine());

    in.close();
    out.close();
    s.close();
    reader.close();
  }
}
