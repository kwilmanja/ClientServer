import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;

public class Participant {

  public static void main(String[] args) {
    try {

      int port = Integer.parseInt(args[0]);

      ParticipantStoreInterface store = new ParticipantStore();

      Registry registry = LocateRegistry.createRegistry(port);
      registry.bind("Store", store);
      System.out.println("Server ready!");

    } catch (Exception e) {
      System.err.println("Error while initializing the store: " + e);
      e.printStackTrace();
    }

  }




}
