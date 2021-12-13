package view;

import client.Client;
import client.ClientThread;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.Socket;
import java.util.Scanner;

public class NetworkPanel extends JPanel {

    Client client;
    JLabel portLabel = new JLabel("Port");
    JLabel ipLabel = new JLabel("IP Address");
    JLabel nameLabel = new JLabel("Name");
    JTextField nameTextField = new JTextField("Enter your name");
    JTextField portTextField = new JTextField("9090");
    JTextField ipTextField = new JTextField("localhost");
    JButton connectButton = new JButton("Connect");
    JButton disconnectButton = new JButton("Disconnect");
    JButton challengeButton = new JButton("Challenge");
    JButton acceptChallengeButton = new JButton("Accept Challenge");
    JButton refuseChallengeButton = new JButton("Refuse Challenge");
    JComboBox players = new JComboBox();

    public NetworkPanel(int width, int height, Client client){
        this.client = client;
        this.setSize(width, height);
        this.setLayout(null);
        this.portLabel.setBounds(0, 50, 200, 100);
        this.portLabel.setFont(new java.awt.Font(Font.DIALOG, 1, 30));
        this.add(this.portLabel);

        this.portTextField.setBounds(200, 50, 200, 100);
        this.portTextField.setFont(new java.awt.Font(Font.DIALOG, 1, 30));
        this.add(this.portTextField);

        this.ipLabel.setBounds(0, 200, 200, 100);
        this.ipLabel.setFont(new java.awt.Font(Font.DIALOG, 1, 30));
        this.add(this.ipLabel);

        this.ipTextField.setBounds(200, 200, 200, 100);
        this.ipTextField.setFont(new java.awt.Font(Font.DIALOG, 1, 30));
        this.add(this.ipTextField);

        this.nameLabel.setBounds(0, 400, 200, 100);
        this.nameLabel.setFont(new java.awt.Font(Font.DIALOG, 1, 30));
        this.add(this.nameLabel);

        this.nameTextField.setBounds(200, 400, 200, 100);
        this.nameTextField.setFont(new java.awt.Font(Font.DIALOG, 1, 15));
        this.add(this.nameTextField);

        this.connectButton.setBounds(20, 600, 150, 100);
        this.connectButton.setFont(new java.awt.Font(Font.DIALOG, 1, 15));
        this.connectButton.addActionListener(new Listener());
        this.add(this.connectButton);

        this.disconnectButton.setBounds(200, 600, 150, 100);
        this.disconnectButton.setFont(new java.awt.Font(Font.DIALOG, 1, 15));
        this.disconnectButton.addActionListener(new Listener());
        this.add(this.disconnectButton);
        this.disconnectButton.setEnabled(false);

        this.players.setBounds(20,800,150,100);
        this.players.setFont(new java.awt.Font(Font.DIALOG, 1, 15));
        this.add(this.players);

        this.challengeButton.setBounds(20, 1000, 150, 100);
        this.challengeButton.setFont(new java.awt.Font(Font.DIALOG, 1, 15));
        this.challengeButton.addActionListener(new Listener());
        this.add(this.challengeButton);

        this.acceptChallengeButton.setBounds(20, 1200, 150, 100);
        this.acceptChallengeButton.setFont(new java.awt.Font(Font.DIALOG, 1, 15));
        this.acceptChallengeButton.addActionListener(new Listener());
        this.add(this.acceptChallengeButton);
        this.acceptChallengeButton.setEnabled(false);

        this.refuseChallengeButton.setBounds(200, 1200, 150, 100);
        this.refuseChallengeButton.setFont(new java.awt.Font(Font.DIALOG, 1, 15));
        this.refuseChallengeButton.addActionListener(new Listener());
        this.add(this.refuseChallengeButton);
        this.refuseChallengeButton.setEnabled(false);

        this.setVisible(true);

    }

    class Listener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            if(e.getSource() == connectButton){
                connectEvent();
            }
            else if(e.getSource() == disconnectButton){
                disconnectEvent();
            }
            else if(e.getSource() == challengeButton){
                challengeEvent();
            }
            else if(e.getSource() == acceptChallengeButton){
                acceptChallengeEvent();
            }
            else if(e.getSource() == refuseChallengeButton){
                refuseChallengeEvent();
            }

        }
    }

    public void connectEvent(){
        this.client.name = this.nameTextField.getText().trim();
        try {
            // 和服务器创建连接
            this.client.onlineMode = true;
            this.client.socket = new Socket(this.ipTextField.getText().trim(), 9090);
            this.client.clientThread= new ClientThread(this.client);
            this.client.clientThread.start();
            this.client.canMove = false;
            this.connectButton.setEnabled(false);
            this.ipTextField.setEnabled(false);
            this.portTextField.setEnabled(false);
            this.disconnectButton.setEnabled(true);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void disconnectEvent(){

    }

    public void challengeEvent(){
        Object o = this.players.getSelectedItem();

        if (o == null || ((String) o).equals("")) {
            JOptionPane.showMessageDialog(this, "Please Select a name", "Error",
                    JOptionPane.ERROR_MESSAGE);//当未选中挑战对象，给出错误提示信息
        }
        else {
            String opposite = (String) this.players.getSelectedItem();
            try {

                this.client.oppositeName = opposite;
                this.client.clientThread.dataOutputStream.writeUTF(String.format("<!CHALLENGE!> %s %s", opposite, this.client.name));
                JOptionPane.showMessageDialog(this, "Waiting for response", "Info",
                        JOptionPane.INFORMATION_MESSAGE);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void acceptChallengeEvent(){
        try {
            this.client.clientThread.dataOutputStream.writeUTF("<!ACCEPT_CHALLENGE!> " + this.client.name);
            this.client.clientThread.initialateColor(-1);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void refuseChallengeEvent(){
        try {
            this.client.clientThread.dataOutputStream.writeUTF("<!REFUSE_CHALLENGE!> " + this.client.name);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
