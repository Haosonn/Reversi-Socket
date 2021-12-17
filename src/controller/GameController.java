package controller;

import ai.ThreadForAI;
import client.Client;
import model.ChessPiece;
import view.*;
import game.GameRecord;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;


public class GameController {

    public Client client;
    private ChessBoardPanel gamePanel;
    private StatusPanel statusPanel;
    private ChessPiece currentPlayer;

    private ThreadForAI threadForBlackAI;
    private ThreadForAI threadForWhiteAI;
    private boolean blackAIModeOn = false;
    private boolean whiteAIModeOn = false;
    private int blackDeep = 4;
    private int whiteDeep = 4;
    public boolean gameEnd = false;
    public boolean canMouseClick = true;

    public boolean isBlackAIModeOn() {
        return blackAIModeOn;
    }

    public void setBlackAIModeOn(boolean blackAIModeOn) {
        this.blackAIModeOn = blackAIModeOn;
    }

    private int blackScore;
    private int whiteScore;
    private boolean cheatingBtnOn = false;

    private ArrayList<GameRecord> gameHistory = new ArrayList();

    public boolean isCheatingBtnOn() {
        return cheatingBtnOn;
    }

    public void setThreadForBlackAI(ThreadForAI threadForBlackAI) {
        this.threadForBlackAI = threadForBlackAI;
    }

    public boolean isWhiteAIModeOn() {
        return whiteAIModeOn;
    }

    public void setWhiteAIModeOn(boolean whiteAIModeOn) {
        this.whiteAIModeOn = whiteAIModeOn;
    }

    public int getBlackDeep() {
        return blackDeep;
    }

    public void setBlackDeep(int blackDeep) {
        this.blackDeep = blackDeep;
    }

    public int getWhiteDeep() {
        return whiteDeep;
    }

    public void setWhiteDeep(int whiteDeep) {
        this.whiteDeep = whiteDeep;
    }

    public ThreadForAI getThreadForBlackAI() {
        return threadForBlackAI;
    }

    public ThreadForAI getThreadForWhiteAI() {
        return threadForWhiteAI;
    }

    public void setThreadForWhiteAI(ThreadForAI threadForWhiteAI) {
        this.threadForWhiteAI = threadForWhiteAI;
    }

    public void setCheatingBtnOn(boolean cheatingBtnOn) {
        this.cheatingBtnOn = cheatingBtnOn;
    }

    public GameRecord getThisStep() {
        return gameHistory.get(gameHistory.size() - 1);
    }

    public GameController(ChessBoardPanel gamePanel, StatusPanel statusPanel, Client client) {
        this.gamePanel = gamePanel;
        this.statusPanel = statusPanel;
        this.currentPlayer = ChessPiece.BLACK;
        blackScore = 2;
        whiteScore = 2;
        this.client = client;

    }

    public void swapPlayer() {
        currentPlayer = (currentPlayer == ChessPiece.BLACK) ? ChessPiece.WHITE : ChessPiece.BLACK;
        statusPanel.setPlayerText(currentPlayer.name());
        statusPanel.setScoreText(blackScore, whiteScore);
    }

//    public void addScore(int score) {
//        //todo: modify the countScore method
//        if (currentPlayer == ChessPiece.BLACK) {
//            blackScore += score;
//        } else {
//            whiteScore += score;
//        }
//    }
//
//    public void changeScore(int score) {
//        if (currentPlayer == ChessPiece.BLACK) {
//            blackScore += score;
//            whiteScore -= score;
//        } else {
//            whiteScore += score;
//            blackScore -= score;
//        }
//    }

