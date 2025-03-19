import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class ParticipantStore extends UnicastRemoteObject implements ParticipantStoreInterface {

  public final ConcurrentHashMap<String, String> store = new ConcurrentHashMap<>();
  public final ConcurrentHashMap<Integer, Transaction> transactionLog = new ConcurrentHashMap<>();
  public final HashSet<String> lockedKeys =  new HashSet<>();


  public ParticipantStore() throws RemoteException {
    super();
  }

  @Override
  public boolean prepare(int requestId, Transaction transaction) throws RemoteException {
    synchronized (lockedKeys) {
      try {
        while (lockedKeys.contains(transaction.getKey())) {
          lockedKeys.wait();
        }
      } catch (InterruptedException e) {
        return false;
      }
      lockedKeys.add(transaction.getKey());
    }

    transactionLog.put(requestId, transaction);
    System.out.println("PREPARE\tRequest: " + requestId + " \t" + transaction);
    return true;
  }

  @Override
  public void commit(int requestId, boolean shouldCommit) {
    Transaction t = transactionLog.get(requestId);
    if(shouldCommit){
      if(t.isDelete()){
        store.remove(t.getKey());
      } else{
        store.put(t.getKey(), t.getValue());
      }
      System.out.println("COMMIT\tRequest: " + requestId + " \t" + t);
    } else{
      System.out.println("ABORT\tRequest: " + requestId + " \t" + t);
    }

    synchronized (lockedKeys){
      lockedKeys.remove(t.getKey());
      lockedKeys.notifyAll();
    }

    transactionLog.remove(requestId);
  }

  @Override
  public String get(String key) throws RemoteException {

    synchronized (lockedKeys) {
      try {
        while (lockedKeys.contains(key)) {
          lockedKeys.wait();
        }
      } catch (InterruptedException ignored) {}
    }

    System.out.println("GET \tKey: " + key);
    synchronized (store) {
      if(store.containsKey(key)){
        return store.get(key);
      }
    }
    return "";
  }

}
