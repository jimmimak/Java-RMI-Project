import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.rmi.RemoteException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;

//klash pou antiproswpeuei to parathuro ths efarmoghs
public class ClientFrame extends JFrame{
    
    private String username; //to username pou eisagei o xrhsths gia na sundethei
    private String password; //o kwdikos pou eisagei o xrhsths gia na sundethei
    BingoServerOperations bs_op;
    
    SignInPanel signinpanel;
    GamePanel gamepanel;
    
    ClientFrame(BingoServerOperations bs_op){
        
        super("Bingo");
        this.bs_op = bs_op;
        signinpanel = new SignInPanel(this, bs_op);
        this.add(signinpanel);
        gamepanel = new GamePanel(this, bs_op);
        
        setExtendedState(JFrame.MAXIMIZED_BOTH); //otan trexei h efarmogh jekinaei se fullscreen
         setSize(600,350);//Orizoume to megethos tou parathurou se 600 px platos kai 600 px upsos
         setLocationRelativeTo(null);//Auth h grammh kwdika kentrarei to parathuro sthn othonh
         setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); //Kleinei h efarmogh otan kanoume klik sto koumpi "Kleisimo" ths grammhs titlou
         setVisible(true); //To plaisio na einai orato
         
         //listener pou otan o xrhsths kleinei thn efarmogh apo to x tou parathurou ton afairei apo th lista twn sundedemenwn paiktwn
         this.addWindowListener(new WindowAdapter(){
            @Override
            public void windowClosing(WindowEvent we) {
                String name = "//localhost/OperationClient";
                
                try {
                    bs_op.removePlayer(name);
                } catch (RemoteException ex) {
                    Logger.getLogger(ClientFrame.class.getName()).log(Level.SEVERE, null, ex);
                }
                dispose();
            }
        });
    }
    
    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    
    public String getUsername(){
        return username;
    }
}
