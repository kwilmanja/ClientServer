

public class Server{
  public static void main(String[] args){
    System.out.println("Server");

    int port = Integer.parse(args);

    ServerSocket ss = new ServerSocket(port);

    Socket s = ss.accept();
    BufferedReader in = new BufferedReader(new InputStreamReader(s.getInputStream()));

    String word = in.readLine();

  }


}