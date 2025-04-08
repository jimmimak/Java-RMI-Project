import java.rmi.Naming;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class WinnerServer {

    public static void main(String[] args) {
        try {
            OperationWinnerServer oServer = new OperationWinnerServer();
            Registry reg = LocateRegistry.createRegistry(52369);
            String url = "rmi://localhost" + ":52369/WinnerS";
            Naming.rebind(url, oServer);
            System.out.println("Winner server up and running....");
        } catch (Exception e) {
            System.out.println("Remote error- " + e);
        }
    }

}
