import client.Client;
import server.Server;
import view.GameFrame;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        try{
            Server.main(args);
            Client.main(args);
        }catch (Exception e){
            e.printStackTrace();
        }


    }
}
