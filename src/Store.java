import java.net.InetAddress;
import java.time.Instant;
import java.util.HashMap;
import java.util.Optional;

public class Store {

  public final HashMap<String, String> store = new HashMap<>();

  public Store(){}

  public void put(String key, String value){
    store.put(key, value);
  }

  public boolean delete(String key){
    if(store.containsKey(key)){
      store.remove(key);
      return true;
    }
    return false;
  }

  public Optional<String> get(String key){
    if(store.containsKey(key)){
      return Optional.of(store.get(key));
    }
    return Optional.empty();
  }

}
