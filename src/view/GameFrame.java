package view;


import client.Client;
import controller.GameController;
import model.ChessPiece;

import javax.swing.*;
import java.awt.*;

public class GameFrame extends JFrame {
    public static GameController controller;
    private ChessBoardPanel chessBoardPanel;
    private StatusPanel statusPanel;


    public GameFrame(int frameSize, Client client) {


        this.setTitle("2021F CS102A Project Reversi");
        this.setLayout(null);

        //获取窗口边框的长度，将这些值加到主窗口大小上，这能使窗口大小和预期相符
        Insets inset = this.getInsets();
        this.setSize(frameSize + inset.left + inset.right, frameSize + inset.top + inset.bottom);

        this.setLocationRelativeTo(null);


        chessBoardPanel = new ChessBoardPanel((int) (this.getWidth() * 0.8), (int) (this.getHeight() * 0.7));
        chessBoardPanel.setLocation((this.getWidth() - chessBoardPanel.getWidth()) / 2, (this.getHeight() - chessBoardPanel.getHeight()) / 3);

        statusPanel = new StatusPanel((int) (this.getWidth() * 0.8), (int) (this.getHeight() * 0.1));
        statusPanel.setLocation((this.getWidth() - chessBoardPanel.getWidth()) / 2, 0);
        controller = new GameController(chessBoardPanel, statusPanel);
        controller.setGamePanel(chessBoardPanel);
        this.controller.client = client;

        this.add(chessBoardPanel);
        this.add(statusPanel);
        controller.addToHistory();

        JButton restartBtn = new JButton("Restart");
        restartBtn.setSize(90, 50);
        restartBtn.setLocation((this.getWidth() - chessBoardPanel.getWidth()) / 2, (this.getHeight() + chessBoardPanel.getHeight()) / 2);
        add(restartBtn);
        restartBtn.addActionListener(e -> {
            restart();
            System.out.println("click restart Btn");
        });

        JButton loadGameBtn = new JButton("Load");
        loadGameBtn.setSize(90, 50);
        loadGameBtn.setLocation(restartBtn.getX() + restartBtn.getWidth() + 28, restartBtn.getY());
        add(loadGameBtn);
        loadGameBtn.addActionListener(e -> {
            System.out.println("clicked Load Btn");
            String filePath = JOptionPane.showInputDialog(this, "input the path here");
            if(filePath.length()==0) return;
            chessBoardPanel.clear();
            chessBoardPanel.initialChessGrids();
            chessBoardPanel.clearReminders();
            controller.readFileData(filePath);
            repaint();
        });

        JButton saveGameBtn = new JButton("Save");
        saveGameBtn.setSize(90, 50);
        saveGameBtn.setLocation(loadGameBtn.getX() + restartBtn.getWidth() + 28, restartBtn.getY());
        add(saveGameBtn);
        saveGameBtn.addActionListener(e -> {
            System.out.println("clicked Save Btn");
            String filePath = JOptionPane.showInputDialog(this, "input the path here");
            controller.writeDataToFile(filePath);
        });

        JButton undoBtn = new JButton("Undo");
        undoBtn.setSize(90, 50);
        undoBtn.setLocation(saveGameBtn.getX() + restartBtn.getWidth() + 28, restartBtn.getY());
        add(undoBtn);
        undoBtn.addActionListener(e -> {
            System.out.println("clicked Undo Btn");
            controller.undoRequest();
            //controller.undo();
            repaint();
        });

        JToggleButton cheatingBtn = new JToggleButton("cheating");
        cheatingBtn.setSize(90, 50);
        cheatingBtn.setLocation(undoBtn.getX() + restartBtn.getWidth() + 28, restartBtn.getY());
        add(cheatingBtn);
        cheatingBtn.addActionListener(e -> {
            System.out.println("clicked Cheat Btn");
            controller.setCheatingBtnOn(!controller.isCheatingBtnOn());
        });

        this.setVisible(true);
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

    }

    private void restart() {
        this.controller.setCurrentPlayer(ChessPiece.BLACK);
        this.chessBoardPanel.clear();
        this.chessBoardPanel.initialChessGrids();
        this.chessBoardPanel.initialGame();
        this.chessBoardPanel.clearReminders();
        this.chessBoardPanel.findAllMoves(ChessPiece.BLACK);
        this.controller.resetCurrentPlayer();
        this.controller.addToHistory();
        repaint();
    }
}
