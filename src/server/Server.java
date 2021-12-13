package server;

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
    }
    public static void main(String[] args) throws IOException {
        Server server = new Server();
    }

    public void refreshList(){
        Vector v = new Vector<>();
    }
}
