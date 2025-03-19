import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Scanner;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class RMIClient{

  public static void main(String[] args) throws Exception {
    String address = args[0];
    int port = Integer.parseInt(args[1]);

    //Initial connection:
    Registry registryMain = LocateRegistry.getRegistry(address, port);
    CoordinatorStoreInterface remoteObjMain = (CoordinatorStoreInterface)
            registryMain.lookup("Store");

    //Fill the store:
    remoteObjMain.put("hello", "world");
    remoteObjMain.put("one", "two");
    remoteObjMain.put("name", "Joe");
    remoteObjMain.put("major", "computer science");
    remoteObjMain.put("hobby", "skiing");
    remoteObjMain.put("height", "6 feet");
    remoteObjMain.put("weight", "879");
    remoteObjMain.put("fill", "the store");

    //Carry out transactions:
    System.out.println(remoteObjMain.get("hello"));
    System.out.println(remoteObjMain.get("not in the database"));
    System.out.println(remoteObjMain.get("hobby"));

    remoteObjMain.delete("hobby");
    System.out.println(remoteObjMain.get("hobby"));
    remoteObjMain.put("hobby", "snowboarding");
    System.out.println(remoteObjMain.get("hobby"));
    remoteObjMain.put("hobby", "knitting");
    remoteObjMain.delete("major");
    remoteObjMain.delete("nothing here");
    System.out.println(remoteObjMain.get("hobby"));
    remoteObjMain.delete("hello");
    remoteObjMain.delete("one");
    System.out.println(remoteObjMain.get("hello"));
    remoteObjMain.put("hello", "world again");
    System.out.println(remoteObjMain.get("hello"));
    remoteObjMain.delete("name");
    remoteObjMain.delete("name");
    System.out.println(remoteObjMain.get("name"));
    remoteObjMain.put("name", "I'm back");
    System.out.println(remoteObjMain.get("name"));


    //Threaded connection:
    ExecutorService executor = Executors.newFixedThreadPool(4);
    for (int i = 1; i <= 50; i++) {
      int finalI = i;
      executor.submit(() -> {
        try {
          Registry registry = LocateRegistry.getRegistry(address, port);
          CoordinatorStoreInterface remoteObj = (CoordinatorStoreInterface) registry.lookup("Store");
          remoteObj.put("hello" + finalI, "world" + finalI);
          remoteObj.put("hello" + (finalI - 1), "world" + finalI);
          remoteObj.put("hello" + (finalI + 1), "world" + finalI);
          System.out.println(finalI + " - " + remoteObj.get("hello" + finalI));
        } catch (RemoteException | NotBoundException e) {
          e.printStackTrace();
        }
      });
    }
    executor.shutdown();

    //Wait for threads to finish
    executor.awaitTermination(5, TimeUnit.SECONDS);



    //User input:
    Scanner scanner = new Scanner(System.in);
    String input;
    String key;
    String value;
    while(true){
      System.out.println("\nEnter Command (PUT, GET, or DELETE):");
      input = scanner.nextLine();

      switch (input) {
        case "PUT":
          System.out.println("Enter Key:");
          key = scanner.nextLine();
          System.out.println("Enter Value:");
          value = scanner.nextLine();
          remoteObjMain.put(key, value);
          break;
        case "DELETE":
          System.out.println("Enter Key:");
          key = scanner.nextLine();
          remoteObjMain.delete(key);
          break;
        case "GET":
          System.out.println("Enter Key:");
          key = scanner.nextLine();
          System.out.println("Value: " + remoteObjMain.get(key));
          break;
        default:
          System.out.println("Command not recognized!");
          System.out.println("Valid commands: PUT, DELETE, GET");

      }


    }
//
//
//

  }


}
