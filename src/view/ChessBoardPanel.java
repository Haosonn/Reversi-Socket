package view;

import client.Client;
import components.ChessGridComponent;
import game.GameRecord;
import model.ChessPiece;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class ChessBoardPanel extends JPanel
{
    private final int CHESS_COUNT = 8;
    private ChessGridComponent[][] chessGrids;
    private boolean hasMove;
    public ChessGridComponent getChessGrids(int i, int j) {
        return chessGrids[i][j];
    }

    private Image image;

    public void setImage(Image image) {
        this.image = image;
    }

    public ChessBoardPanel(int width, int height)
    {
        image = new ImageIcon("resources/Board1.png").getImage();
        this.setVisible(true);
        this.setFocusable(true);
        this.setLayout(null);
        this.setBackground(Color.BLACK);
        int length = Math.min(width, height);
        this.setSize(length, length);
        ChessGridComponent.gridSize = length / CHESS_COUNT;
        ChessGridComponent.chessSize = (int) (ChessGridComponent.gridSize * 0.8);
        System.out.printf("width = %d height = %d gridSize = %d chessSize = %d\n",
                width, height, ChessGridComponent.gridSize, ChessGridComponent.chessSize);

        initialChessGrids();//return empty chessboard
        initialGame();//add initial four chess
        findAllMoves(ChessPiece.BLACK);
        repaint();
    }

    /**
     * set an empty chessboard
     */
    public void initialChessGrids()
    {
        chessGrids = new ChessGridComponent[CHESS_COUNT][CHESS_COUNT];

        //draw all chess grids
        for (int i = 0; i < CHESS_COUNT; i++)
        {
            for (int j = 0; j < CHESS_COUNT; j++)
            {
                ChessGridComponent gridComponent = new ChessGridComponent(i, j);
                gridComponent.setLocation(j * ChessGridComponent.gridSize, i * ChessGridComponent.gridSize);
                chessGrids[i][j] = gridComponent;
                this.add(chessGrids[i][j]);

            }
        }
    }

    /**
     * initial origin four chess
     */
    public void initialGame()
    {

        chessGrids[3][3].setChessPiece(ChessPiece.BLACK);
        chessGrids[3][4].setChessPiece(ChessPiece.WHITE);
        chessGrids[4][3].setChessPiece(ChessPiece.WHITE);
        chessGrids[4][4].setChessPiece(ChessPiece.BLACK);

    }

    public void clear()
    {
        for (int i = 0; i < CHESS_COUNT; i++)
        {
            for (int j = 0; j < CHESS_COUNT; j++)
            {
                this.remove(chessGrids[i][j]);
            }
        }
    }

    public void clearReminders()
    {
        for (int i = 0; i <= 7; i++) {
            for (int j = 0; j <= 7; j++) {
                chessGrids[i][j].setReminder(false);
            }
        }
    }
    @Override
    protected void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        g.drawImage(image, 0, 0, this.getWidth(), this.getHeight(), this);
//        g.setColor(Color.BLACK);
//        g.fillRect(0, 0, this.getWidth(), this.getHeight());
    }

    public boolean checkDirection(int row, int col, ChessPiece currentPlayer, int rowDir, int colDir)
    {
        int nowRow = row + rowDir;
        int nowCol = col + colDir;
        if (!(nowRow >= 0 && nowRow < CHESS_COUNT && nowCol >= 0 && nowCol < CHESS_COUNT)) return false;
        if (chessGrids[nowRow][nowCol].getChessPiece() == null) return false;
        if (chessGrids[nowRow][nowCol].getChessPiece().getColor() == currentPlayer.getColor()) return false;
        nowRow += rowDir;
        nowCol += colDir;
        while (nowRow >= 0 && nowRow < CHESS_COUNT && nowCol >= 0 && nowCol < CHESS_COUNT)
        {
            if (chessGrids[nowRow][nowCol].getChessPiece() == null) return false;
            if (chessGrids[nowRow][nowCol].getChessPiece().getColor() == currentPlayer.getColor()) return true;
            nowRow += rowDir;
            nowCol += colDir;
        }
        return false;
    }

    public int reverse(int row, int col, ChessPiece currentPlayer, int rowDir, int colDir)
    {
        int nowRow = row + rowDir;
        int nowCol = col + colDir;
        int cnt = 0;
        while (nowRow >= 0 && nowRow < CHESS_COUNT && nowCol >= 0 && nowCol < CHESS_COUNT)
        {
            if (chessGrids[nowRow][nowCol].getChessPiece().getColor() == currentPlayer.getColor()) return cnt;
            chessGrids[nowRow][nowCol].setChessPiece(currentPlayer);
            nowRow += rowDir;
            nowCol += colDir;
            cnt++;
        }
        return cnt;

    }
    public boolean findAllMoves(ChessPiece currentPlayer){
        clearReminders();
        int[][] direction = {{0, 1}, {1, 0}, {0, -1}, {-1, 0}, {1, 1}, {1, -1}, {-1, 1}, {-1, -1}};
        this.hasMove = false;
        for (int i = 0; i <= 7; i++) {
            for (int j = 0; j <= 7; j++) {
                for (int k = 0; k <= 7; k++) {
                    if(checkDirection(i, j, currentPlayer, direction[k][0], direction[k][1]) && chessGrids[i][j].getChessPiece() == null) {
                        chessGrids[i][j].setReminder(true);
                        this.hasMove = true;
                        break;
                    }
                }
            }
        }
        repaint();
        return this.hasMove;
    }
    public int canClickGrid(int row, int col, ChessPiece currentPlayer)
    {
        if (chessGrids[row][col].getChessPiece() != null) return 0;


        if (!chessGrids[row][col].getReminder()) return 0;


        int[][] direction = {{0, 1}, {1, 0}, {0, -1}, {-1, 0}, {1, 1}, {1, -1}, {-1, 1}, {-1, -1}};
        int[] correctDir = new int[9];
        int reverseCnt = 0;
        for (int i = 0; i <= 7; i++)
        {
            if (checkDirection(row, col, currentPlayer, direction[i][0], direction[i][1]))
                correctDir[++correctDir[0]] = i;
        }
        if (correctDir[0] > 0)
        {

            clearReminders();
            for (int i = 1; i <= correctDir[0]; i++)
            {
                reverseCnt += reverse(row, col, currentPlayer, direction[correctDir[i]][0], direction[correctDir[i]][1]);
            }
            chessGrids[row][col].setChessPiece(currentPlayer);
            repaint();
            return reverseCnt;
        }
        return reverseCnt;
    }


    public ChessPiece[][] getChessBoard(){
        ChessPiece[][] chessBoard = new ChessPiece[8][8];
        for (int i = 0; i <= 7; i++) {
            for (int j = 0; j <= 7; j++) {
                chessBoard[i][j] = this.chessGrids[i][j].getChessPiece();
            }
        }
        return chessBoard;
    }



}
