import java.net.InetAddress;
import java.time.Instant;

public class Logger {

  public static void log(String message){
    System.out.println(Instant.now() + " - " + message);
  }

  public static void log(String id, String message){
    System.out.println(Instant.now() + " - " + "Request ID: " + id + " - " + message);
  }

  public static void logResponse(int requestID, Type type, int status, String value) {
    System.out.println(Instant.now() + " - "
            + "RESPONSE - "
            + "Request ID: " + requestID + " - "
            + "Type: " + type + " - "
            + "Status: " + status + " - "
            + "Value: " + value);
  }

  public static void logResponse(int requestID, InetAddress address, int port, Type type, int status, String value) {
    System.out.println(Instant.now() + " - "
            + "RESPONSE - "
            + "Destination: " + address + ":" + port + " - "
            + "Request ID: " + requestID + " - "
            + "Type: " + type + " - "
            + "Status: " + status + " - "
            + "Value: " + value);
  }

  public static void logRequest(int requestID, Type type, String arg1, String arg2) {
    System.out.println(Instant.now() + " - "
            + "REQUEST - "
            + "Request ID: " + requestID + " - "
            + "Type: " + type + " - "
            + "Argument 1: " + arg1 + " - "
            + "Argument 2: " + arg2);

  }

  public static void logRequest(int requestID, InetAddress address, int port, Type type, String arg1, String arg2) {
    System.out.println(Instant.now() + " - "
            + "REQUEST - "
            + "Source: " + address + ":" + port + " - "
            + "Request ID: " + requestID + " - "
            + "Type: " + type + " - "
            + "Argument 1: " + arg1 + " - "
            + "Argument 2: " + arg2);
  }

  public static void logMalformedRequest(InetAddress address, int port, int length) {
    System.out.println(Instant.now() + " - "
            + "REQUEST - "
            + "Source: " + address + ":" + port + " - "
            + "Error: Received malformed request of length " + length);
  }
}
