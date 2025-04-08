//Dimitrios Makris, 3212019119
//Leonidas Kominos, 3212015092

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.MalformedURLException;
import java.rmi.*;
import java.rmi.server.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

//klash pou ulopoiei th diepafh BingoServerOperations
public class OperationBingoServer extends UnicastRemoteObject implements BingoServerOperations {

    Random rand;
    private ArrayList<Integer> lotteryNumbers; //lista pou perilambanei olous tous arithmous apo to 1 mexri to 75
    private ArrayList<Integer> numbersAlreadyDrawn; //lista pou perilambanei olous tous arithmous pou exoun klhrwthei wste na apofeugontai ta diplotupa
    private ArrayList<ClientOperations> players; //lista pou perilambanei tous paiktes pou exoun sundethei sthn efarmogh
    private int randomNumber; //metablhth pou periexei ena tuxaio noumero apo to 1 ews to 75 ws to prwto tuxaio noumero pou tha klhrwthei
    WinnerServerOperations w_op;

    public OperationBingoServer(WinnerServerOperations w_op) throws RemoteException {
        super();
        this.w_op = w_op;
        rand = new Random();
        lotteryNumbers = new ArrayList<Integer>();
        players = new ArrayList<ClientOperations>();
        //prosthetoume sth lista lotteryNumbers olous tous arithmous apo to 1 ews to 75
        for (int i = 1; i <= 75; i++) {
            lotteryNumbers.add(i);
        }
        numbersAlreadyDrawn = new ArrayList<Integer>();
        randomNumber = 1 + rand.nextInt(75);

        new LotteryNumbersThread(this).start(); //jekiname th leitourgia tou thread pou tha paragei tous arithmous pou tha klhrwthoun
    }

    //methodos gia thn eggrafh tou xrhsth sthn efarmogh
    @Override
    public boolean userRegistration(String username, String password) throws RemoteException, IOException {
        //an to arxeio pou perilambanei ta username kai tous kwdikous twn eggegrammenwn sthn efarmogh xrhstwn exei dhmiourghthei
        if ((new File("UserData.txt").exists())) {
            BufferedReader in = new BufferedReader(new FileReader("UserData.txt"));
            String line;

            //diabazoume mexri kai thn teleutaia grammh tou arxeiou
            while ((line = in.readLine()) != null) {
                //xwrizoume thn kathe grammh me bash ta kena pou periexei 
                //bazontas thn prwth lejh ths kathe seiras (username) sthn prwth thesi
                //tou pinaka userdata kai thn deuterh lejh ths kathe seiras (password) sthn deuterh
                //thesi tou pinaka userdata
                String[] userdata = line.split(" ");
                //an to username pou eishgage o xrhsths kata thn prospatheia eggrafhs tou
                //einai idio me to username kapoiou hdh eggegrammenou xrhsth mesa apo to arxeio
                //tote h eggrafh tou xrhsth apetuxe kai h methodos epistrefei false
                if (username.equals(userdata[0])) {
                    return false;
                }

            }
        }

        //stream pou kanei append sto arxeio eggrafwn
        BufferedWriter out = new BufferedWriter(new FileWriter("UserData.txt", true));

        out.write(username + " " + password + "\n"); //sto arxeio eggrafwn grafontai to username kai to password tou xrhsth sto ejhs format: "username password" 

        out.close();
        return true; //h eggrafh htan epituxia opote h methodos epistrefei true
    }

    //methodos gia thn sundesh tou xrhsth sthn efarmogh
    @Override
    public boolean userLogin(String username, String password) throws RemoteException, IOException {
        //an to arxeio den uparxei tote o xrhsths pou epixeirei na kanei log in sigoura den uparxei sto susthma
        //ki epomenws h methodos epistrefei false
        if (!(new File("UserData.txt").exists())) {
            return false;
        }
        BufferedReader in = new BufferedReader(new FileReader("UserData.txt"));
        String line;

        //diabazoume mexri kai thn teleutaia grammh tou arxeiou
        while ((line = in.readLine()) != null) {
            //xwrizoume thn kathe grammh me bash ta kena pou periexei 
            //bazontas thn prwth lejh ths kathe seiras (username) sthn prwth thesi
            //tou pinaka userdata kai thn deuterh lejh ths kathe seiras (password) sthn deuterh
            //thesi tou pinaka userdata
            String[] userdata = line.split(" ");
            //an to username pou eishgage o xrhsths gia na sundethei uparxei sto arxeio
            if (username.equals(userdata[0])) {
                //kai an o kwdikos pou eishgage o xrhsths uparxei epishs sto arxeio
                //kai antistoixei sto username pou eishgage o xrhsths
                if (password.equals(userdata[1])) {
                    in.close();
                    return true; //tote h sundesh tou xrhsth sthn efarmogh pragmatopoieitai me epituxia kai h methodos epistrefei true
                } else {
                    in.close();
                    return false; //an omws enw uparxei to username den uparxei o kwdikos h sundesh apotugxanetai kai h methodos epistrefei false
                }

            }
        }
        in.close();
        return false; //an oute to username oute to password pou hshgage o xrhsths den brethoun sto arxeio tote h sundesh apotugxanei kai h methodos epistrefei false
    }

