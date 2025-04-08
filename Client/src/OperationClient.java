import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import javax.swing.JTextField;

//klash pou ulopoiei th diepafh ClientOperations
public class OperationClient extends UnicastRemoteObject implements ClientOperations {

    private JTextField lotteryNumbersField; //text field sto opoio emfanizontai oi arithmoi pou klhrwthikan
    private ArrayList<Integer> numbersAlreadyDrawn; //lista sthn opoia apothikeuontai oi arithmoi pou exoun klhrwthei

    public OperationClient(JTextField numbersField, ArrayList<Integer> numbersAlreadyDrawn) throws RemoteException {
        super();
        this.lotteryNumbersField = numbersField;
        this.numbersAlreadyDrawn = numbersAlreadyDrawn;
    }

    //methodos pou prosthetei tou arithmous pou klhrwnontai sto text field katw apo to deltio bingo
    @Override
    public void addLotteryNumber(int number) throws RemoteException {
        //an to text field einai adeio tote emfanise se auto ton 1o arithmo pou klhrwthike
        if (lotteryNumbersField.getText().equals("")) {
            lotteryNumbersField.setText(lotteryNumbersField.getText() + number);
            numbersAlreadyDrawn.add(number);
        } 
        //an to text field den einai adeio tote emfanise se auto ena komma kai dipla ton arithmo pou klhrwthike
        else {
            lotteryNumbersField.setText(lotteryNumbersField.getText() + ", " + number);
            numbersAlreadyDrawn.add(number);
        }

    }

    //methodos pou adeiazei to text field kai thn lista me tous arithmous pou klhrwthikan
    @Override
    public void newLotteryNumbers() throws RemoteException {
        lotteryNumbersField.setText("");
        numbersAlreadyDrawn.clear();
    }
}
