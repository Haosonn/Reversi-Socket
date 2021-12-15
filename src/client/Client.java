package client;

import view.GameFrame;
import view.MenuBar;

import javax.swing.*;
import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

public class Client {

    public Socket socket;
    public ClientThread clientThread;
    public String name;
    public static GameFrame mainFrame;
    public int color;
    public boolean canMove;
    public boolean onlineMode;
    public boolean aiMode;
    public Client(){
        this.canMove = true;
        this.onlineMode = false;
        this.aiMode = false;

    }
    public static void main(String[] args) throws IOException {
        Client client = new Client();

        SwingUtilities.invokeLater(() -> {

            mainFrame = new GameFrame(1000,800, client);
            mainFrame.setJMenuBar(new MenuBar(mainFrame));
            mainFrame.setVisible(true);


        });
    }

    public ClientThread getClientThread() {
        return this.clientThread;
    }


}
