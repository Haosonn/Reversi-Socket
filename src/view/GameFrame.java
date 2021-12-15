package view;


import ai.ThreadForAI;
import client.Client;
import controller.GameController;
import model.ChessPiece;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.Vector;

public class GameFrame extends JFrame {
    public static GameController controller;
    private ChessBoardPanel chessBoardPanel;
    private StatusPanel statusPanel;
    private NetworkPanel networkPanel;


    public GameFrame(int frameWidth,int frameHeight, Client client) {


        this.setTitle("2021F CS102A Project Reversi");
        this.setLayout(null);

        //获取窗口边框的长度，将这些值加到主窗口大小上，这能使窗口大小和预期相符
        Insets inset = this.getInsets();
        this.setSize(frameWidth + inset.left + inset.right, frameHeight + inset.top + inset.bottom);

        this.setLocationRelativeTo(null);


        chessBoardPanel = new ChessBoardPanel((int) (this.getWidth() * 0.8), (int) (this.getHeight() * 0.8));
        chessBoardPanel.setLocation((this.getWidth()) / 25, (this.getHeight()) / 12);

        statusPanel = new StatusPanel((int) (this.getWidth() * 0.6), (int) (this.getHeight() * 0.08));
        statusPanel.setLocation((this.getWidth()) / 8, 0);
        controller = new GameController(chessBoardPanel, statusPanel, client);
        controller.setGamePanel(chessBoardPanel);

        networkPanel = new NetworkPanel((int) (this.getWidth() * 0.2), (int) (this.getHeight()), client);
        networkPanel.setLocation( (15 * this.getWidth()) / 20, (this.getHeight()) / 10);
        this.add(chessBoardPanel);
        this.add(statusPanel);
        this.add(networkPanel);
        controller.addToHistory();


        this.setVisible(true);
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

    }

    public void restart() {
        controller.setCurrentPlayer(ChessPiece.BLACK);
        this.chessBoardPanel.clear();
        this.chessBoardPanel.initialChessGrids();
        this.chessBoardPanel.initialGame();
        this.chessBoardPanel.clearReminders();
        this.chessBoardPanel.findAllMoves(ChessPiece.BLACK);
        controller.resetCurrentPlayer();
        controller.addToHistory();
        if (GameFrame.controller.isBlackAIModeOn()) {
            GameFrame.controller.setThreadForBlackAI(new ThreadForAI(1, controller.getBlackDeep()));
            GameFrame.controller.getThreadForBlackAI().start();
            System.out.println("Black AI On");
        }
        if (GameFrame.controller.isWhiteAIModeOn()) {
            GameFrame.controller.setThreadForWhiteAI(new ThreadForAI(-1, controller.getWhiteDeep()));
            GameFrame.controller.getThreadForWhiteAI().start();
            System.out.println("White AI On");
        }
        repaint();
    }

    public void refreshPlayerList(String msg){
        Vector v = new Vector();
        String[] info = msg.split(" ");
        int cnt = Integer.parseInt(info[1]);
        for (int i = 1; i <= cnt; i++) {
            if(controller.client.name.equals(info[i+1])) continue;
            v.add(info[i+1]);
        }
        this.networkPanel.players.setModel(new DefaultComboBoxModel(v));

    }

    public void getChallengeRequest(String msg) throws IOException {
        if(!msg.split(" ")[1].equals(controller.client.name)) return;
        controller.client.clientThread.dataOutputStream.writeUTF("<!OPPOSITE_NAME!> " + msg.split(" ")[2]);
        JOptionPane.showMessageDialog(this, String.format("%s challenges you!\nDo you agree his(her) request?", msg.split(" ")[2]), "Info",
                JOptionPane.INFORMATION_MESSAGE);
        this.networkPanel.acceptChallengeButton.setEnabled(true);
        this.networkPanel.refuseChallengeButton.setEnabled(true);
    }
    public ChessBoardPanel getChessBoardPanel() {
        return this.chessBoardPanel;
    }

    public void receiveRefuseChallengeMsg(String msg){
        JOptionPane.showMessageDialog(this, String.format("%s refuses your challenge!", msg.split(" ")[1]), "Info",
                JOptionPane.INFORMATION_MESSAGE);
    }

    public void repeatName(){
        JOptionPane.showMessageDialog(this, "Your name is used！\n Please choose another one!",
                "Error", JOptionPane.ERROR_MESSAGE);
        try{
            controller.client.clientThread.dataOutputStream.close();
            controller.client.clientThread.dataInputStream.close();
            this.networkPanel.ipTextField.setEnabled(true);
            this.networkPanel.portTextField.setEnabled(true);
            this.networkPanel.nameTextField.setEnabled(true);
            this.networkPanel.connectButton.setEnabled(true);
            this.networkPanel.disconnectButton.setEnabled(false);
            this.networkPanel.challengeButton.setEnabled(false);
            this.networkPanel.acceptChallengeButton.setEnabled(false);
            this.networkPanel.refuseChallengeButton.setEnabled(false);
            controller.client.socket.close();
            controller.client.socket = null;
            controller.client.clientThread.flag = false;
            controller.client.clientThread = null;
        }catch (Exception e){
            e.printStackTrace();
        }

    }
}
