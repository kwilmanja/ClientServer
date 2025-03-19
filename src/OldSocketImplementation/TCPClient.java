package OldSocketImplementation;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketTimeoutException;

import OldSocketImplementation.Client;

public class TCPClient extends Client {

  public Socket socket;

  public TCPClient(InetAddress address, int port) throws IOException {
    super(address, port);
    this.socket = new Socket(address, port);
    this.socket.setSoTimeout(this.TIMEOUT_MILLIS);
  }

  @Override
  protected void close() throws IOException {
    this.socket.close();
  }

  @Override
  protected void sendData(byte[] requestData) throws IOException {
    OutputStream outputStream = this.socket.getOutputStream();
    outputStream.write(requestData);
    outputStream.flush();
  }

  @Override
  protected boolean readData(byte[] responseData) throws IOException {
    InputStream inputStream = this.socket.getInputStream();

    try{
      inputStream.read(responseData);
      return true;
    } catch(SocketTimeoutException e) {
      return false;
    }
  }

//  public boolean communicate(byte[] requestData, byte[] responseData) throws IOException {
//    OutputStream outputStream = this.socket.getOutputStream();
//    InputStream inputStream = this.socket.getInputStream();
//    try{
//      outputStream.write(requestData);
//      outputStream.flush();
//      inputStream.read(responseData);
//      return true;
//    } catch(SocketTimeoutException e) {
//      return false;
//    }
//  }



}
