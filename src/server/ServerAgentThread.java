package server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;
import java.util.Vector;

public class ServerAgentThread extends Thread{
    private Socket socket = null;
    private Server father = null;
    DataInputStream dataInputStream;
    DataOutputStream dataOutputStream;
    String name;
    boolean flag = true;

    public ServerAgentThread(Server father, Socket socket) {
        this.socket = socket;
        this.father = father;
        try{
            dataInputStream = new DataInputStream(socket.getInputStream());
            dataOutputStream = new DataOutputStream(socket.getOutputStream());
            dataOutputStream.writeUTF(String.format("<!COLOR!> %d",father.onlineList.size()));
            this.father.onlineList.add(this);
            name = dataInputStream.readUTF().trim();
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        while (flag){
            try {
                String msg = dataInputStream.readUTF().trim();
                Vector v = this.father.onlineList;
                for (int i = 0; i < v.size(); i++) {
                    ServerAgentThread tempSat = (ServerAgentThread) v.get(i);
                    if(tempSat.name.equals(this.name)) continue;
                    tempSat.dataOutputStream.writeUTF(msg);
                }
                System.out.println(msg);


                //dataOutputStream.writeUTF(msg);
            } catch (Exception e ){
                e.printStackTrace();
            }
        }
    }

}
