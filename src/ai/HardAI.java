package ai;

import controller.GameController;
import model.ChessPiece;
import view.GameFrame;

import java.util.ArrayList;

public class HardAI extends Thread {
    private int deep = 4;
    protected ChessPiece[][] chessboard = new ChessPiece[8][8];
    private int[][] intboard = new int[8][8];
    private ArrayList<TreeNode>[] tree = new ArrayList[deep + 1];
    public ChessPiece color = ChessPiece.WHITE;
    protected boolean[][] reminder = new boolean[8][8];
    public final int CHESS_COUNT = 8;
    public void loadGame(ChessPiece[][] chessboard) {
        for (int i = 0; i <= 7; i++) {
            for (int j = 0; j <= 7; j++) {
                this.chessboard[i][j] = chessboard[i][j];
                if(chessboard[i][j] == ChessPiece.BLACK) intboard[i][j] = 1;
                if(chessboard[i][j] == ChessPiece.WHITE) intboard[i][j] = -1;
                else intboard[i][j] = 0;
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

        int tempValue;
            for (int i = 0; i < deep + 1; i++) {
                tree[i] = new ArrayList<>();
            }
            tree[0].add(new TreeNode(0, this.intboard));
            for (int i = 0; i < deep; i++) {
                for (int j = 0; j < tree[i].size(); j++) {
                    tree[i].get(j).addTreeNodes();
                    tree[i + 1].addAll(tree[i].get(j).getTreeNodes());
                }
            }
            for (int i = deep - 1; i >= 0; i--) {
                for (int j = 0; j < tree[i].size(); j++) {
                    if (i == deep - 1) {
                        tree[i].get(j).addValue();
                    } else {
                        if (i % 2 != 0) {
                            tree[i].get(j).addMiniValue();
                        } else {
                            tree[i].get(j).addMaxValue();
                        }
                    }
                }
            }
            for (int i = 0; i < tree[1].size(); i++) {
                if (tree[1].get(i).getValue() == tree[0].get(0).getValue()) {
                    System.out.println(tree[0].get(0).getValue());
                    bestMove[0] = tree[1].get(i).getStep()[0];
                    bestMove[1] = tree[1].get(i).getStep()[1];
                    return bestMove;
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
            try { Thread.sleep ( 500 ) ;
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