    public void updateScore() {
        blackScore = whiteScore = 0;
        for (int i = 0; i <= 7; i++) {
            for (int j = 0; j <= 7; j++) {
                if (gamePanel.getChessBoard()[i][j] == ChessPiece.BLACK) blackScore++;
                else if (gamePanel.getChessBoard()[i][j] == ChessPiece.WHITE) whiteScore++;
            }
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

    public void resetCurrentPlayer() {
        this.currentPlayer = ChessPiece.BLACK;
        blackScore = whiteScore = 2;
        statusPanel.setScoreText(2, 2);
        statusPanel.setPlayerText(currentPlayer.name());
    }

    public void readFileData(String fileName) {
        List<String> fileData = new ArrayList<>();
        GameRecord gameRecord = new GameRecord();
        try {
            FileReader fileReader = new FileReader(fileName);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                fileData.add(line);
            }
//            statusPanel.setScoreText(this.blackScore, this.whiteScore);
//            statusPanel.setPlayerText(currentPlayer.name());
//            gamePanel.findAllMoves(currentPlayer);
            Client.mainFrame.restart();
//            gameHistory.clear();
            ThreadForLoading threadForLoading = new ThreadForLoading(fileData);
            threadForLoading.start();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void readData(String data) {
        data = data.substring(9);
        int row = Integer.parseInt(data.split(" ")[0]);
        int col = Integer.parseInt(data.split(" ")[1]);

        GameFrame.controller.getGamePanel().getChessGrids(row, col).onMouseClicked();
//        statusPanel.setScoreText(this.blackScore, this.whiteScore);
//        statusPanel.setPlayerText(currentPlayer.name());
//        gamePanel.findAllMoves(currentPlayer);
//        this.addToHistory();
    }

    public void writeDataToFile(String filePath) {
        //todo: write data into file
        try {
            FileWriter fileWriter = new FileWriter(filePath);
            BufferedWriter writer = new BufferedWriter(fileWriter);
            List<String> lines = new ArrayList<>();
            for (GameRecord gameRecord : gameHistory) {
                lines.add(gameRecord.toString());
            }
            for (String line : lines) {
                writer.write(line);
            }
            writer.close();
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean canClick() {
        return gamePanel.findAllMoves(currentPlayer);
    }

    public boolean canClick(int row, int col) {
        int cnt = gamePanel.canClickGrid(row, col, currentPlayer);
        if (cnt == 0) {
            return false;
        } else {
            this.gamePanel.repaint();
            this.updateScore();
            return true;
        }
    }

    public void endGame() {
        if (threadForBlackAI != null && threadForBlackAI.isAlive()) threadForBlackAI.exit = true;
        if (threadForWhiteAI != null && threadForWhiteAI.isAlive()) threadForWhiteAI.exit = true;
        gameEnd = true;
        if (this.blackScore > this.whiteScore) {
            statusPanel.setGameResult("BLACK WIN");
            JOptionPane.showMessageDialog(Client.mainFrame, "BLACK WIN");
        } else {
            if (this.whiteScore > this.blackScore) {
                statusPanel.setGameResult("WHITE WIN");
                JOptionPane.showMessageDialog(Client.mainFrame, "WHITE WIN");
            } else {
                statusPanel.setGameResult("DRAW");
                JOptionPane.showMessageDialog(Client.mainFrame, "DRAW");
            }
        }
    }

    public void setThisStep(GameRecord gameRecord) {
        gameRecord.setChessboard(gamePanel.getChessBoard());
        gameRecord.setBlackCnt(blackScore);
        gameRecord.setWhiteCnt(whiteScore);
        gameRecord.setCurrentPlayer(currentPlayer);
    }

    public void addToHistory(int row, int col) {
        GameRecord thisChessBoard = new GameRecord();
        setThisStep(thisChessBoard);
        thisChessBoard.setStep(new int[]{row, col});
        gameHistory.add(thisChessBoard);
    }


    public void setBlackScore(int blackScore) {
        this.blackScore = blackScore;
    }

    public void setWhiteScore(int whiteScore) {
        this.whiteScore = whiteScore;
    }

    public void setCurrentPlayer(ChessPiece currentPlayer) {
        this.currentPlayer = currentPlayer;
    }

    public void undoRequest() {
        try {
            this.client.clientThread.dataOutputStream.writeUTF("<!UNDOREQUEST!>");
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void undo() {
        GameRecord gameRecord = new GameRecord();
        if (gameHistory.size() == 1) return;
        gameHistory.remove(gameHistory.size() - 1);
        String previousMove = gameHistory.get(gameHistory.size() - 1).toString();
        gameRecord.copyToGame(this.gamePanel, this, previousMove);
        statusPanel.setScoreText(this.blackScore, this.whiteScore);
        statusPanel.setPlayerText(currentPlayer.name());
        gamePanel.findAllMoves(currentPlayer);
        this.client.canMove = this.client.canMove ? false : true;
    }

    public void sendInfo(int row, int col) {
        String info = String.format("%s %s", String.valueOf(row), String.valueOf(col));
        try {
            this.client.clientThread.dataOutputStream.writeUTF("<!MOVE!> " + info);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public ChessPiece[][] getChessBoard() {
        ChessPiece[][] tempChessPiece = new ChessPiece[8][8];
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                tempChessPiece[i][j] = gamePanel.getChessBoard()[i][j];
            }
        }
        return tempChessPiece;
    }

    public void setOnePiece(int row, int col) {
        gamePanel.getChessGrids(row, col).setChessPiece(this.currentPlayer);
    }

    public int getBlackScore() {
        return this.blackScore;
    }

    public int getWhiteScore() {
        return this.whiteScore;
    }


    //    public void loadToGame(List<String> fileData) {
//        for (int i = 0; i < fileData.size(); i++) {
//            this.gamePanel.getChessGrids(loadRow(fileData.get(i)), loadCol(fileData.get(i))).onMouseClicked();
//        }
//    }
//
//    public int loadRow(String string) {
//        return Character.getNumericValue(string.charAt(string.indexOf('[') + 1));
//    }
//
//    public int loadCol(String string) {
//        return Character.getNumericValue(string.charAt(string.indexOf(']') - 1));
//    }
}
