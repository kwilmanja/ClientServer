

public class Client{
  public static void main(String[] args){
    int port = Integer.parse(args);
    Socket socket = new Socket("127.0.0.1", port);

    InputStream input = socket.getInputStream();
  }
}
