package server;

import java.net.ServerSocket;
import java.net.Socket;

public class ServerThread extends Thread{

    private Socket socket;
    private Server father;
    private ServerSocket serverSocket;

    boolean flag = true;
    public ServerThread(Server father) {
        this.father = father;
        this.serverSocket = father.serverSocket;
    }



    @Override
    public void run() {
            while (flag) {
                try {

                    socket = serverSocket.accept();
                    ServerAgentThread serverAgentThread = new ServerAgentThread(father, socket);
                    serverAgentThread.start();

                    System.out.println("Connected");

                } catch (Exception e ) {
                    e.printStackTrace();
                }
            }
        }
}

