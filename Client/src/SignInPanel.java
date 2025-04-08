//Dimitrios Makris, 3212019119
//Leonidas Kominos, 3212015092

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

//klash pou anaparista to arxiko parathuro sto opoio o xrhsths kanei sign in h sign up
public class SignInPanel extends JPanel {

    private JPanel panel; //panel pou perilambanei 6 grammes kai 2 sthles apo labels kai textfields
    private JPanel row1;
    private JPanel row2;
    private JPanel row3;
    private JPanel row4;
    private JPanel row5;
    private JTextField username;
    private JPasswordField password;
    private JLabel label1;
    private JLabel label2;
    private JLabel label3;
    private JButton signInButton;
    private JButton signUpButton;
    private JCheckBox showPassword;
    Player user;
    ClientFrame cf;
    BingoServerOperations bs_op;

    public SignInPanel(ClientFrame cf, BingoServerOperations bs_op) {
        this.cf = cf;
        this.bs_op = bs_op;
        user = new Player("newuser");

        this.setLayout(new BorderLayout()); //thetoume sto SignInPanel ton diaxeirhsth diatajhs BorderLayout

        GridLayout layout = new GridLayout(6, 2);
        panel = new JPanel(layout);

        row1 = new JPanel();
        label1 = new JLabel("Username:", JLabel.RIGHT);
        username = new JTextField(20);

        row2 = new JPanel();
        label2 = new JLabel("Password:", JLabel.RIGHT);
        password = new JPasswordField(20);

        row3 = new JPanel();
        showPassword = new JCheckBox("Show Password");

        row4 = new JPanel();
        label3 = new JLabel("", JLabel.RIGHT);

        row5 = new JPanel();
        signInButton = new JButton("Sign In");
        signUpButton = new JButton("Sign Up");

        //Prwth grammh
        row1.setLayout(new FlowLayout());
        row1.add(label1);
        row1.add(username);
        panel.add(row1);

        //Deuterh grammh
        row2.setLayout(new FlowLayout());
        row2.add(label2);
        row2.add(password);
        panel.add(row2);

        //Trith grammh
        row3.setLayout(new FlowLayout());
        row3.add(showPassword);
        panel.add(row3);

        //Tetarth grammh
        row4.setLayout(new FlowLayout());
        row4.add(label3);
        panel.add(row4);

        //Pempth grammh
        row5.setLayout(new FlowLayout());
        row5.add(signInButton);
        row5.add(signUpButton);
        panel.add(row5);

        this.add(panel, BorderLayout.NORTH); //prosthetoume to panel sto panw meros tou SignInPanel

        //analoga me to an o xrhsths exei epilegmeno to check box h oxi fainetai h den fainetai o kwdikos tou
        showPassword.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (e.getSource() == showPassword) {
                    if (showPassword.isSelected()) {
                        password.setEchoChar((char) 0);
                    } else {
                        password.setEchoChar('*');
                    }

                }
            }
        });
        
        //sundesh tou xrhsth sthn efarmogh
        signInButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    //an ta stoixeia pou eishgage o xrhsths brethikan mesa sto arxeio
                    if (bs_op.userLogin(username.getText(), String.copyValueOf(password.getPassword()))) {
                        user.setUsername(username.getText());
                        cf.setUsername(username.getText().toString());
                        cf.setPassword(String.copyValueOf(password.getPassword()));
                        cf.remove(cf.signinpanel); //to SignInPanel ejafanizetai

                        //o paikths pou sundethike prostithetai sth lista me tous sundedemenous paiktes
                        String name = "//localhost/OperationClient";
                        try {
                            ClientOperations lOperationClient = new OperationClient(cf.gamepanel.getLotteryNumbersField(),cf.gamepanel.getNumbersAlreadyDrawn());
                            Naming.rebind(name, lOperationClient);
                            bs_op.addPlayer(name);
                        } catch (MalformedURLException ex) {
                            Logger.getLogger(SignInPanel.class.getName()).log(Level.SEVERE, null, ex);
                        }

                        cf.add(cf.gamepanel); //afou to SignInPanel ejafanistei, dinei th thesi tou sto GamePanel pou anaparista to "tablo" tou paixnidiou
                        cf.revalidate(); //to parathuro ginetai refresh gia na fanoun oi allages pou eginan

                    } else {
                        JOptionPane.showMessageDialog(panel, "Username or password is incorrect, please try again.");
                    }
                    //ta pedia pou o xrhsths eisagei to username kai ton kwdiko tou adeiazoun
                    username.setText("");
                    password.setText("");
                } catch (IOException ex) {
                    Logger.getLogger(SignInPanel.class.getName()).log(Level.SEVERE, null, ex);
                }

            }
        });
        
        //eggrafh tou xrhsth sthn efarmogh
        signUpButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    //an to username me to opoio o xrhsths thelei na eggrafei sthn efarmogh den uparxei sto arxeio me ta stoixeia twn eggegrammenwn melwn
                    //tou emfanizetai mhnuma pou epibebaiwnei thn epituxh eggrafh tou sto susthma
                    if (bs_op.userRegistration(username.getText(), String.copyValueOf(password.getPassword()))) {
                        JOptionPane.showMessageDialog(panel, "Registration was successfull!");

                    } 
                    //alliws tou emfanizetai mhnuma oti to username pou eishgage uparxei hdh kai h eggrafh tou aporriptetai
                    else {
                        JOptionPane.showMessageDialog(panel, "The username you entered already exists, please try a different username.");
                    }
                    //ta pedia pou o xrhsths eisagei to username kai ton kwdiko tou adeiazoun
                    username.setText("");
                    password.setText("");
                } catch (IOException ex) {
                    Logger.getLogger(SignInPanel.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
    }
    
    public Player getUser(){
        return user;
    }
}
