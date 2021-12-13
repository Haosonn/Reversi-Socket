package components;

import model.*;
import view.GameFrame;

import javax.swing.*;
import java.awt.*;

public class ChessGridComponent extends BasicComponent {
    public static int chessSize;
    public static int gridSize;
    public static Color gridColor = new Color(255, 150, 50);

    public static final Color none = new Color(0, 0, 0, 0);
    public static boolean isCustom = false;
    public static Color primaryColor = Color.DARK_GRAY;
    public static Color secondaryColor = Color.LIGHT_GRAY;

    private Image blackChess, whiteChess;
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
        if (!GameFrame.controller.client.canMove && GameFrame.controller.client.onlineMode) return;
        if (GameFrame.controller.canClick(row, col) || GameFrame.controller.isCheatingBtnOn()) {
//            if (this.chessPiece == null) {
            this.reminder = false;
//                int[] step = {row, col};
//                GameFrame.controller.getThisStep().getStep().add(step);
//                for (int i = 0; i < GameFrame.controller.getThisStep().getStep().size(); i++) {
//                    System.out.println(Arrays.toString(GameFrame.controller.getThisStep().getStep().get(i)));
//                }
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
//        }
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
//        g.setColor(gridColor);
//        g.fillRect(1, 1, this.getWidth() - 2, this.getHeight() - 2);
        blackChess = new ImageIcon("resources/Black.png").getImage();
        whiteChess = new ImageIcon("resources/White.png").getImage();
        if (this.chessPiece != null) {
//            g.setColor(chessPiece.getColor());
//            g.fillOval((gridSize - chessSize) / 2, (gridSize - chessSize) / 2, chessSize, chessSize);
            if (this.chessPiece == ChessPiece.BLACK)
                g.drawImage(blackChess, (gridSize - chessSize) / 2, (gridSize - chessSize) / 2, chessSize, chessSize, this);
            else
                g.drawImage(whiteChess, (gridSize - chessSize) / 2, (gridSize - chessSize) / 2, chessSize, chessSize, this);
        }
        if (this.reminder) {
            g.setColor(new Color(0, 0, 0, 70));
            g.fillOval((gridSize - chessSize) / 2, (gridSize - chessSize) / 2, chessSize, chessSize);
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
