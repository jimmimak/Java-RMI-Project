//Dimitrios Makris, 3212019119
//Leonidas Kominos, 3212015092

import java.io.Serializable;

public class Player implements Serializable{
    private String username;
    
    public Player(String username){
        this.username = username;
    }
    
    public String getUsername(){
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
    
}
