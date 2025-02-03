import java.io.IOException;
import java.net.InetAddress;
import java.net.SocketException;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

public abstract class Client{

  public final int TIMEOUT_MILLIS = 3000;
  public InetAddress address;
  public int port;
  public int requestCount;

  public Client(InetAddress address, int port) throws SocketException {
    this.address = address;
    this.port = port;
    this.requestCount = 0;
  }

  public void execute(ArrayList<Message> messages) throws IOException {

    byte[] requestData;
    byte[] responseData = new byte[1024];

    for(Message m : messages){

      Logger.logRequest(this.requestCount, m.type, m.arg1, m.arg2);

      requestData = m.convertToRequestData(this.requestCount);

      while(true){

        if(communicate(requestData, responseData)){
          try {
            ByteBuffer byteBuffer = ByteBuffer.wrap(responseData);

            int requestID = byteBuffer.getInt();
            if (requestID != this.requestCount) {
              Logger.logClientMalformed(this.requestCount);
              continue;
            }

            Type type = Type.fromCode(byteBuffer.getInt());
            int status = byteBuffer.getInt();
            int messageSize = byteBuffer.getInt();

            byte[] messageData = new byte[messageSize];
            byteBuffer.get(messageData);
            String value = new String(messageData, StandardCharsets.UTF_8);

            Logger.logResponse(requestID, type, status, value);
            break;
          } catch (Exception e){
            Logger.logClientMalformed(this.requestCount);
          }
        } else{
          Logger.logTimeout(this.requestCount, this.TIMEOUT_MILLIS);
          break;
        }

      }

      this.requestCount++;
    }
    close();
  }

  protected abstract void close() throws IOException;

  protected abstract boolean communicate(byte[] requestData, byte[] responseData) throws IOException;


  public static void main(String[] args) throws IOException {

    InetAddress address = InetAddress.getByName(args[0]);
    int port = Integer.parseInt(args[1]);

    Client client;
    int useTCP = Integer.parseInt(args[2]);
    if(useTCP == 1){
      client  = new TCPClient(address, port);
    } else{
      client = new UDPClient(address, port);
    }

    ArrayList<Message> messages = new ArrayList<>();

    messages.add(new Message(Type.PUT, "hello", "world"));
    messages.add(new Message(Type.GET, "hello"));
    messages.add(new Message(Type.DELETE, "hello"));

    client.execute(messages);
  }



}
