package client;

import view.GameFrame;

import javax.swing.*;
import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

public class Client {

    Socket socket;
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
        this.aiMode = true;
        if(!onlineMode) return;
        Scanner typeIn = new Scanner(System.in);
        name = typeIn.next();
        try {
            // 和服务器创建连接
            this.socket = new Socket("192.168.244.208", 9090);
            clientThread= new ClientThread(this);
            clientThread.start();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static void main(String[] args) throws IOException {
        Client client = new Client();

        SwingUtilities.invokeLater(() -> {

            mainFrame = new GameFrame(800, client);
            mainFrame.setVisible(true);


        });
    }

    public ClientThread getClientThread() {
        return this.clientThread;
    }


}