    //methodos pou dhmiourgei to deltio bingo
    @Override
    public int[][] bingoBoard() throws RemoteException {
        int[][] t = new int[5][5]; //pinakas pou periexei tous arithmous tou deltiou bingo

        //1h sthlh
        ArrayList<Integer> list1 = new ArrayList<Integer>(); //lista pou periexei tuxaious arithmous mesa sta oria pou theloume na briskontai oi arithmoi ths prwths sthlhs
        ArrayList<Integer> list2 = new ArrayList<Integer>(); //lista pou periexei tous 5 arithmous pou tha mpoune telika sthn prwth sthlh tou deltiou bingo
        //pername sthn list1 tuxaious arithmous mesa sta oria pou theloume na briskontai oi arithmoi ths prwths sthlhs
        for (int i = 1; i <= 15; i++) {
            list1.add(i);
        }
        Collections.shuffle(list1); //anakateuoume tous arithmous ths list1 wste autoi na einai monadikoi
        //pername tous prwtous 5 arithmous ths list1 sth list2
        for (int i = 0; i < 5; i++) {
            list2.add(list1.get(i));
        }

        //2h sthlh
        ArrayList<Integer> list3 = new ArrayList<Integer>(); //lista pou periexei tuxaious arithmous mesa sta oria pou theloume na briskontai oi arithmoi ths deutherhs sthlhs
        ArrayList<Integer> list4 = new ArrayList<Integer>(); //lista pou periexei tous 5 arithmous pou tha mpoune telika sthn deutherh sthlh tou deltiou bingo
        //pername sthn list3 tuxaious arithmous mesa sta oria pou theloume na briskontai oi arithmoi ths deuterhs sthlhs
        for (int i = 16; i <= 30; i++) {
            list3.add(i);
        }
        Collections.shuffle(list3); //anakateuoume tous arithmous ths list3 wste autoi na einai monadikoi
        //pername tous prwtous 5 arithmous ths list3 sth list4
        for (int i = 0; i < 5; i++) {
            list4.add(list3.get(i));
        }

        //3h sthlh
        ArrayList<Integer> list5 = new ArrayList<Integer>(); //lista pou periexei tuxaious arithmous mesa sta oria pou theloume na briskontai oi arithmoi ths triths sthlhs
        ArrayList<Integer> list6 = new ArrayList<Integer>(); //lista pou periexei tous 5 arithmous pou tha mpoune telika sthn trith sthlh tou deltiou bingo
        //pername sthn list5 tuxaious arithmous mesa sta oria pou theloume na briskontai oi arithmoi ths triths sthlhs
        for (int i = 31; i <= 45; i++) {
            list5.add(i);
        }
        Collections.shuffle(list5); //anakateuoume tous arithmous ths list5 wste autoi na einai monadikoi
        //pername tous prwtous 5 arithmous ths list5 sth list6
        for (int i = 0; i < 5; i++) {
            list6.add(list5.get(i));
        }

        //4h sthlh
        ArrayList<Integer> list7 = new ArrayList<Integer>(); //lista pou periexei tuxaious arithmous mesa sta oria pou theloume na briskontai oi arithmoi ths tetarths sthlhs
        ArrayList<Integer> list8 = new ArrayList<Integer>(); //lista pou periexei tous 5 arithmous pou tha mpoune telika sthn tetarth sthlh tou deltiou bingo
        //pername sthn list7 tuxaious arithmous mesa sta oria pou theloume na briskontai oi arithmoi ths tetarths sthlhs
        for (int i = 46; i <= 60; i++) {
            list7.add(i);
        }
        Collections.shuffle(list7); //anakateuoume tous arithmous ths list7 wste autoi na einai monadikoi
        //pername tous prwtous 5 arithmous ths list7 sth list8
        for (int i = 0; i < 5; i++) {
            list8.add(list7.get(i));
        }

        //5h sthlh
        ArrayList<Integer> list9 = new ArrayList<Integer>(); //lista pou periexei tuxaious arithmous mesa sta oria pou theloume na briskontai oi arithmoi ths pempths sthlhs
        ArrayList<Integer> list10 = new ArrayList<Integer>(); //lista pou periexei tous 5 arithmous pou tha mpoune telika sthn pempth sthlh tou deltiou bingo
        //pername sthn list9 tuxaious arithmous mesa sta oria pou theloume na briskontai oi arithmoi ths pempths sthlhs
        for (int i = 61; i <= 75; i++) {
            list9.add(i);
        }
        Collections.shuffle(list9); //anakateuoume tous arithmous ths list9 wste autoi na einai monadikoi
        //pername tous prwtous 5 arithmous ths list9 sth list10
        for (int i = 0; i < 5; i++) {
            list10.add(list9.get(i));
        }

        //dhmiourgia tou telikou pinaka pou tha periexei tous arithmous tou deltiou bingo
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                t[i][0] = list2.get(i); //sthn prwth sthlh pername tous arithmous ths list2
                t[i][1] = list4.get(i); //sthn deuterh sthlh pername tous arithmous ths list4
                t[i][2] = list6.get(i); //sthn trith sthlh pername tous arithmous ths list6
                t[i][3] = list8.get(i); //sthn tetarth sthlh pername tous arithmous ths list8
                t[i][4] = list10.get(i); //sthn pempth sthlh pername tous arithmous ths list10

            }
        }

        return t;
    }

    //methodos pou prosthetei tous paiktes pou einai sundedemenoi sthn efarmogh se mia lista
    @Override
    public void addPlayer(String name) throws RemoteException {
        try {
            ClientOperations c_op = (ClientOperations) Naming.lookup(name);
            players.add(c_op);
        } catch (NotBoundException ex) {
            Logger.getLogger(OperationBingoServer.class.getName()).log(Level.SEVERE, null, ex);
        } catch (MalformedURLException ex) {
            Logger.getLogger(OperationBingoServer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    //methodos pou afairei tous paiktes pou kleinoun thn efarmogh apo th lista twn sundedemenwn paiktwn
    @Override
    public void removePlayer(String name) throws RemoteException {
        try {
            ClientOperations c_op = (ClientOperations) Naming.lookup(name);
            players.remove(c_op);
        } catch (NotBoundException ex) {
            System.out.println("Client \"" + name + "\" didn't sign in.");
        } catch (MalformedURLException ex) {
            Logger.getLogger(OperationBingoServer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    //Methodos pou elegxei an enas paikths ekane bingo h oxi. H methodos auth mazi me thn lotteryNumber() pou paragei tous monadikous tuxaious arithmous pou tha klhrwthoun
    //dhlwnontai ws synchronized prokeimenou na ruthmistei to zhthma antagwnismou se periptwsh pou o xrhsths dhlwsei oti pisteuei pws exei kanei bingo
    //kai tautoxrona paei na klhrwthei kainourios arithmos
    @Override
    synchronized public boolean bingo(int[][] t, ArrayList<Integer> numbersAlreadyDrawn,
            ArrayList<Integer> bingoNumbers) throws RemoteException {
        //Sunthikh gia na kanei o xrhsths bingo stis tesseris gwnies. H lista numbersAlreadyDrawn elegxei
        //an ta noumera pou klhrwthikan einai ta idia me auta pou briskontai stis tesseris gwnies kai h lista bingoNumbers
        //elegxei an ta noumera pou epeleje o xrhsths einai auta pou briskontai stis tesseris gwnies
        //an ta duo parapanw isxuoun tote o paikths ekane bingo kai h methodos epistrefei true
        if (numbersAlreadyDrawn.contains(t[0][0]) && numbersAlreadyDrawn.contains(t[0][4])
                && numbersAlreadyDrawn.contains(t[4][0]) && numbersAlreadyDrawn.contains(t[4][4])
                && bingoNumbers.contains(t[0][0]) && bingoNumbers.contains(t[0][4])
                && bingoNumbers.contains(t[4][0]) && bingoNumbers.contains(t[4][4])) {
            return true;
        }

        //Sunthikh gia na kanei o xrhsths bingo sthn kuria diagwnio
        int x = 0; //metablhth pou metraei gia posous arithmous pou perilambanei h kuria diagwnios, oi arithmoi pou klhrwthikan kai oi arithmoi pou epeleje o paikths einai idioi me autous ths diagwniou
        for (int i = 0; i < 5; i++) {
            if (numbersAlreadyDrawn.contains(t[i][i]) && bingoNumbers.contains(t[i][i])) {
                x++;
            }
        }
        //an kai oi 5 arithmoi ths diagwniou einai idioi me autous pou klhrwthikan kai me autous pou epeleje o xrhsths tote exei kanei bingo kai h methodos epistrefei true
        if (x == 5) {
            return true;
        }

        //Sunthikh gia na kanei o xrhsths bingo sthn deutereuousa diagwnio
        x = 0; //metablhth pou metraei gia posous arithmous pou perilambanei h deutereuousa diagwnios, oi arithmoi pou klhrwthikan kai oi arithmoi pou epeleje o paikths einai idioi me autous ths diagwniou
        for (int i = 0; i < 5; i++) {
            if (numbersAlreadyDrawn.contains(t[i][4 - i])
                    && bingoNumbers.contains(t[i][4 - i])) {
                x++;
            }
        }
        //an kai oi 5 arithmoi ths deutereuousas diagwniou einai idioi me autous pou klhrwthikan kai me autous pou epeleje o xrhsths tote exei kanei bingo kai h methodos epistrefei true
        if (x == 5) {
            return true;
        }

        //Sunthikh gia na kanei o xrhsths bingo se mia orizontia grammh
        for (int i = 0; i < 5; i++) {
            x = 0; //metablhth pou metraei gia posous arithmous pou perilambanei h grammh, oi arithmoi pou klhrwthikan kai oi arithmoi pou epeleje o paikths einai idioi me autous ths grammhs
            for (int j = 0; j < 5; j++) {
                if (numbersAlreadyDrawn.contains(t[i][j])
                        && bingoNumbers.contains(t[i][j])) {
                    x++;
                }
            }
            //an kai oi 5 arithmoi ths grammhs einai idioi me autous pou klhrwthikan kai me autous pou epeleje o xrhsths tote exei kanei bingo kai h methodos epistrefei true
            if (x == 5) {
                return true;
            }
        }

        //Sunthikh gia na kanei o xrhsths bingo se mia sthlh
        for (int j = 0; j < 5; j++) {
            x = 0; //metablhth pou metraei gia posous arithmous pou perilambanei h sthlh, oi arithmoi pou klhrwthikan kai oi arithmoi pou epeleje o paikths einai idioi me autous ths sthlhs
            for (int i = 0; i < 5; i++) {
                if (numbersAlreadyDrawn.contains(t[i][j])
                        && bingoNumbers.contains(t[i][j])) {
                    x++;
                }
            }
            //an kai oi 5 arithmoi ths sthlhs einai idioi me autous pou klhrwthikan kai me autous pou epeleje o xrhsths tote exei kanei bingo kai h methodos epistrefei true
            if (x == 5) {
                return true;
            }
        }
        return false; //an tipota apo ta parapanw den isxusei tote o xrhsths den exei kanei bingo kai h sunarthsh epistrefei false
    }

    //methodos pou stelnei enan paikth pou ekane bingo apo ton Client ston BingoServer kai apo ton BingoServer ston WinnerServer.
    //Auto sumbainei afou o Client den epikoinwnei ap eutheias me ton WinnerServer
    @Override
    public void sendWinnerToWinnerServer(Player p) throws RemoteException {
        w_op.recordWinnersToFile(p);
    }

    //methodos pou epistrefei ta onomata twn nikhtwn apo to arxeio antikeimenwn pou autoi briskontai ston WinnerServer
    @Override
    public ArrayList<String> returnWinners() throws RemoteException, IOException, ClassNotFoundException {
        ArrayList<String> winners = new ArrayList<>(); //lista sthn opoia apothikeuontai ta onomata twn nikhtwn
        //gia kathe paikth pou uparxei sto arxeio prosthetoume sth lista winners to username tou kathe paikth
        for (Player p : w_op.getWinners()) {
            winners.add(p.getUsername());
        }
        return winners; //epistrefoume th lista me ta usernames twn nikhtwn
    }

    public ArrayList<ClientOperations> getPlayers() {
        return players;
    }

    public ArrayList<Integer> getNumbersAlreadyDrawn() {
        return numbersAlreadyDrawn;
    }

    //H methodos auth mazi me thn bingo() pou elegxei an enas paikths exei kanei bingo
    //dhlwnontai ws synchronized prokeimenou na ruthmistei to zhthma antagwnismou se periptwsh pou o xrhsths dhlwsei oti pisteuei pws exei kanei bingo
    //kai tautoxrona paei na klhrwthei kainourios arithmos
    synchronized public int lotteryNumber() {

        //an o tuxaios arithmos pou klhrwnetai kathe fora uparxei sth lista me tous hdh klhrwmenous arithmous
        //tote janapsaxnoume mesa sth lista me tous arithmous apo to 1 ews to 75 kai dialegoume allo arithmo, auto sumbainei
        //etsi wste na bebaiwthoume oti den tha klhrwthei kapoios arithmos parapanw apo mia fora
        while (numbersAlreadyDrawn.contains(randomNumber)) {
            randomNumber = lotteryNumbers.get(rand.nextInt(lotteryNumbers.size()));
        }
        numbersAlreadyDrawn.add(randomNumber); //alliws o arithmos pou klhrwthike mpainei sth lista me tous klhrwmenous arithmous

        return randomNumber; //epistrefoume ton arithmo pou klhrwthike
    }
}
