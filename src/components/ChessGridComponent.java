package components;

import animation.ThreadForNewPiece;
import animation.ThreadForReversing;
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

    public int getReversingColor() {
        return reversingColor;
    }

    public void setReversingColor(int reversingColor) {
        this.reversingColor = reversingColor;
    }

    private boolean newPiece;
    private Image black, white;

    private int newPieceAlpha = 100;
    private ThreadForNewPiece threadForNewPiece;
    private int reversingSize = chessSize;
    private int reversingColor;
    private ThreadForReversing threadForReversing;

    public int getReversingSize() {
        return reversingSize;
    }

    public void setReversingSize(int reversingSize) {
        this.reversingSize = reversingSize;
    }

    public int getNewPieceAlpha() {
        return newPieceAlpha;
    }

    public void setNewPieceAlpha(int newPieceAlpha) {
        this.newPieceAlpha = newPieceAlpha;
    }

    public void setNewPiece(boolean newPiece) {
        this.newPiece = newPiece;
    }

    public ThreadForReversing getThreadForReversing() {
        return threadForReversing;
    }

    public void setThreadForReversing(ThreadForReversing threadForReversing) {
        this.threadForReversing = threadForReversing;
    }

    public ChessGridComponent(int row, int col) {
        this.setSize(gridSize, gridSize);

        this.row = row;
        this.col = col;
    }

    @Override
    public void onMouseClicked() {
        System.out.printf("%s clicked (%d, %d)\n", GameFrame.controller.getCurrentPlayer(), row, col);
        //todo: complete mouse click method
        //if (!GameFrame.controller.client.canMove && GameFrame.controller.client.onlineMode) return;
        if (GameFrame.controller.canClick(row, col) || GameFrame.controller.isCheatingBtnOn()) {

            this.reminder = false;
            GameFrame.controller.setOnePiece(row, col);
            GameFrame.controller.getGamePanel().getChessGrids(GameFrame.controller.getGamePanel().getNewPiece()[0], GameFrame.controller.getGamePanel().getNewPiece()[1]).setNewPiece(false);
            GameFrame.controller.getGamePanel().setNewPiece(row, col);
            this.reversingColor = (this.getChessPiece() == ChessPiece.BLACK) ? 1 : -1;
            this.newPiece = true;
            this.threadForNewPiece = new ThreadForNewPiece(this);
            this.threadForNewPiece.start();
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
                GameFrame.controller.sendInfo(row, col);
            GameFrame.controller.addToHistory(row, col);
        }
        repaint();
    }


    public ChessPiece getChessPiece() {
        return chessPiece;
    }

    public void setChessPiece(ChessPiece chessPiece) {
        this.chessPiece = chessPiece;
        this.reversingColor = (this.getChessPiece() == ChessPiece.BLACK) ? 1 : -1;
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }

    public boolean isNewPiece() {
        return newPiece;
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
            if (this.reversingColor == 1) {
                g.drawImage(black, (gridSize - reversingSize) / 2, (gridSize - chessSize) / 2, reversingSize, chessSize, this);
            } else {
                g.drawImage(white, (gridSize - reversingSize) / 2, (gridSize - chessSize) / 2, reversingSize, chessSize, this);
            }
        }
        if (this.reminder) {
            g.setColor(new Color(0, 0, 0, 10));
            g.fillOval((gridSize - chessSize + 4) / 2, (gridSize - chessSize + 4) / 2, chessSize - 4, chessSize - 4);
            g.setColor(new Color(0, 0, 0, 10));
            g.fillOval((gridSize - chessSize) / 2, (gridSize - chessSize) / 2, chessSize, chessSize);
            g.setColor(new Color(0, 0, 0, 50));
            g.fillOval((gridSize - chessSize - 4) / 2, (gridSize - chessSize - 4) / 2, chessSize + 4, chessSize + 4);
        }
        if (this.newPiece) {
            g.setColor(new Color(255, 0, 0, newPieceAlpha));
            g.fillOval((gridSize - chessSize / 2) / 2, (gridSize - chessSize / 2) / 2, chessSize / 2, chessSize / 2);
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
