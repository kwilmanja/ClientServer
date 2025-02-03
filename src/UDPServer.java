import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.Optional;

public class UDPServer implements Server{

  public DatagramSocket socket;

  public UDPServer(int port) throws SocketException {
    this.socket = new DatagramSocket(port);
  }

  public void execute() throws IOException {

    byte[] requestData = new byte[1024];
    byte[] responseData = new byte[1024];
    DatagramPacket requestPacket;
    DatagramPacket responsePacket;

    Store store = new Store();

    while (true) {
       requestPacket = new DatagramPacket(requestData, requestData.length);
       socket.receive(requestPacket);
        ByteBuffer byteBuffer = ByteBuffer.wrap(requestPacket.getData());

        int requestID = byteBuffer.getInt();
        Type type = Type.fromCode(byteBuffer.getInt());
        int arg1Size = byteBuffer.getInt();
        int arg2Size = byteBuffer.getInt();

        byte[] arg1Data = new byte[arg1Size];
        byteBuffer.get(arg1Data);
        String arg1 = new String(arg1Data, StandardCharsets.UTF_8);

        byte[] arg2Data = new byte[arg2Size];
        byteBuffer.get(arg2Data);
        String arg2 = new String(arg2Data, StandardCharsets.UTF_8);

        Logger.logRequest(requestID, requestPacket.getAddress(), requestPacket.getPort(), type, arg1, arg2);

        switch(type){
          case PUT:
            store.put(arg1, arg2);
            responseData = createResponseData(requestID, type, 1, "SUCCESS", requestPacket);
            break;
          case GET:
            Optional<String> value = store.get(arg1);
            if(value.isPresent()){
              responseData = createResponseData(requestID, type, 1, value.get(), requestPacket);
            } else{
              responseData = createResponseData(requestID, type, 0, "Key not found", requestPacket);
            }
            break;
          case DELETE:
            store.delete(arg1);
            responseData = createResponseData(requestID, type, 1, "SUCCESS", requestPacket);
            break;
        }

        responsePacket = new DatagramPacket(responseData, responseData.length,
                requestPacket.getAddress(), requestPacket.getPort());
        this.socket.send(responsePacket);

      }
    }

    public byte[] createResponseData(int requestID, Type type, int status, String message,
                                     DatagramPacket requestPacket){
      ByteBuffer buffer = ByteBuffer.allocate(16 + message.length());

      buffer.putInt(requestID);
      buffer.putInt(type.getCode());
      buffer.putInt(status);
      buffer.putInt(message.length());

      buffer.put(message.getBytes(StandardCharsets.UTF_8));

      Logger.logResponse(requestID, requestPacket.getAddress(), requestPacket.getPort(), type, status, message);

      return buffer.array();
    }


}
