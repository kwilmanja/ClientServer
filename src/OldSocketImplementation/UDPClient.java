package OldSocketImplementation;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.SocketTimeoutException;

import OldSocketImplementation.Client;

public class UDPClient extends Client {

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

  @Override
  protected void sendData(byte[] requestData) throws IOException {
    DatagramPacket requestPacket;
    requestPacket = new DatagramPacket(requestData, requestData.length, this.address, this.port);
    socket.send(requestPacket);

  }

  @Override
  protected boolean readData(byte[] responseData) throws IOException {
    DatagramPacket responsePacket;
    try{
      responsePacket = new DatagramPacket(responseData, 1024);
      socket.receive(responsePacket);
      return true;
    } catch (SocketTimeoutException e) {
      return false;
    }
  }
//
//  public boolean communicate(byte[] requestData, byte[] responseData) throws IOException {
//    DatagramPacket requestPacket;
//    DatagramPacket responsePacket;
//    try{
//      requestPacket = new DatagramPacket(requestData, requestData.length, this.address, this.port);
//      socket.send(requestPacket);
//
//      responsePacket = new DatagramPacket(responseData, 1024);
//      socket.receive(responsePacket);
//      return true;
//    } catch (SocketTimeoutException e) {
//      return false;
//    }
//  }





}
