package components;

import model.ChessPiece;
import view.GameFrame;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public abstract class BasicComponent extends JComponent {
    public BasicComponent() {
        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                super.mousePressed(e);
                if (!aiModeOn() && !onlineModeOn() && GameFrame.controller.canMouseClick) {
                    super.mousePressed(e);
                    onMouseClicked();
                }
            }
        });
    }


    public abstract void onMouseClicked();

    private boolean aiModeOn() {
        if (GameFrame.controller.getCurrentPlayer() == ChessPiece.BLACK && GameFrame.controller.isBlackAIModeOn()) {
            return true;
        }
        if (GameFrame.controller.getCurrentPlayer() == ChessPiece.WHITE && GameFrame.controller.isWhiteAIModeOn()) {
            return true;
        }
        return false;
    }

    private boolean onlineModeOn() {
        if (GameFrame.controller.getCurrentPlayer() == ChessPiece.BLACK && GameFrame.controller.client.color == -1) {
            return true;
        }
        if (GameFrame.controller.getCurrentPlayer() == ChessPiece.WHITE && GameFrame.controller.client.color == 1) {
            return true;
        }
        return false;
    }
}
