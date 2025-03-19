import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Optional;

public interface ParticipantStoreInterface  extends Remote {

  boolean prepare(int requestId, Transaction transaction) throws RemoteException;

  void commit(int requestId, boolean shouldCommit) throws RemoteException;

  String get(String key) throws RemoteException;


}
