package server;

import javax.swing.*;
import java.io.IOException;
import java.net.ServerSocket;
import java.util.Vector;


public class Server {

    public Vector onlineList = new Vector();
    ServerSocket serverSocket;
    ServerThread serverThread;
    ServerFrame serverFrame;

    public Server() {
        serverFrame = new ServerFrame(this);
        this.serverFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

    }

    public static void main(String[] args) throws IOException {
        Server server = new Server();
    }

}
