import java.rmi.*;
import java.util.logging.Level;
import java.util.logging.Logger;

//klash pou gemizei to text field pou perilambanei tous arithmous pou klirwthikan me tuxaious arithmous
public class LotteryNumbersThread extends Thread {

    OperationBingoServer op_bs;

    LotteryNumbersThread(OperationBingoServer op_bs) {
        this.op_bs = op_bs;
    }

    public void run() {
        try {
            while (true) {
                for (int i = 1; i <= 75; i++) {

                    Thread.sleep(10000); //o BingoServer stelnei arithmous ston Client ana 10 deuterolepta
                    int number = op_bs.lotteryNumber(); //sth metablhth number apothikeuetai enas tuxaios arithmos pou paragetai apo thn methodo lotteryNumber()

                    //se kathe sundedemeno xrhsth gemizei to text field ths efarmoghs tou me tous klhrwteous arithmous
                    for (int j = 0; j < op_bs.getPlayers().size(); j++) {
                        op_bs.getPlayers().get(j).addLotteryNumber(number);
                    }
                }

                //afou staloun 75 arithmoi
                Thread.sleep(20000); //meta apo 20 deuterolepta

                //se olous tous sundedemenous xrhstes adeiazei to text field me tous tuxaious arithmous gia na janasteilei kainouria partida me tuxaious arithmous
                for (int j = 0; j < op_bs.getPlayers().size(); j++) {
                    op_bs.getPlayers().get(j).newLotteryNumbers();
                }
                op_bs.getNumbersAlreadyDrawn().clear(); //adeiazei h lista pou periexei tous arithmous pou klhrwthikan gia na gemisei me thn kainouria partida tuxaiwn arithmwn
            }
        } catch (InterruptedException ex) {
            Logger.getLogger(LotteryNumbersThread.class.getName()).log(Level.SEVERE, null, ex);
        } catch (RemoteException ex) {
            Logger.getLogger(OperationBingoServer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
