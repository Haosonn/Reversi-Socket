package animation;

import components.ChessGridComponent;

public class ThreadForNewPiece extends Thread {
    private ChessGridComponent chessGrid;

    public ThreadForNewPiece(ChessGridComponent chessGrid) {
        this.chessGrid = chessGrid;
    }

    @Override
    public void run() {
        while (chessGrid.isNewPiece()) {
            for (int i = 1; i < 10; i++) {
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
        chessGrid.setNewPieceAlpha(100);
    }
}
