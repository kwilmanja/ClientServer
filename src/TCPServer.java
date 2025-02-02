import java.io.*;
import java.net.*;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TCPServer{


  public static void main(String[] args) throws IOException {

    int port = Integer.parseInt(args[0]);
    ServerSocket ss = new ServerSocket(port);

    Pattern putPattern = Pattern.compile("^(\\d+)-P-(.*)-(.*)$");
    Pattern deletePattern = Pattern.compile("^(\\d+)-D-(.*)$");
    Pattern getPattern = Pattern.compile("^(\\d+)-G-(.*)-(.*)$");
    Matcher matcher;

    while (true) {
      Socket s = ss.accept();
      BufferedReader in = new BufferedReader(new InputStreamReader(s.getInputStream()));
      PrintWriter out = new PrintWriter(s.getOutputStream(), true);
      String line;
      while ((line = in.readLine()) != null) {
        matcher = putPattern.matcher(line);
        if(matcher.matches()){
          System.out.println("number:" + matcher.group(1));
          System.out.println("word 1:" + matcher.group(2));
          System.out.println("word 2:" + matcher.group(3));
        }



      }
      s.close();
      in.close();
    }
  }






}