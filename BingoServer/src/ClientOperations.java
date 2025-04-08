//H RMI diepafh pou ulopoiei o Client
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ClientOperations extends Remote{
    //methodos pou prosthetei tou arithmous pou klhrwnontai sto text field katw apo to deltio bingo
    public void addLotteryNumber(int number) throws RemoteException;
    
    //methodos pou adeiazei to text field kai thn lista me tous arithmous pou klhrwthikan
    public void newLotteryNumbers() throws RemoteException;
}
