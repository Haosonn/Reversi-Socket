package ai;

import model.ChessPiece;
import view.GameFrame;

public class ThreadForAI extends Thread {
    private int color;
    private int deep;
    private int[][] chessBoard = new int[8][8];
    private Node root;
    public boolean exit = false;

    public void setDeep(int deep) {
        this.deep = deep;
    }

    public ThreadForAI(int color, int deep) {
        this.color = color;
        this.deep = deep;
    }


    private void loadChessBoard() {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                this.chessBoard[i][j] = 0;
                if (GameFrame.controller.getGamePanel().getChessGrids(i, j).getChessPiece() == null)
                    continue;
                this.chessBoard[i][j] = (GameFrame.controller.getGamePanel().getChessGrids(i, j).getChessPiece() == ChessPiece.BLACK) ? 1 : -1;
            }
        }
    }

    @Override
    public void run() {
        while ((this.color == 1 && GameFrame.controller.isBlackAIModeOn()) || (this.color == -1 && GameFrame.controller.isWhiteAIModeOn())) {
            if (exit) {
                System.out.println("Game End");
                break;
            }
            if (boradSpace() == 0) {
                System.out.println("Game End");
                break;
            }
            try {
                Thread.sleep(50);
            } catch (InterruptedException ie) {
            }

            if (GameFrame.controller.getCurrentPlayer() == ((this.color == 1) ? ChessPiece.BLACK : ChessPiece.WHITE)) {
                try {
                    Thread.sleep(300);
                } catch (InterruptedException ie) {
                }
                loadChessBoard();
                if (boradSpace() <= 10 && this.deep != 1) {
                    this.deep = 10;
                    System.out.println((this.color == 1 ? "Black" : "White") + " Final Game Searching");
                    root = new Node(this.deep, -this.color, this.chessBoard);
                    int value = root.getFinalValue();
                    if (value == 1) {
                        System.out.println((this.color == 1 ? "Black" : "White") + " Final Game Searching Success");
                        for (int i = 0; i < root.getNodes().size(); i++) {
                            if (root.getNodes().get(i).getBeta() == value) {
                                if (GameFrame.controller.getCurrentPlayer() == ((this.color == 1) ? ChessPiece.BLACK : ChessPiece.WHITE)) {
                                    GameFrame.controller.getGamePanel().getChessGrids
                                            (root.getNodes().get(i).getStep()[0], root.getNodes().get(i).getStep()[1]).onMouseClicked();
                                }
                                break;
                            }
                        }
                    } else {
                        System.out.println((this.color == 1 ? "Black" : "White") + " Final Game Searching Fail");
                        root = new Node(this.deep, -this.color, this.chessBoard);
                        value = root.getValue();
                        for (int i = 0; i < root.getNodes().size(); i++) {
                            if (root.getNodes().get(i).getBeta() == value) {
                                if (GameFrame.controller.getCurrentPlayer() == ((this.color == 1) ? ChessPiece.BLACK : ChessPiece.WHITE)) {
                                    GameFrame.controller.getGamePanel().getChessGrids
                                            (root.getNodes().get(i).getStep()[0], root.getNodes().get(i).getStep()[1]).onMouseClicked();

                                    System.out.println(this.deep);

                                }
                                break;
                            }
                        }
                    }
                } else {
                    root = new Node(this.deep, -this.color, this.chessBoard);
                    int value = root.getValue();
                    for (int i = 0; i < root.getNodes().size(); i++) {
                        if (root.getNodes().get(i).getBeta() == value) {
                            if (GameFrame.controller.getCurrentPlayer() == ((this.color == 1) ? ChessPiece.BLACK : ChessPiece.WHITE)) {
                                GameFrame.controller.getGamePanel().getChessGrids
                                        (root.getNodes().get(i).getStep()[0], root.getNodes().get(i).getStep()[1]).onMouseClicked();

                                System.out.println(this.deep);

                            }
                            break;
                        }
                    }
                }
            }
        }
    }

    public int boradSpace() {
        int space = 64;
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (this.chessBoard[i][j] != 0) {
                    space--;
                }
            }
        }
        return space;
    }
}

