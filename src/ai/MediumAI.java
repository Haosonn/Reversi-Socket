package ai;

import controller.GameController;
import model.ChessPiece;
import view.GameFrame;

public class MediumAI extends Thread {

    protected ChessPiece[][] chessboard = new ChessPiece[8][8];
    public ChessPiece color = ChessPiece.BLACK;
    protected boolean[][] reminder = new boolean[8][8];
    public final int CHESS_COUNT = 8;
    public int[][] value = {{90,-60,10,10,10,10,-60,90},
                            {-60,-80,5,5,5,5,-80,-60},
                            {10,5,1,1,1,1,5,10},
                            {10,5,1,1,1,1,5,10},
                            {10,5,1,1,1,1,5,10},
                            {10,5,1,1,1,1,5,10},
                            {-60,-80,5,5,5,5,-80,-60},
                            {90,-60,10,10,10,10,-60,90}};
    public void loadGame(ChessPiece[][] chessboard) {
        for (int i = 0; i <= 7; i++) {
            for (int j = 0; j <= 7; j++) {
                this.chessboard[i][j] = chessboard[i][j];
            }
        }
    }

    public void clearReminders()
    {
        for (int i = 0; i <= 7; i++) {
            for (int j = 0; j <= 7; j++) {
                this.reminder[i][j] = false;
            }
        }
    }

    public void findAllMoves(ChessPiece currentPlayer){
        this.clearReminders();
        int[][] direction = {{0, 1}, {1, 0}, {0, -1}, {-1, 0}, {1, 1}, {1, -1}, {-1, 1}, {-1, -1}};
        for (int i = 0; i <= 7; i++) {
            for (int j = 0; j <= 7; j++) {
                for (int k = 0; k <= 7; k++) {
                    if(checkDirection(i, j, currentPlayer, direction[k][0], direction[k][1]) && chessboard[i][j] == null) {
                        reminder[i][j] = true;
                        break;
                    }
                }
            }
        }
    }

    public int[] findBestMove() {
        findAllMoves(color);
        int[] bestMove = new int[2];
        int mostValue = -999;
        int tempValue;
        for (int i = 0; i <= 7; i++) {
            for (int j = 0; j <= 7; j++) {
                if (this.reminder[i][j]) {
                    tempValue = this.value[i][j];
                    if(tempValue > mostValue){
                        mostValue = tempValue;
                        bestMove[0] = i;
                        bestMove[1] = j;
                    }
                }
            }
        }
        return bestMove;
    }

    public boolean checkDirection(int row, int col, ChessPiece currentPlayer, int rowDir, int colDir) {
        int nowRow = row + rowDir;
        int nowCol = col + colDir;
        if (!(nowRow >= 0 && nowRow < CHESS_COUNT && nowCol >= 0 && nowCol < CHESS_COUNT)) return false;
        if (chessboard[nowRow][nowCol] == null) return false;
        if (chessboard[nowRow][nowCol].getColor() == currentPlayer.getColor()) return false;
        nowRow += rowDir;
        nowCol += colDir;
        while (nowRow >= 0 && nowRow < CHESS_COUNT && nowCol >= 0 && nowCol < CHESS_COUNT) {
            if (chessboard[nowRow][nowCol] == null) return false;
            if (chessboard[nowRow][nowCol].getColor() == currentPlayer.getColor()) return true;
            nowRow += rowDir;
            nowCol += colDir;
        }
        return false;
    }

    public int cntReverse(int row, int col, boolean ifReverse) {
        int[][] direction = {{0, 1}, {1, 0}, {0, -1}, {-1, 0}, {1, 1}, {1, -1}, {-1, 1}, {-1, -1}};
        int[] correctDir = new int[9];
        int reverseCnt = 0;
        for (int i = 0; i <= 7; i++) {
            if (checkDirection(row, col, color, direction[i][0], direction[i][1]))
                correctDir[++correctDir[0]] = i;
        }
        if (correctDir[0] > 0) {
            for (int i = 1; i <= correctDir[0]; i++) {
                reverseCnt += reverse(row, col, color, direction[correctDir[i]][0], direction[correctDir[i]][1], ifReverse);
            }
            if(ifReverse) chessboard[row][col] = (color == ChessPiece.BLACK) ? ChessPiece.BLACK : ChessPiece.WHITE;
        }
        return reverseCnt;
    }

    public int reverse ( int row, int col, ChessPiece color,int rowDir, int colDir, boolean ifReverse){
        int nowRow = row + rowDir;
        int nowCol = col + colDir;
        int cnt = 0;
        while (nowRow >= 0 && nowRow < 8 && nowCol >= 0 && nowCol < 8) {
            if (chessboard[nowRow][nowCol].getColor() == color.getColor()) return cnt;
            if (ifReverse) chessboard[nowCol][nowRow] = (color == ChessPiece.BLACK) ? ChessPiece.BLACK : ChessPiece.WHITE;
            nowRow += rowDir;
            nowCol += colDir;
            cnt++;
        }
        return cnt;

    }

    @Override
    public void run() {
        boolean flag = true;
        while(flag){
            try { Thread.sleep ( 250 ) ;
            } catch (InterruptedException ie){}
            if(GameFrame.controller.getBlackScore() + GameFrame.controller.getWhiteScore() == 64 || GameFrame.controller.getBlackScore() == 0 || GameFrame.controller.getWhiteScore() == 0 )
                GameFrame.controller.endGame();
            if(!GameFrame.controller.canClick())
                GameFrame.controller.swapPlayer();
            if(GameFrame.controller.getCurrentPlayer() == this.color){
                if(!GameFrame.controller.canClick()) {
                    GameFrame.controller.swapPlayer();
                    continue;
                }
                this.loadGame(GameFrame.controller.getChessBoard());
                int bestMove[] = this.findBestMove();
                this.cntReverse(bestMove[0],bestMove[1],true);
                GameFrame.controller.getGamePanel().canClickGrid(bestMove[0],bestMove[1],this.color);
                GameFrame.controller.swapPlayer();
                GameFrame.controller.updateScore();
                GameFrame.controller.canClick();
                GameFrame.controller.getGamePanel().repaint();
                GameFrame.controller.addToHistory();


            }
        }
    }
}




