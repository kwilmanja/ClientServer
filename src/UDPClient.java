import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.nio.ByteBuffer;
import java.util.concurrent.TimeoutException;

public class UDPClient extends Client{

  public DatagramSocket socket;

  public UDPClient(InetAddress address, int port) throws SocketException {
    super(address, port);
    this.socket = new DatagramSocket();
    this.socket.setSoTimeout(this.TIMEOUT_MILLIS);
  }

  @Override
  protected void close() {
    this.socket.close();
  }

  public boolean communicate(byte[] requestData, byte[] responseData) throws IOException {
    DatagramPacket requestPacket;
    DatagramPacket responsePacket;
    try{
      requestPacket = new DatagramPacket(requestData, requestData.length, this.address, this.port);
      socket.send(requestPacket);

      responsePacket = new DatagramPacket(responseData, 1024);
      socket.receive(responsePacket);
      return true;
    } catch (SocketTimeoutException e) {
      return false;
    }
  }



}
