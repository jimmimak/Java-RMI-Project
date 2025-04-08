import java.rmi.*;
import java.rmi.registry.Registry;

public class BingoServer {

    public static void main(String[] args) {
        try {
            String name = "rmi://localhost" + ":52369/WinnerS";
            WinnerServerOperations look_op = (WinnerServerOperations) Naming.lookup(name);
            Registry r = java.rmi.registry.LocateRegistry.createRegistry(1099);
            OperationBingoServer server = new OperationBingoServer(look_op);
            Naming.rebind("//localhost/OperationBingoServer", server);
            System.out.println("Bingo server up and running....");
        } catch (Exception e) {
            System.out.println("Server not connected: " + e);
            System.exit(1);
        }
    }

}
