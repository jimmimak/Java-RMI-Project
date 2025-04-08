//Dimitrios Makris, 3212019119
//Leonidas Kominos, 3212015092

//H RMI diepafh pou ulopoiei o WinnerServer
import java.io.FileNotFoundException;
import java.io.IOException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;

public interface WinnerServerOperations extends Remote {
    //methodos pou grafei ena antikeimeno paikth se ena arxeio
    public void recordWinnersToFile(Player p) throws RemoteException;
    
    //methodos pou epistrefei mia lista me ta antikeimena twn paiktwn pou uparxoun se ena arxeio
    public ArrayList<Player> getWinners() throws RemoteException, FileNotFoundException, IOException, ClassNotFoundException;
}