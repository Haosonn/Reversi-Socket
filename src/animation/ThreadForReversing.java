package animation;

import components.ChessGridComponent;
import model.ChessPiece;
import view.GameFrame;

import java.awt.*;

public class ThreadForReversing extends Thread {
    private ChessGridComponent chessGrid;
    private boolean end = false;

    public ThreadForReversing(ChessGridComponent chessGrid) {
        this.chessGrid = chessGrid;
    }

    public void setEnd(boolean end) {
        this.end = end;
    }

    @Override
    public void run() {
        chessGrid.setReversingSize(ChessGridComponent.chessSize);
        chessGrid.setReversingColor((chessGrid.getChessPiece() == ChessPiece.BLACK) ? -1 : 1);
        for (int i = 0; i < 10; i++) {
            if (end) {
                System.out.println("end");
                return;
            }
            chessGrid.setReversingSize(chessGrid.getReversingSize() - ChessGridComponent.chessSize / 10);
            chessGrid.repaint();
            try {
                Thread.sleep(20);
            } catch (InterruptedException ie) {
            }
        }
        chessGrid.setReversingColor(-chessGrid.getReversingColor());
        for (int i = 0; i < 10; i++) {
            if (end) {
                return;
            }
            chessGrid.setReversingSize(chessGrid.getReversingSize() + ChessGridComponent.chessSize / 10);
            chessGrid.repaint();
            try {
                Thread.sleep(20);
            } catch (InterruptedException ie) {
            }
        }
    }
}
