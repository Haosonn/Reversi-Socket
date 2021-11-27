package controller;

import model.ChessPiece;
import view.*;

import javax.swing.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;


public class GameController {


    private ChessBoardPanel gamePanel;
    private StatusPanel statusPanel;
    private ChessPiece currentPlayer;
    private int blackScore;
    private int whiteScore;

    public GameController(ChessBoardPanel gamePanel, StatusPanel statusPanel) {
        this.gamePanel = gamePanel;
        this.statusPanel = statusPanel;
        this.currentPlayer = ChessPiece.BLACK;
        blackScore = 2;
        whiteScore = 2;
    }

    public void swapPlayer() {
        currentPlayer = (currentPlayer == ChessPiece.BLACK) ? ChessPiece.WHITE : ChessPiece.BLACK;
        statusPanel.setPlayerText(currentPlayer.name());
        statusPanel.setScoreText(blackScore, whiteScore);
    }

    public void addScore(int score) {
        //todo: modify the countScore method
        if (currentPlayer == ChessPiece.BLACK) {
            blackScore += score;
        } else {
            whiteScore += score;
        }
    }
    public void changeScore(int score){
        if (currentPlayer == ChessPiece.BLACK) {
            blackScore += score;
            whiteScore -= score;
        } else {
            whiteScore += score;
            blackScore -= score;
        }
    }

    public ChessPiece getCurrentPlayer() {
        return currentPlayer;
    }

    public ChessBoardPanel getGamePanel() {
        return gamePanel;
    }


    public void setGamePanel(ChessBoardPanel gamePanel) {
        this.gamePanel = gamePanel;
    }

    public void resetCurrentPlayer(){
        this.currentPlayer = ChessPiece.BLACK;
        blackScore = whiteScore = 2;
        statusPanel.setScoreText(2,2);
        statusPanel.setPlayerText(currentPlayer.name());
    }
    public void readFileData(String fileName) {
        //todo: read date from file
        List<String> fileData = new ArrayList<>();
        try {
            FileReader fileReader = new FileReader(fileName);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                fileData.add(line);
            }
            fileData.forEach(System.out::println);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void writeDataToFile(String fileName) {
        //todo: write data into file
    }
    public boolean canClick(){
        return gamePanel.findAllMoves(currentPlayer);
    }
    public boolean canClick(int row, int col) {
        int cnt =  gamePanel.canClickGrid(row, col, currentPlayer);

        if (cnt == 0) {
            return false;
        }
        else{
            this.changeScore(cnt);
            return true;
        }
    }

    public void endGame() {
        JFrame endFrame = new JFrame();
        if (this.blackScore > this.whiteScore) {
            statusPanel.setGameResult("BLACK WIN");
        } else {
            if (this.whiteScore > this.blackScore) {
                statusPanel.setGameResult("WHITE WIN");
            } else {
                statusPanel.setGameResult("DRAW");
            }
        }
    }
}
