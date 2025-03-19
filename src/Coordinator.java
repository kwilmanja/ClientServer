import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.sql.Array;
import java.util.ArrayList;

public class Coordinator {

  public static void main(String[] args) {
    try {

      int port = Integer.parseInt(args[0]);

      ArrayList<ParticipantStoreInterface> replicas = new ArrayList<>();
      for(int i=1; i<4; i++){

        Registry registryMain = LocateRegistry.getRegistry("localhost", port + i);
        ParticipantStoreInterface remoteObj = (ParticipantStoreInterface)
                registryMain.lookup("Store");
        replicas.add(remoteObj);
      }

      CoordinatorStoreInterface store = new CoordinatorStore(replicas);

      Registry registry = LocateRegistry.createRegistry(port);
      registry.bind("Store", store);
      System.out.println("Server ready!");

    } catch (Exception e) {
      System.err.println("Error while initializing the store: " + e);
      e.printStackTrace();
    }

  }


}
