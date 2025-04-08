//Dimitrios Makris, 3212019119
//Leonidas Kominos, 3212015092

import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

//klash pou anaparista to "tablo" tou paixnidiou
public class GamePanel extends JPanel implements ActionListener {

    private ArrayList<Integer> bingoNumbers; //lista sthn opoia apothikeuontai oi arithmoi pou exoun epilegei apo ton xrhsth
    private ArrayList<Integer> numbersAlreadyDrawn; //lista sthn opoia apothikeuontai oi arithmoi pou exoun klhrwthei apo ton BingoServer
    private JButton[][] numbers; //pinakas koumpiwn pou perilambanei ta koumpia pou apartizoun to deltio bingo
    private JButton bingoButton; //koumpi pou pataei o paikths gia na elegjei o BingoServer an o paikths ekane bingo h oxi
    private JButton showWinnersButton; //koumpi pou pataei o xrhsths gia na tou emfanistoun ta onomata twn nikhtwn
    private JTextField lotteryNumbersField = new JTextField(); //text field sto opoio emfanizontai oi arithmoi pou klhrwnontai
    private String[] winOptions = {"Play Again", "Exit"}; //epiloges pou tha borei na epilejei o xrhsths otan tou emfanistei to parathuro pou tha tou leei oti kerdise
    private String[] loseOptions = {"Continue", "Exit"}; //epiloges pou tha borei na epilejei o xrhsths otan tou emfanistei to parathuro pou tha tou leei oti den pragmatopoihshe bingo

    ClientFrame cf;
    BingoServerOperations bs_op;
    SignInPanel signinpanel;
    int[][] t; //pinakas me tous arithmous tou deltiou bingo

