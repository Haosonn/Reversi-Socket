package components;

import model.*;
import view.GameFrame;

import javax.swing.*;
import java.awt.*;

public class ChessGridComponent extends BasicComponent {
    public static int chessSize;
    public static int gridSize;
    public static final Color none = new Color(0, 0, 0, 0);
    public static boolean isCustom = false;
    public static Color primaryColor = Color.DARK_GRAY;
    public static Color secondaryColor = Color.LIGHT_GRAY;

    private ChessPiece chessPiece;
    private int row;
    private int col;
    private boolean reminder;
    private Image black, white;

    public ChessGridComponent(int row, int col) {
        this.setSize(gridSize, gridSize);

        this.row = row;
        this.col = col;
    }

    @Override
    public void onMouseClicked() {
        System.out.printf("%s clicked (%d, %d)\n", GameFrame.controller.getCurrentPlayer(), row, col);
        //todo: complete mouse click method
        if (!GameFrame.controller.client.canMove && GameFrame.controller.client.onlineMode) return;
        if (GameFrame.controller.canClick(row, col) || GameFrame.controller.isCheatingBtnOn()) {

            this.reminder = false;
            GameFrame.controller.setOnePiece(row, col);
            GameFrame.controller.updateScore();
            GameFrame.controller.swapPlayer();
            if (!GameFrame.controller.canClick()) {
                if (!GameFrame.controller.client.aiMode)
                    GameFrame.controller.swapPlayer();
                if (!GameFrame.controller.canClick()) {
                    GameFrame.controller.endGame();
                }
            }
            GameFrame.controller.client.canMove = false;
            if (GameFrame.controller.client.onlineMode)
                GameFrame.controller.sendInfo();
            GameFrame.controller.addToHistory();
        }
        repaint();
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
        g.setColor(((this.col + this.row) % 2 != 0) ? primaryColor : secondaryColor);
        if (!isCustom) {
            g.setColor(none);
        }
        g.fillRect(0, 0, this.getWidth(), this.getHeight());
        black = new ImageIcon("resources/Black.png").getImage();
        white = new ImageIcon("resources/White.png").getImage();
        if (this.chessPiece != null) {
            if (this.chessPiece.getColor() == Color.BLACK) {
                g.drawImage(black, (gridSize - chessSize + 4) / 2, (gridSize - chessSize + 4) / 2, chessSize - 4, chessSize - 4, this);
            } else {
                g.drawImage(white, (gridSize - chessSize + 4) / 2, (gridSize - chessSize + 4) / 2, chessSize - 4, chessSize - 4, this);
            }
        }
        if (this.reminder) {
            g.setColor(new Color(0, 0, 0, 15));
            g.fillOval((gridSize - chessSize + 4) / 2, (gridSize - chessSize + 4) / 2, chessSize - 4, chessSize - 4);
            g.setColor(new Color(0, 0, 0, 15));
            g.fillOval((gridSize - chessSize) / 2, (gridSize - chessSize) / 2, chessSize, chessSize);
            g.setColor(new Color(0, 0, 0, 50));
            g.fillOval((gridSize - chessSize - 4) / 2, (gridSize - chessSize - 4) / 2, chessSize + 4, chessSize + 4);

        }
    }


    @Override
    public void paintComponent(Graphics g) {
        super.printComponents(g);
        drawPiece(g);
    }

    public void setReminder(boolean status) {
        this.reminder = status;
    }

    public boolean getReminder() {
        return this.reminder;
    }
}
