package controller;

import model.ChessPiece;
import view.*;
import game.GameRecord;

import javax.swing.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;


public class GameController {


    private ChessBoardPanel gamePanel;
    private StatusPanel statusPanel;
    private ChessPiece currentPlayer;
    private GameRecord gameRecord = new GameRecord();
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
        GameRecord gameRecord = new GameRecord();
        try {
            FileReader fileReader = new FileReader(fileName);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                fileData.add(line);
            }

            gameRecord.copyToGame(this.gamePanel, this, fileData);
            statusPanel.setScoreText(this.blackScore,this.whiteScore);
            statusPanel.setPlayerText(currentPlayer.name());
            gamePanel.findAllMoves(currentPlayer);

            fileData.forEach(System.out::println);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void writeDataToFile(String filePath) {
        //todo: write data into file
        try {
            FileWriter fileWriter = new FileWriter(filePath);
            BufferedWriter writer = new BufferedWriter(fileWriter);

                    gameRecord.setChessboard(gamePanel.getChessBoard());
                    gameRecord.setBlackCnt(blackScore);
                    gameRecord.setWhiteCnt(whiteScore);
                    gameRecord.setCurrentPlayer(currentPlayer);


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

    public void setBlackScore(int blackScore) {
        this.blackScore = blackScore;
    }

    public void setWhiteScore(int whiteScore) {
        this.whiteScore = whiteScore;
    }

    public void setCurrentPlayer(ChessPiece currentPlayer) {
        this.currentPlayer = currentPlayer;
    }
}
