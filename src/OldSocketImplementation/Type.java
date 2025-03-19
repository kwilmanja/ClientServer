package OldSocketImplementation;

public enum Type{
  PUT(0),
  GET(1),
  DELETE(2);

  private final int code;

  Type(int code) {
    this.code = code;
  }

  public int getCode(){
    return this.code;
  }

  public static Type fromCode(int i) {
    for (Type t : Type.values()) {
      if (t.getCode() == i) {
        return t;
      }
    }
    throw new IllegalArgumentException("Invalid OldSocketImplementation.Type Code: " + i);
  }

}
