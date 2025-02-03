import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

public class UDPClient implements Client{

  public InetAddress address;
  public int port;
  public DatagramSocket socket;
  public int requestCount;

  public UDPClient(InetAddress address, int port) throws SocketException {
    this.address = address;
    this.port = port;
    this.socket = new DatagramSocket();
    this.requestCount = 0;
  }

  public void execute(ArrayList<Message> messages) throws IOException {

    byte[] requestData = new byte[1024];
    byte[] responseData = new byte[1024];
    DatagramPacket requestPacket;
    DatagramPacket responsePacket;

    for(Message m : messages){

      Logger.logRequest(this.requestCount, m.type, m.arg1, m.arg2);

      requestData = m.convertToRequestData(this.requestCount);
      requestPacket = new DatagramPacket(requestData, requestData.length, this.address, this.port);
      this.socket.send(requestPacket);

      responsePacket = new DatagramPacket(responseData, responseData.length);
      socket.receive(responsePacket);
      ByteBuffer byteBuffer = ByteBuffer.wrap(responsePacket.getData());

      int requestID = byteBuffer.getInt();
      Type type = Type.fromCode(byteBuffer.getInt());
      int status = byteBuffer.getInt();
      int messageSize = byteBuffer.getInt();

      byte[] messageData = new byte[messageSize];
      byteBuffer.get(messageData);
      String value = new String(messageData, StandardCharsets.UTF_8);


      Logger.logResponse(requestID, type, status, value);

      this.requestCount++;
    }

  }

  @Override
  public void close() throws IOException {

  }

}
