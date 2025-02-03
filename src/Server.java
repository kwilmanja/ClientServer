import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.Optional;

public class Server{

  public final int PACKET_SIZE = 1024;
  public int port;
  public Store store;

  public Server(int port) throws SocketException {
    this.port = port;
    this.store = new Store();
  }

  public byte[] processData(byte[] requestData, InetAddress address, int port){

    ByteBuffer byteBuffer = ByteBuffer.wrap(requestData);

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


    Logger.logRequest(requestID, address, port, type, arg1, arg2);

    switch(type) {
      case PUT:
        this.store.put(arg1, arg2);
        return createResponseData(requestID, type, 1, "SUCCESS", address, port);
      case GET:
        Optional<String> value = this.store.get(arg1);
        if (value.isPresent()) {
          return createResponseData(requestID, type, 1, value.get(), address, port);
        } else {
          return createResponseData(requestID, type, 0, "Key not found", address, port);
        }
      case DELETE:
        this.store.delete(arg1);
        return createResponseData(requestID, type, 1, "SUCCESS", address, port);
    }
    return null;
  }

    public byte[] createResponseData(int requestID, Type type, int status, String message,
                                     InetAddress address, int port){
      ByteBuffer buffer = ByteBuffer.allocate(16 + message.length());

      buffer.putInt(requestID);
      buffer.putInt(type.getCode());
      buffer.putInt(status);
      buffer.putInt(message.length());

      buffer.put(message.getBytes(StandardCharsets.UTF_8));

      Logger.logResponse(requestID, address, port, type, status, message);

      return buffer.array();
    }

  public void executeUDP() throws IOException {
    DatagramSocket socket = new DatagramSocket(this.port);
    byte[] responseData;
    byte[] requestData = new byte[PACKET_SIZE];


    DatagramPacket requestPacket;
    DatagramPacket responsePacket;

    while (true) {
      //Receive
      requestPacket = new DatagramPacket(requestData, requestData.length);
      socket.receive(requestPacket);

      try{
        //Process
        responseData = processData(requestData,
                requestPacket.getAddress(), requestPacket.getPort());
        //Respond
        responsePacket = new DatagramPacket(responseData, responseData.length,
                requestPacket.getAddress(), requestPacket.getPort());
        socket.send(responsePacket);
      } catch(Exception e){
        Logger.logServerMalformed(requestPacket.getAddress(), requestPacket.getPort(), requestPacket.getLength());
      }


    }
  }

  public void executeTCP() throws IOException{
    ServerSocket ss = new ServerSocket(port);
    while(true) {
      Socket socket = ss.accept();
      byte[] requestData = new byte[PACKET_SIZE];
      byte[] responseData;
      OutputStream outputStream = socket.getOutputStream();
      InputStream inputStream = socket.getInputStream();

      //Receive
      int requestSize;
      while ((requestSize = inputStream.read(requestData)) != -1) {
        try {
          //Process
          responseData = processData(requestData, socket.getInetAddress(), socket.getPort());
          //Respond
          outputStream.write(responseData);
          outputStream.flush();
        } catch(Exception e){
          Logger.logServerMalformed(socket.getInetAddress(), socket.getPort(), requestSize);
        }
      }
    }
  }


  public static void main(String[] args) throws IOException {
    int port = Integer.parseInt(args[0]);
    Server server = new Server(port);
    int useTCP = Integer.parseInt(args[1]);
    if(useTCP == 1){
      server.executeTCP();
    } else{
      server.executeUDP();
    }
  }


}
