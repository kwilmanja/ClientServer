import java.io.*;
import java.net.*;

public class Server{
  public static void main(String[] args) throws IOException{

    int port = Integer.parseInt(args[0]);

    ServerSocket ss = new ServerSocket(port);

    Socket s = ss.accept();
    BufferedReader in = new BufferedReader(new InputStreamReader(s.getInputStream()));
    PrintWriter out = new PrintWriter(s.getOutputStream(), true);

    String word = in.readLine();
    String newWord = fixWord(word);

    out.println(newWord);

    ss.close();
    s.close();
    in.close();
  }

  public static String fixWord(String word){
    String result = "";
    char ch;
    for (int i=0; i<word.length(); i++){

      ch = word.charAt(i);

      if(Character.isUpperCase(ch)){
        ch = Character.toLowerCase(ch);
      } else if (Character.isLowerCase(ch)){
        ch = Character.toUpperCase(ch);
      }

      result = ch + result;
    }

    return result;

  }


}