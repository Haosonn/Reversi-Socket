package animation;

import components.ChessGridComponent;
import view.GameFrame;

public class ThreadForNewPiece extends Thread {
    private ChessGridComponent chessGrid;

    public ThreadForNewPiece(ChessGridComponent chessGrid) {
        this.chessGrid = chessGrid;
    }

    @Override
    public void run() {
        chessGrid.setNewPieceAlpha(100);
        while (chessGrid.isNewPiece() && !GameFrame.controller.gameEnd) {
            for (int i = 0; i < 10; i++) {
                chessGrid.setNewPieceAlpha(chessGrid.getNewPieceAlpha() - 10);
                chessGrid.repaint();
                try {
                    Thread.sleep(20);
                } catch (InterruptedException ie) {
                }
            }
            for (int i = 0; i < 10; i++) {
                chessGrid.setNewPieceAlpha(chessGrid.getNewPieceAlpha() + 10);
                chessGrid.repaint();
                try {
                    Thread.sleep(20);
                } catch (InterruptedException ie) {
                }
            }
        }
    }
}
