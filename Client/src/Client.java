import java.rmi.*;

public class Client {

    public static void main(String[] args) {

        try {
            String name = "//localhost/OperationBingoServer";
            BingoServerOperations look_op = (BingoServerOperations) Naming.lookup(name);
            ClientFrame cf = new ClientFrame(look_op);
        } catch (Exception e) {
            System.out.println("Client error: " + e);
            System.exit(1);
        }
    }
}
