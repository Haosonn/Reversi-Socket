package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.Vector;


public class Server {

    public Vector onlineList = new Vector();
    ServerSocket serverSocket;
    ServerThread serverThread;
    public Server() {
        try {
            // 创建服务端socket
            serverSocket = new ServerSocket(9090);
            // 创建客户端socket
            serverThread = new ServerThread(this);

            serverThread.start();


        } catch (Exception e) {

            e.printStackTrace();
        }
    }
    public static void main(String[] args) throws IOException {
        Server server = new Server();
    }

    public void refreshList(){
        Vector v = new Vector<>();
    }
}
