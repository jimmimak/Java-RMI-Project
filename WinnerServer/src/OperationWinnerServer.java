//Dimitrios Makris, 3212019119
//Leonidas Kominos, 3212015092

import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

//klash pou ulopoiei th diepafh WinnerServerOperations
public class OperationWinnerServer extends UnicastRemoteObject implements WinnerServerOperations {

    private ArrayList<Player> users; //lista pou periexei ta antikeimena twn paiktwn pou exoun kerdisei
    private File winnerfile;

    OperationWinnerServer() throws RemoteException {
        super();
        winnerfile = new File("Winners.dat");
        readWinnersFromFile();
    }

    //methodos pou grafei ena antikeimeno paikth se ena arxeio
    @Override
    public void recordWinnersToFile(Player p) throws RemoteException {
        //to parakatw block einai synchronized etsi wste na apofeuxthoun oi sunthikes antagwnismou pou mporei na prokpsoun
        //ean enas xrhsths zhthsei na dei tous nikhtes kai tautoxrona ginetai h eisagwgh enos nikhth sto arxeio
        synchronized (winnerfile) {
            try {
                ObjectOutputStream output = new ObjectOutputStream((new FileOutputStream(winnerfile)));
                users.add(p); //o nikhths prostithetai sth lista me tous nikhtes
                output.writeObject(users); //h lista me tous nikhtes grafetai sto katallhlo arxeio
                output.close();
            } catch (IOException ex) {
                Logger.getLogger(OperationWinnerServer.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    //methodos pou diabazei to arxeio me tous nikhtes
    private void readWinnersFromFile() throws RemoteException {
        users = new ArrayList<>();
        //to parakatw block einai synchronized etsi wste na apofeuxthoun oi sunthikes antagwnismou pou mporei na prokpsoun
        //ean enas xrhsths zhthsei na dei tous nikhtes kai tautoxrona ginetai h eisagwgh enos nikhth sto arxeio
        synchronized (winnerfile) {
            try {
                if (!winnerfile.createNewFile()) {
                    try (ObjectInputStream input = new ObjectInputStream((new FileInputStream("Winners.dat")))) {
                        users = (ArrayList<Player>) input.readObject();
                    } catch (EOFException ex) {
                    }
                }
            } catch (IOException | ClassNotFoundException ex) {
                Logger.getLogger(OperationWinnerServer.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    //methodos pou epistrefei mia lista me ta antikeimena twn paiktwn pou uparxoun se ena arxeio
    @Override
    public ArrayList<Player> getWinners() throws RemoteException, FileNotFoundException, IOException, ClassNotFoundException {
        return users;
    }
}
