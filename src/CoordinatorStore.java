import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.sql.Array;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Optional;
import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class CoordinatorStore extends UnicastRemoteObject implements CoordinatorStoreInterface {

  public ArrayList<ParticipantStoreInterface> replicas;
  public int requestIdCounter;
  public final Random rand = new Random();
  public final ExecutorService executor = Executors.newCachedThreadPool();

  public CoordinatorStore(ArrayList<ParticipantStoreInterface> replicas) throws RemoteException {
    super();
    this.replicas = replicas;
    this.requestIdCounter = 0;
  }

  @Override
  public boolean put(String key, String value) throws RemoteException {
    boolean success = writeTransaction(new Transaction(key, value));
    System.out.println(Instant.now() + "\tThread:" + Thread.currentThread().getName() + "   \tSuccess: " + success + " Action: put(" + key + ", " + value + ")");
    return success;
  }

  @Override
  public boolean delete(String key) throws RemoteException {
    boolean success = writeTransaction(new Transaction(key));
    System.out.println(Instant.now() + "\tThread:" + Thread.currentThread().getName() + "   \tSuccess: " + success + " Action: delete(" + key +")");
    return success;
  }

  private boolean writeTransaction(Transaction transaction) throws RemoteException {

    int requestId = this.getRequestId();

    ArrayList<Future<Boolean>> results = new ArrayList<>();
    for(ParticipantStoreInterface replica : replicas) {
      Callable<Boolean> task = () -> replica.prepare(requestId, transaction);
      results.add(executor.submit(task));
    }

    try {

      for (Future<Boolean> f : results) {
        if (!f.get(1, TimeUnit.SECONDS)) {
          return this.commit(requestId, false);
        }
      }

    } catch (TimeoutException | InterruptedException | ExecutionException e){
      return this.commit(requestId, false);
    }

    return this.commit(requestId, true);
  }

  public boolean commit(int requestId, boolean shouldCommit) throws RemoteException {

    for(ParticipantStoreInterface replica : replicas) {
      replica.commit(requestId, shouldCommit);
    }
    return shouldCommit;

  }

  public synchronized int getRequestId(){
    this.requestIdCounter++;
    return this.requestIdCounter;
  }

  @Override
  public String get(String key) throws RemoteException {
    try{
      Callable<String> task = () -> replicas.get(rand.nextInt(replicas.size())).get(key);
      Future<String> f = executor.submit(task);
      String value = f.get(3, TimeUnit.SECONDS);
      System.out.println(Instant.now() + "\tThread:" + Thread.currentThread().getName() + "   \tSuccess: true \tAction: get(" + key +")");
      return value;
    } catch (InterruptedException | ExecutionException | TimeoutException e){
      System.out.println(Instant.now() + "\tThread:" + Thread.currentThread().getName() + "   \tSuccess: false \tAction: get(" + key +")");
      return "Failed to retrieve key:" + key;
    }
  }

}
