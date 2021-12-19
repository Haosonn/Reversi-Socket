package client;

import view.GameFrame;

import javax.swing.*;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.util.Scanner;

public class ClientThread extends Thread {
    private Client father;
    public boolean flag = true;

    public DataOutputStream dataOutputStream;
    public DataInputStream dataInputStream;

    Scanner typeIn = new Scanner(System.in);

    public ClientThread(Client father) {
        this.father = father;
        try {
            dataInputStream = new DataInputStream(father.socket.getInputStream());
            dataOutputStream = new DataOutputStream(father.socket.getOutputStream());
            String name = father.name;
            dataOutputStream.writeUTF(name);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        while (flag) {
            try {
                String msg = dataInputStream.readUTF().trim();
                System.out.println(msg);
                if (msg.equals("<!REPEATED_NAME!>")) {
                    Client.mainFrame.repeatName();
                } else if (msg.startsWith("<!ACCEPT_CHALLENGE!>")) {
                    this.initialateColor(1);
                    JOptionPane.showMessageDialog(Client.mainFrame,Client.mainFrame.oppositeName+" has accepted your challenge","info",JOptionPane.INFORMATION_MESSAGE);
                    Client.mainFrame.setduishou();
                    Client.mainFrame.setSurrenderBtnOn();
                } else if (msg.startsWith("<!REFUSE_CHALLENGE!>")) {
                    Client.mainFrame.receiveRefuseChallengeMsg(msg);
                } else if (msg.startsWith("<!UNDOREQUEST!>")) {
                    this.undoRequest();
                } else if (msg.startsWith("<!AGREEUNDO!>")) {
                    System.out.println("Agree!");
                    GameFrame.controller.undo();
                    Client.mainFrame.repaint();
                } else if (msg.startsWith("<!DISAGREEUNDO!>")) {
                    System.out.println("Disagree!");
                } else if (msg.startsWith("<!NAME_LIST!>")) {
                    Client.mainFrame.refreshPlayerList(msg);
                } else if (msg.startsWith("<!CHALLENGE!>")) {
                    Client.mainFrame.getChallengeRequest(msg);
                } else if (msg.startsWith("<!MOVE!>")) {
                    GameFrame.controller.readData(msg);
                    this.father.canMove = true;
                    Client.mainFrame.repaint();
                } else if (msg.startsWith("<!SURRENDER!>")) {
                    if (this.father.color == 1) {
                        GameFrame.controller.setBlackScore(64);
                        GameFrame.controller.setWhiteScore(0);
                    } else {
                        GameFrame.controller.setBlackScore(0);
                        GameFrame.controller.setWhiteScore(64);
                    }
                    Client.mainFrame.setChallengeBtnOn();
                    JOptionPane.showMessageDialog(Client.mainFrame, "Your opposite has surrendered");
                    GameFrame.controller.endGame();
                }


            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    public void initialateColor(int color) {
        Client.mainFrame.restart();
        Client.mainFrame.setziji(color);
        if (color == 1) {
            this.father.color = 1;
            this.father.canMove = true;
        } else {
            this.father.color = -1;
            this.father.canMove = false;
        }
    }

    public void undoRequest() {
        int option = JOptionPane.showConfirmDialog(
                Client.mainFrame, "Do u AGREE your opposite's UNDO? ", "Request ", JOptionPane.YES_NO_CANCEL_OPTION);
        try {
            if (option == JOptionPane.YES_OPTION) {
                dataOutputStream.writeUTF("<!AGREEUNDO!>");
                Client.mainFrame.controller.undo();
                Client.mainFrame.repaint();
            } else {
                dataOutputStream.writeUTF("<!DISAGREEUNDO!>");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


    }
}

