package client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.util.Scanner;

public class ClientThread extends Thread{
    private Client father;
    boolean flag = true;

    public DataOutputStream dataOutputStream;
    public DataInputStream dataInputStream;

    Scanner typeIn = new Scanner(System.in);
    public ClientThread(Client father){
        this.father = father;
        try{
            dataInputStream = new DataInputStream(father.socket.getInputStream());
            dataOutputStream = new DataOutputStream(father.socket.getOutputStream());

            String name = father.name;

            dataOutputStream.writeUTF(name);
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        while(flag){
            try{
                String msg = dataInputStream.readUTF().trim();
                System.out.println(msg);
                if(msg.equals("Welcome")) continue;
                else if(msg.startsWith("<!COLOR!>")){
                    this.initialateColor(msg);
                }
                else if(msg.startsWith("<!UNDO_REQUEST!>")){
                    this.undoRequest();
                }
                else if(msg.startsWith("<!AGREEUNDO!>")){
                    System.out.println("Agree!");
                    Client.mainFrame.controller.undo();
                }
                else if(msg.startsWith("<!DISAGREEUNDO!>")){
                    System.out.println("Disagree!");
                }
                else{
                    Client.mainFrame.controller.readData(msg);
                    this.father.canMove = true;
                    Client.mainFrame.repaint();
                }


            } catch (Exception e){
                e.printStackTrace();
            }
        }

    }

    public void initialateColor(String msg) {
        if(msg.charAt(10) == '0'){
            this.father.color = 1;
            this.father.canMove = true;
        }
        else{
            this.father.color = -1;
            this.father.canMove = false;
        }
    }

    public void undoRequest() {
        System.out.printf("Do u AGREE your opposite's UNDO? (Y/N):\n");
        String ans = typeIn.next();
        try{
            if(ans.charAt(0) == 'Y'){
                dataOutputStream.writeUTF("<!AGREEUNDO!>");
            }
            else{
                dataOutputStream.writeUTF("<!DISAGREEUNDO!>");
            }
        } catch (Exception e){
            e.printStackTrace();
        }

    }
}

