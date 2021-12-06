package view;

import components.ChessGridComponent;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class MenuBar extends JMenuBar {
    public MenuBar(GameFrame mainframe) {
        add(createGameMenu(mainframe));
        add(createEditMenu(mainframe));
        add(createThemeMenu());
        setVisible(true);
    }

    private JMenu createGameMenu(GameFrame mainframe) {
        JMenu menu = new JMenu("Game");
        JMenuItem item = new JMenuItem("new");
        item.addActionListener(e -> {
            System.out.println("click restart Btn");
            mainframe.restart();
        });
        menu.add(item);
        menu.addSeparator();

        item = new JMenuItem("load");
        item.addActionListener(e -> {
            System.out.println("click load Btn");
            String filePath = JOptionPane.showInputDialog(this, "input the path here");
            if(filePath.length() == 0) return;
            GameFrame.controller.readFileData(filePath);
        });
        menu.add(item);

        item = new JMenuItem("save");
        item.addActionListener(e -> {
            System.out.println("click save Btn");
            String filePath = JOptionPane.showInputDialog(this, "input the path here");
            if(filePath.length() == 0) return;
            GameFrame.controller.writeDataToFile(filePath);
        });
        menu.add(item);
        menu.addSeparator();

        JCheckBoxMenuItem cbMenuItem = new JCheckBoxMenuItem("cheating mode");
        menu.add(cbMenuItem);

        return menu;
    }


    private JMenu createEditMenu(GameFrame mainframe) {
        JMenu menu = new JMenu("Edit");
        JMenuItem item = new JMenuItem("undo");
        item.addActionListener(e -> {
            System.out.println("click undo Btn");
            if(mainframe.controller.client.onlineMode) mainframe.controller.undoRequest();
            else mainframe.controller.undo();
            repaint();
        });
        menu.add(item);
        return menu;
    }

    private JMenu createThemeMenu() {
        JMenu menu = new JMenu("Theme");
        JMenu board = new JMenu("board");
        ButtonGroup buttonGroup = new ButtonGroup();
        JRadioButton rb1 = new JRadioButton("board 1");
        rb1.setSelected(true);
        rb1.addActionListener(e -> {
            System.out.println("choose board 1");
            ChessGridComponent.isCustom = false;
            Image image = new ImageIcon("Reversi-Socket/resources/Board1.png").getImage();
            GameFrame.controller.getGamePanel().setImage(image);
            GameFrame.controller.getGamePanel().repaint();
        });

        JRadioButton rb2 = new JRadioButton("board 2");
        rb2.addActionListener(e -> {
            System.out.println("choose board 2");
            ChessGridComponent.isCustom = false;
        });

        JRadioButton rb3 = new JRadioButton("board 3");
        rb3.addActionListener(e -> {
            System.out.println("choose board 3");
            ChessGridComponent.isCustom = false;
        });

        JRadioButton rb4 = new JRadioButton("custom board");
        rb4.addActionListener(e -> {
            System.out.println("choose custom board");
            ChessGridComponent.isCustom = true;
            GameFrame.controller.getGamePanel().repaint();
        });
        JMenu customBoard = new JMenu("customization");

        JMenuItem item = new JMenuItem("primary color");
        item.addActionListener(e -> {
            System.out.println("choose primary color");
            ChessGridComponent.primaryColor = JColorChooser.showDialog(customBoard, "Primary Color Chooser", ChessGridComponent.primaryColor);
            GameFrame.controller.getGamePanel().repaint();
        });
        customBoard.add(item);

        item = new JMenuItem(("secondary color"));
        item.addActionListener(e -> {
            System.out.println("choose secondary color");
            ChessGridComponent.secondaryColor = JColorChooser.showDialog(customBoard, "Secondary Color Chooser", ChessGridComponent.secondaryColor);
            GameFrame.controller.getGamePanel().repaint();
        });
        customBoard.add(item);

        buttonGroup.add(rb1);
        buttonGroup.add(rb2);
        buttonGroup.add(rb3);
        buttonGroup.add(rb4);
        board.add(rb1);
        board.add(rb2);
        board.add(rb3);
        board.add(rb4);
        board.add(customBoard);
        menu.add(board);

        return menu;
    }

}
