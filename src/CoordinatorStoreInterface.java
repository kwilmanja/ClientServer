import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Optional;
import java.util.concurrent.ExecutionException;

public interface CoordinatorStoreInterface extends Remote {

  boolean put(String key, String value) throws RemoteException;

  boolean delete(String key) throws RemoteException;

  String get(String key) throws RemoteException;

}
