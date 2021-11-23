package view;

import components.ChessGridComponent;
import model.ChessPiece;

import javax.swing.*;
import java.awt.*;

public class ChessBoardPanel extends JPanel
{
    private final int CHESS_COUNT = 8;
    private ChessGridComponent[][] chessGrids;

    public ChessBoardPanel(int width, int height)
    {
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

    @Override
    protected void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, this.getWidth(), this.getHeight());
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
    public int canClickGrid(int row, int col, ChessPiece currentPlayer)
    {
        //System.out.printf(chessGrids[row][col].getChessPiece().getColor().toString());
        if (chessGrids[row][col].getChessPiece() != null) return 0;
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
            for (int i = 1; i <= correctDir[0]; i++)
            {
                reverseCnt += reverse(row, col, currentPlayer, direction[correctDir[i]][0], direction[correctDir[i]][1]);
            }
            repaint();
            return reverseCnt;
        }
        return reverseCnt;
    }

}