    GamePanel(ClientFrame cf, BingoServerOperations bs_op) {
        try {
            bingoNumbers = new ArrayList<Integer>();
            numbersAlreadyDrawn = new ArrayList<Integer>();
            numbers = new JButton[6][5];
            bingoButton = new JButton("Bingo!");
            showWinnersButton = new JButton("Display winners");
            this.cf = cf;
            this.bs_op = bs_op;
            GridLayout layout = new GridLayout(8, 5);
            this.setLayout(layout);
            t = bs_op.bingoBoard();
            this.add(new JLabel("B", JLabel.CENTER));
            this.add(new JLabel("I", JLabel.CENTER));
            this.add(new JLabel("N", JLabel.CENTER));
            this.add(new JLabel("G", JLabel.CENTER));
            this.add(new JLabel("O", JLabel.CENTER));
            for (int i = 0; i < 5; i++) {
                for (int j = 0; j < 5; j++) {
                    numbers[i][j] = new JButton(Integer.toString(t[i][j]));
                    this.add(numbers[i][j]);
                    
                    //prosthetoume se ola ta koumpia enan listener gia na ruthmisoume to pws tha antidroun
                    //kathe fora pou o xrhsths tha kanei click epanw tous
                    numbers[i][j].addActionListener(new ActionListener() {
                        public void actionPerformed(ActionEvent e) {
                            JButton tmp = (JButton) e.getSource();
                            GamePanel gp = (GamePanel) tmp.getParent();

                            //an o xrhsths pathsei se ena koumpi pou den einai kokkino (ara den exei epilegei)
                            //tote auto ginetai kokkino (pleon exei epilegei apo ton xrhsth) kai o arithmos pou exei san onoma auto to koumpi
                            //prostithetai sth lista me tous arithmous pou epeleje o xrhsths
                            if (tmp.getBackground() != Color.red) {
                                gp.addBingoNumber(tmp.getText());
                                tmp.setBackground(Color.red);
                            } 
                            //an o xrhsths pathsei se ena koumpi pou einai kokkino (ara exei epilegei)
                            //tote auto pauei na einai kokkino kai pairnei to default xrwma tou (den einai epilegmeno pleon) kai o arithmos pou exei san onoma auto to koumpi
                            //afaireitai apo th lista me tous arithmous pou epeleje o xrhsths
                            else if (tmp.getBackground() == Color.red) {
                                gp.removeBingoNumber(tmp.getText());
                                tmp.setBackground(null);
                            }
                        }
                    });
                }
            }
            
            this.add(new JLabel("")); //keno label gia na gemisei o kenos xwros tou GridLayout
            this.add(new JLabel("")); //keno label gia na gemisei o kenos xwros tou GridLayout

            lotteryNumbersField.setEditable(false); //o xrhsths den mporei na peirajei to text field sto opoio emfanizontai oi arithmoi pou klhrwnontai
            this.add(new JScrollPane(lotteryNumbersField)); //prosthetoume to text field sto opoio emfanizontai oi arithmoi pou klhrwnontai sto panel kai tou dinoume th dunatothta an gemisei na mporei o xrhsths na scrollarei aristera kai dejia se auto
            this.add(new JLabel("")); //keno label gia na gemisei o kenos xwros tou GridLayout
            this.add(new JLabel("")); //keno label gia na gemisei o kenos xwros tou GridLayout
            this.add(new JLabel("")); //keno label gia na gemisei o kenos xwros tou GridLayout

            this.add(showWinnersButton).setBackground(Color.yellow);
            this.add(new JLabel("")); //keno label gia na gemisei o kenos xwros tou GridLayout
            this.add(bingoButton).setBackground(Color.cyan);

            bingoButton.addActionListener(this);

            //an o xrhsths pathsei panw sto katallhlo koumpi tha tou emfanistei ena parathuro me mia lista me ta usernames twn nikhtwn
            showWinnersButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    try {
                        JOptionPane.showMessageDialog(null, cf.bs_op.returnWinners());
                    } catch (IOException | ClassNotFoundException ex) {
                        Logger.getLogger(GamePanel.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            });
        } catch (RemoteException ex) {
            Logger.getLogger(GamePanel.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public ArrayList<Integer> getNumbersAlreadyDrawn() {
        return numbersAlreadyDrawn;
    }

    public JTextField getLotteryNumbersField() {
        return lotteryNumbersField;
    }

    public JButton getBingoButton() {
        return bingoButton;
    }
    
    //o arithmos pou auth h methodos pairnei san orisma prostithetai sth lista me tous arithmous pou exei epilejei o xrhsths
    private void addBingoNumber(String number) {
        bingoNumbers.add(Integer.valueOf(number));
    }

    //o arithmos pou auth h methodos pairnei san orisma afaireitai apo th lista me tous arithmous pou exei epilejei o xrhsths
    private void removeBingoNumber(String number) {
        bingoNumbers.remove(Integer.valueOf(number));
    }

    //action pou ginetai otan o paikths pathsei to koumpi "Bingo!"
    @Override
    public void actionPerformed(ActionEvent e) {
        try {
            //an o paikths exei kanei bingo
            if (cf.bs_op.bingo(t, numbersAlreadyDrawn, bingoNumbers)) {
                //emfanizetai ena parathuro pou ton enhmerwnei oti nikhse
                int choice = JOptionPane.showOptionDialog(null, "You win!", "Success", JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, winOptions, winOptions[0]);
                cf.bs_op.sendWinnerToWinnerServer(cf.signinpanel.getUser()); //to antikeimeno tou paikth pou kerdise stelnetai ston WinnerServer

                //an o paikths epeleje thn epilogh "Play Again" tote stelnetai kainourio deltio bingo ston paikth
                if (choice == 0) {
                    t = bs_op.bingoBoard(); //stelnontai oi kainourioi arithmoi tou deltiou bingo
                    numbersAlreadyDrawn.clear(); //katharizetai h lista me tous arithmous pou exoun hdh klhrwthei
                    bingoNumbers.clear(); //katharizetai h lista me tous arithmous pou exei epilejei o paikths
                    //ta koumpia pairnoun san onoma to kainourio set me arithmous pou esteile o BingoServer
                    //kai pairnoun ola to default xrwma tous
                    for (int i = 0; i < 5; i++) {
                        for (int j = 0; j < 5; j++) {
                            numbers[i][j].setText(Integer.toString(t[i][j]));
                            numbers[i][j].setBackground(null);

                        }
                    }
                    this.lotteryNumbersField.setText(null); //to text field pou perilambanei tous arithmous pou exoun klhrwthei adeiazei ki auto
                } 
                //an o paikths epeleje thn epilogh "Exit", tote bgainei apo thn efarmogh kai afaireitai apo th lista twn sundedemenwn paiktwn
                else if (choice == 1) {
                    String name = "//localhost/OperationClient";

                    try {
                        bs_op.removePlayer(name);
                    } catch (RemoteException ex) {
                        Logger.getLogger(ClientFrame.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    System.exit(0);
                }
            } 
            //an o paikths den exei kanei bingo
            else {
                int choice = JOptionPane.showOptionDialog(null, "No bingo found! How do you want to proceed?", "No bingo found", JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, loseOptions, loseOptions[0]);
                
                //an o paikths epeleje thn epilogh "Exit", tote bgainei apo thn efarmogh kai afaireitai apo th lista twn sundedemenwn paiktwn
                if(choice == 1){
                    String name = "//localhost/OperationClient";

                    try {
                        bs_op.removePlayer(name);
                    } catch (RemoteException ex) {
                        Logger.getLogger(ClientFrame.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    System.exit(0);
                }
            }
        } catch (RemoteException ex) {
            Logger.getLogger(GamePanel.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
