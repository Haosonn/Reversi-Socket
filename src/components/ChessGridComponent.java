package components;

import controller.GameController;
import model.*;
import view.GameFrame;

import java.awt.*;

public class  ChessGridComponent extends BasicComponent {
    public static int chessSize;
    public static int gridSize;
    public static Color gridColor = new Color(255, 150, 50);

    private ChessPiece chessPiece;
    private int row;
    private int col;
    private boolean reminder;
    public ChessGridComponent(int row, int col) {
        this.setSize(gridSize, gridSize);

        this.row = row;
        this.col = col;
    }

    @Override
    public void onMouseClicked() {
        System.out.printf("%s clicked (%d, %d)\n", GameFrame.controller.getCurrentPlayer(), row, col);
        //todo: complete mouse click method
        if (GameFrame.controller.canClick(row, col)) {
            if (this.chessPiece == null) {
                this.reminder = false;
                this.chessPiece = GameFrame.controller.getCurrentPlayer();
                GameFrame.controller.addScore(1);
                GameFrame.controller.swapPlayer();
                if (!GameFrame.controller.canClick()) {
                    GameFrame.controller.swapPlayer();
                    if(!GameFrame.controller.canClick()) {
                        GameFrame.controller.endGame();
                    }
                }
            }
            repaint();
        }
    }




    public ChessPiece getChessPiece() {
        return chessPiece;
    }

    public void setChessPiece(ChessPiece chessPiece) {
        this.chessPiece = chessPiece;
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }

    public void drawPiece(Graphics g) {
        g.setColor(gridColor);
        g.fillRect(1, 1, this.getWidth() - 2, this.getHeight() - 2);
        if (this.chessPiece != null) {
            g.setColor(chessPiece.getColor());
            g.fillOval((gridSize - chessSize) / 2, (gridSize - chessSize) / 2, chessSize, chessSize);
        }
        if (this.reminder) {
            g.setColor(Color.GRAY);
            g.fillOval((gridSize - chessSize) / 2, (gridSize - chessSize) / 2, chessSize, chessSize);
        }
    }


    @Override
    public void paintComponent(Graphics g) {
        super.printComponents(g);
        drawPiece(g);
    }

    public void setReminder(boolean status){
        this.reminder = status;
    }

    public boolean getReminder(){
        return this.reminder;
    }
}