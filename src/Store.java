import java.time.Instant;
import java.util.HashMap;

public class Store {

  public final HashMap<String, String> store = new HashMap<>();


  public void put(String key, String value){
    store.put(key, value);
  }

  public void delete(String key){
    store.remove(key);
  }

  public String get(String key){
    return store.get(key);
  }

  public void log(String message){
    System.out.println(Instant.now() + " - " + message);
  }

}
