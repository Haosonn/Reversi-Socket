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
    public GameRecord(List<String> gameRecord){
        ;
    }
    public GameRecord(){
        ;
    }
    @Override
    public String toString() {
        String finalString = "";
        for (int i = 0; i <= 7; i++) {
            for (int j = 0; j <= 7; j++) {
                if(chessboard[i][j] == null) finalString += " 0";
                if(chessboard[i][j] == ChessPiece.BLACK) finalString += " 1";
                if(chessboard[i][j] == ChessPiece.WHITE) finalString += " -1";
            }
            finalString += '\n';
        }

        if(currentPlayer == ChessPiece.BLACK) finalString += " 1";
        if(currentPlayer == ChessPiece.WHITE) finalString += " -1";

        finalString += String.format(" %d %d",blackCnt,whiteCnt);
        return finalString;
    }
    public List<String> toStringList() {
        List<String> lines = new ArrayList<>();
        String line = new String();
        for (int i = 0; i <= 7; i++) {
            line = "";
            for (int j = 0; j <= 7; j++) {
                if(chessboard[i][j] == null) line += "0 ";
                if(chessboard[i][j] == ChessPiece.BLACK) line += "1 ";
                if(chessboard[i][j] == ChessPiece.WHITE) line += "-1 ";
            }
            lines.add(line);
        }
        line = "";
        if(currentPlayer == ChessPiece.BLACK) line += "1 ";
        if(currentPlayer == ChessPiece.WHITE) line += "-1 ";
        line += String.format("%d %d",blackCnt,whiteCnt);
        lines.add(line);
        return lines;
    }
    //String = chessboard + currentplayer + blackcnt + whitecnt
    public void setChessboard(ChessPiece[][] chessboard) {
        for (int i = 0; i <= 7; i++) {
            for (int j = 0; j <= 7; j++) {
                if (chessboard[i][j] == null) {
                    this.chessboard[i][j] = null;
                    continue;
                }
                this.chessboard[i][j] = chessboard[i][j];
            }
        }
    }
    public void setCurrentPlayer(ChessPiece currentPlayer) { this.currentPlayer = currentPlayer; }
    public void setBlackCnt(int blackCnt) { this.blackCnt = blackCnt; }
    public void setWhiteCnt(int whiteCnt) { this.whiteCnt = whiteCnt; }

    public int getBlackCnt() { return blackCnt; }
    public int getWhiteCnt() { return whiteCnt; }
    public ChessPiece getCurrentPlayer() { return currentPlayer; }
    public void copyToGame(ChessBoardPanel gamePanel, GameController controller, List<String> fileData){
        String[] data = fileData.get(0).split(" ");
        int row = 0;
        int col = 0;
        int cnt = 0;
        while(true){

            int player = Integer.parseInt(data[cnt]);
            if(player == 1) gamePanel.getChessGrids(row,col).setChessPiece(ChessPiece.BLACK);
            if(player == -1) gamePanel.getChessGrids(row,col).setChessPiece(ChessPiece.WHITE);
            if(player == 0) gamePanel.getChessGrids(row,col).setChessPiece(null);
            cnt++; col += 1;
            if(col == 8) { col = 0; row += 1;}
            if(row == 8) break;
        }
        int player = Integer.parseInt(data[cnt]);
        if(player == 1) controller.setCurrentPlayer(ChessPiece.BLACK);
        if(player == -1) controller.setCurrentPlayer(ChessPiece.WHITE);
        cnt++;
        controller.setBlackScore(Integer.parseInt(data[cnt]));
        cnt++;
        controller.setWhiteScore(Integer.parseInt(data[cnt]));



    }
}
