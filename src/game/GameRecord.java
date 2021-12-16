package game;

import controller.GameController;
import model.ChessPiece;
import view.ChessBoardPanel;
import view.GameFrame;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GameRecord {
    private ChessPiece[][] chessboard = new ChessPiece[8][8];
    private ChessPiece currentPlayer;
    private int blackCnt;
    private int whiteCnt;
    private int[] step = {-1, -1};

    public int[] getStep() {
        return step;
    }

    public GameRecord(List<String> gameRecord) {
        ;
    }

    public GameRecord() {
        ;
    }

    @Override
    public String toString() {
        String line = new String();
        for (int i = 0; i <= 7; i++) {
            for (int j = 0; j <= 7; j++) {
                if (chessboard[i][j] == null) line += "0 ";
                if (chessboard[i][j] == ChessPiece.BLACK) line += "1 ";
                if (chessboard[i][j] == ChessPiece.WHITE) line += "-1 ";
            }
        }
        if (currentPlayer == ChessPiece.BLACK) line += "1 ";
        if (currentPlayer == ChessPiece.WHITE) line += "-1 ";
        line += String.format("%d %d %d %d\n", blackCnt, whiteCnt, step[0], step[1]);
        return line;
    }
    //String = chessboard + currentplayer + blackcnt + whitecnt + row + col
    public void setChessboard(ChessPiece[][] chessboard) {
        for (int i = 0; i <= 7; i++) {
            for (int j = 0; j <= 7; j++) {
                if (chessboard[i][j] == null) {
                    this.chessboard[i][j] = null;
                    continue;
                }
                if(chessboard[i][j] == ChessPiece.BLACK)
                    this.chessboard[i][j] = ChessPiece.BLACK;
                if(chessboard[i][j] == ChessPiece.WHITE)
                    this.chessboard[i][j] = ChessPiece.WHITE;
            }
        }
    }

    public void setCurrentPlayer(ChessPiece currentPlayer) {
        if(currentPlayer == ChessPiece.BLACK)
            this.currentPlayer = ChessPiece.BLACK;
        else
            this.currentPlayer = ChessPiece.WHITE;
    }

    public void setBlackCnt(int blackCnt) {
        this.blackCnt = blackCnt;
    }

    public void setWhiteCnt(int whiteCnt) {
        this.whiteCnt = whiteCnt;
    }

    public int getBlackCnt() {
        return blackCnt;
    }

    public int getWhiteCnt() {
        return whiteCnt;
    }

    public ChessPiece getCurrentPlayer() {
        return currentPlayer;
    }

    public void copyToGame(ChessBoardPanel gamePanel, GameController controller, String fileData) {
        String[] data = fileData.split(" ");
        int row = 0;
        int col = 0;
        int cnt = 0;
        while (true) {

            int player = Integer.parseInt(data[cnt]);
            if (player == 1) gamePanel.getChessGrids(row, col).setChessPiece(ChessPiece.BLACK);
            if (player == -1) gamePanel.getChessGrids(row, col).setChessPiece(ChessPiece.WHITE);
            if (player == 0) gamePanel.getChessGrids(row, col).setChessPiece(null);
            cnt++;
            col += 1;
            if (col == 8) {
                col = 0;
                row += 1;
            }
            if (row == 8) break;
        }
        int player = Integer.parseInt(data[cnt]);
        if (player == 1) controller.setCurrentPlayer(ChessPiece.BLACK);
        if (player == -1) controller.setCurrentPlayer(ChessPiece.WHITE);
        cnt++;
        controller.setBlackScore(Integer.parseInt(data[cnt]));
        cnt++;
        controller.setWhiteScore(Integer.parseInt(data[cnt]));
    }

    public void setStep(int[] step) {
        this.step[0] = step[0];
        this.step[1] = step[1];
    }
}
