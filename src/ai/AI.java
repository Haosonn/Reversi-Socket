package ai;

import model.ChessPiece;

public class AI {
    protected ChessPiece[][] chessboard = new ChessPiece[8][8];
    public ChessPiece color = ChessPiece.WHITE;
    protected boolean[][] reminder = new boolean[8][8];
    public final int CHESS_COUNT = 8;

}
