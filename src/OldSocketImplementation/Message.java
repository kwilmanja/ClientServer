package OldSocketImplementation;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

public class Message {

  public Type type;
  public String arg1;
  public String arg2;

  public Message(Type type, String arg1){
    this.type = type;
    this.arg1 = arg1;
    this.arg2 = "";
  }

  public Message(Type type, String arg1, String arg2){
    this(type, arg1);
    this.arg2 = arg2;
  }

  public byte[] convertToRequestData(int requestID){

    ByteBuffer buffer = ByteBuffer.allocate(16 + arg1.length() + arg2.length());

    buffer.putInt(requestID);
    buffer.putInt(this.type.getCode());
    buffer.putInt(arg1.length());
    buffer.putInt(arg2.length());

    buffer.put(arg1.getBytes(StandardCharsets.UTF_8));
    buffer.put(arg2.getBytes(StandardCharsets.UTF_8));

    return buffer.array();
  }

}
