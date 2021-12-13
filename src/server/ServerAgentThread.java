package server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;
import java.util.Vector;

public class ServerAgentThread extends Thread{
    Socket socket = null;
    Server father = null;
    DataInputStream dataInputStream;
    DataOutputStream dataOutputStream;
    String name;
    String oppositeName;
    boolean flag = true;

    public ServerAgentThread(Server father, Socket socket) {
        this.socket = socket;
        this.father = father;
        try{
            dataInputStream = new DataInputStream(socket.getInputStream());
            dataOutputStream = new DataOutputStream(socket.getOutputStream());
            //dataOutputStream.writeUTF(String.format("<!COLOR!> %d",father.onlineList.size()));
            name = dataInputStream.readUTF().trim();
            this.father.onlineList.add(this);
            this.father.serverFrame.refreshList();
            Vector v = this.father.onlineList;
            String msg = "<!NAME_LIST!>";
            msg += String.format(" %s",String.valueOf(v.size()));
            for (int i = 0; i < v.size(); i++) {
                ServerAgentThread tempSat = (ServerAgentThread) v.get(i);
                msg += String.format(" %s",tempSat.name);
            }
            for (int i = 0; i < v.size(); i++) {
                ServerAgentThread tempSat = (ServerAgentThread) v.get(i);
                tempSat.dataOutputStream.writeUTF(msg);
            }
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
                    if (tempSat.name.equals(this.name)) continue;
                    if (msg.startsWith("<!MOVE!>") || msg.startsWith("<!UNDO_REQUEST!>") || msg.startsWith("<!AGREEUNDO!>") || msg.startsWith("<!DISAGREEUNDO!>")) {
                        if(tempSat.name.equals(oppositeName)){
                            tempSat.dataOutputStream.writeUTF(msg);
                            break;
                        }
                    }
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
