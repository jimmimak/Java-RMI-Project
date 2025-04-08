//H RMI diepafh pou ulopoiei o BingoServer
import java.io.IOException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;

public interface BingoServerOperations extends Remote {
    //methodos gia thn eggrafh tou xrhsth sthn efarmogh
    public boolean userRegistration(String username,String password) throws RemoteException, IOException;
    
    //methodos gia thn sundesh tou xrhsth sthn efarmogh
    public boolean userLogin(String username,String password) throws RemoteException, IOException;
    
    //methodos pou dhmiourgei to deltio bingo
    public int[][] bingoBoard()throws RemoteException;
    
    //methodos pou elegxei an enas paikths ekane bingo h oxi
    public boolean bingo(int t[][], ArrayList<Integer> numbersAlreadyDrawn,ArrayList<Integer> bingoNumbers) throws RemoteException;
    
    //methodos pou prosthetei tous paiktes pou einai sundedemenoi sthn efarmogh se mia lista
    public void addPlayer(String name) throws RemoteException;
    
    //methodos pou afairei tous paiktes pou kleinoun thn efarmogh apo th lista twn sundedemenwn paiktwn
    public void removePlayer(String name) throws RemoteException;
    
    //methodos pou stelnei enan paikth pou ekane bingo apo ton Client ston BingoServer kai apo ton BingoServer ston WinnerServer.
    //Auto sumbainei afou o Client den epikoinwnei ap eutheias me ton WinnerServer
    public void sendWinnerToWinnerServer(Player p) throws RemoteException;
    
    //methodos pou epistrefei ta onomata twn nikhtwn apo to arxeio antikeimenwn pou autoi briskontai ston WinnerServer
    public ArrayList<String> returnWinners() throws RemoteException, IOException, ClassNotFoundException;
}
