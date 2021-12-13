package controller;

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

    public EasyAI easyAI;
    public MediumAI mediumAI;
    public HardAI hardAI;
    public Client client;
    private ChessBoardPanel gamePanel;
    private StatusPanel statusPanel;
    private ChessPiece currentPlayer;

    private int blackScore;
    private int whiteScore;
    private boolean cheatingBtnOn = false;

    private ArrayList<GameRecord> gameHistory = new ArrayList();

    public boolean isCheatingBtnOn() {
        return cheatingBtnOn;
    }

    public void setCheatingBtnOn(boolean cheatingBtnOn) {
        this.cheatingBtnOn = cheatingBtnOn;
    }

    public GameRecord getThisStep()
    {
        return gameHistory.get(gameHistory.size()-1);
    }

    public GameController(ChessBoardPanel gamePanel, StatusPanel statusPanel, Client client) {
        this.gamePanel = gamePanel;
        this.statusPanel = statusPanel;
        this.currentPlayer = ChessPiece.BLACK;
        blackScore = 2;
        whiteScore = 2;
        this.client = client;
        if(client.aiMode)
        {
//            easyAI = new EasyAI();
//            easyAI.start();
//            mediumAI = new MediumAI();
//            mediumAI.start();
            hardAI = new HardAI();
            hardAI.start();
        }

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

    public void updateScore(){
        blackScore = whiteScore = 0;
        for (int i = 0; i <= 7; i++) {
            for (int j = 0; j <= 7; j++) {
                if(gamePanel.getChessBoard()[i][j] == ChessPiece.BLACK) blackScore++;
                else if(gamePanel.getChessBoard()[i][j] == ChessPiece.WHITE) whiteScore++;
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
        //todo: read date from file
        List<String> fileData = new ArrayList<>();
        GameRecord gameRecord = new GameRecord();
        try {
            FileReader fileReader = new FileReader(fileName);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                fileData.add(line);
            }

            gameRecord.copyToGame(this.gamePanel, this, fileData);
            statusPanel.setScoreText(this.blackScore, this.whiteScore);
            statusPanel.setPlayerText(currentPlayer.name());
            gamePanel.findAllMoves(currentPlayer);

            for (int i = 1; i < fileData.size(); i++) {
                int[] arrayLine = {Character.getNumericValue(fileData.get(i).charAt(0)),Character.getNumericValue(fileData.get(i).charAt(2))};
                gameRecord.getStep().clear();
                gameRecord.getStep().add(arrayLine);
            }

            fileData.forEach(System.out::println);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void readData(String data){
        data = data.substring(9);
        List<String> fileData = new ArrayList<>();
        fileData.add(data);
        GameRecord gameRecord = new GameRecord();
        gameRecord.copyToGame(this.gamePanel, this, fileData);
        statusPanel.setScoreText(this.blackScore, this.whiteScore);
        statusPanel.setPlayerText(currentPlayer.name());
        gamePanel.findAllMoves(currentPlayer);
        this.addToHistory();
    }
    public void writeDataToFile(String filePath) {
        //todo: write data into file
        try {
            FileWriter fileWriter = new FileWriter(filePath);
            BufferedWriter writer = new BufferedWriter(fileWriter);
            GameRecord gameRecord = new GameRecord();
            setThisStep(gameRecord);
            List<String> lines = gameRecord.toStringList();
            for (String line : lines
            ) {
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
        class EndDialog extends JDialog{
            public EndDialog(int result){
                super();
                Container container = getContentPane();
                if(result == 1)
                    container.add(new JLabel("BLACK WIN"));
                else
                    container.add(new JLabel("WHITE WIN"));
                setBounds(120,120,100,100);
                setVisible(true);
            }
        }
        if (this.blackScore > this.whiteScore) {
            //EndDialog endDialog = new EndDialog(1);
            statusPanel.setGameResult("BLACK WIN");
        } else {
            if (this.whiteScore > this.blackScore) {
                statusPanel.setGameResult("WHITE WIN");
                //EndDialog endDialog = new EndDialog(-1);
            } else {
                statusPanel.setGameResult("DRAW");
            }
        }
    }

    public void setThisStep (GameRecord gameRecord){
        gameRecord.setChessboard(gamePanel.getChessBoard());
        gameRecord.setBlackCnt(blackScore);
        gameRecord.setWhiteCnt(whiteScore);
        gameRecord.setCurrentPlayer(currentPlayer);
    }

    public void addToHistory(){
        GameRecord thisStep = new GameRecord();
        setThisStep(thisStep);
        gameHistory.add(thisStep);
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

    public void undoRequest(){
        try{
            this.client.clientThread.dataOutputStream.writeUTF("<!UNDOREQUEST!>");
        } catch (Exception e){
            e.printStackTrace();
        }

    }

    public void undo(){
        GameRecord gameRecord = new GameRecord();
        if(gameHistory.size() == 1) return;
        gameHistory.remove(gameHistory.size()-1);
        List<String> previousMove = gameHistory.get(gameHistory.size()-1).toStringList();
        gameRecord.copyToGame(this.gamePanel, this, previousMove);
        statusPanel.setScoreText(this.blackScore, this.whiteScore);
        statusPanel.setPlayerText(currentPlayer.name());
        gamePanel.findAllMoves(currentPlayer);
        this.client.canMove = this.client.canMove ? false:true;
    }
    public void sendInfo(){
        GameRecord infoTosend = new GameRecord();
        setThisStep(infoTosend);
        String info = infoTosend.toString();
        try {
            this.client.clientThread.dataOutputStream.writeUTF("<!MOVE!> "+ info);
        } catch (Exception e){
            e.printStackTrace();
        }

    }
    public ChessPiece[][] getChessBoard(){
        ChessPiece[][] tempChessPiece = new ChessPiece[8][8];
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                tempChessPiece[i][j] = gamePanel.getChessBoard()[i][j];
            }
        }
        return tempChessPiece;
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
