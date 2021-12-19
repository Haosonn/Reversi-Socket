package view;

import ai.ThreadForAI;
import components.ChessGridComponent;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class MenuBar extends JMenuBar {

    public MenuBar(GameFrame mainframe) {
        add(createGameMenu(mainframe));
        add(createEditMenu(mainframe));
        add(createThemeMenu());
        add(createAIMenu());
        setVisible(true);
    }

    private JMenu createGameMenu(GameFrame mainframe) {
        JMenu menu = new JMenu("Game");
        JMenuItem item = new JMenuItem("restart");
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
            if (filePath.length() == 0) return;
            GameFrame.controller.readFileData(filePath);
        });
        menu.add(item);

        item = new JMenuItem("save");
        item.addActionListener(e -> {
            System.out.println("click save Btn");
            String filePath = JOptionPane.showInputDialog(this, "input the path here");
            if (filePath.length() == 0) return;
            GameFrame.controller.writeDataToFile(filePath);
        });
        menu.add(item);
        menu.addSeparator();

        JCheckBoxMenuItem cbMenuItem = new JCheckBoxMenuItem("cheating mode");

        cbMenuItem.addActionListener(e -> {
            GameFrame.controller.setCheatingBtnOn(!GameFrame.controller.isCheatingBtnOn());
        });


        menu.add(cbMenuItem);

        return menu;
    }


    private JMenu createEditMenu(GameFrame mainframe) {
        JMenu menu = new JMenu("Edit");
        JMenuItem item = new JMenuItem("undo");
        item.addActionListener(e -> {
            System.out.println("click undo Btn");
            if (mainframe.controller.client.onlineMode) mainframe.controller.undoRequest();
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
            Image image = new ImageIcon("resources/Board1.png").getImage();
            GameFrame.controller.getGamePanel().setImage(image);
            GameFrame.controller.getGamePanel().repaint();
        });

        JRadioButton rb2 = new JRadioButton("board 2");
        rb2.addActionListener(e -> {
            System.out.println("choose board 2");
            ChessGridComponent.isCustom = false;
            Image image = new ImageIcon("resources/Board2.png").getImage();
            GameFrame.controller.getGamePanel().setImage(image);
            GameFrame.controller.getGamePanel().repaint();
        });

        JRadioButton rb3 = new JRadioButton("board 3");
        rb3.addActionListener(e -> {
            System.out.println("choose board 3");
            ChessGridComponent.isCustom = false;
            Image image = new ImageIcon("resources/Board3.png").getImage();
            GameFrame.controller.getGamePanel().setImage(image);
            GameFrame.controller.getGamePanel().repaint();
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

    private JMenu createAIMenu() {
        JMenu menu = new JMenu("AI");

        JMenu aiMode = new JMenu("AI mode");

        JCheckBoxMenuItem blackAI = new JCheckBoxMenuItem("black");
        blackAI.addActionListener(e -> {
            GameFrame.controller.setBlackAIModeOn(!GameFrame.controller.isBlackAIModeOn());
            if (GameFrame.controller.isBlackAIModeOn()) {
                GameFrame.controller.setThreadForBlackAI(new ThreadForAI(1, GameFrame.controller.getBlackDeep()));
                GameFrame.controller.getThreadForBlackAI().start();
                System.out.println("Black AI On");
            } else {

                System.out.println("Black AI Off");
            }
        });

        JCheckBoxMenuItem whiteAI = new JCheckBoxMenuItem("white");
        whiteAI.addActionListener(e -> {
            GameFrame.controller.setWhiteAIModeOn(!GameFrame.controller.isWhiteAIModeOn());
            if (GameFrame.controller.isWhiteAIModeOn()) {
                GameFrame.controller.setThreadForWhiteAI(new ThreadForAI(-1, GameFrame.controller.getWhiteDeep()));
                GameFrame.controller.getThreadForWhiteAI().start();
                System.out.println("White AI On");
            } else {
                System.out.println("White AI Off");
            }
        });

        aiMode.add(blackAI);
        aiMode.add(whiteAI);
        menu.add(aiMode);

        JMenu aiLevel = new JMenu("AI level");
        JMenu blackLevel = new JMenu("black level");
        JMenu whiteLevel = new JMenu("white level");
        JSlider blackSlider = new JSlider(1, 4);
        JSlider whiteSlider = new JSlider(1, 4);
        blackSlider.setMajorTickSpacing(1);
        whiteSlider.setMajorTickSpacing(1);
        blackSlider.setPaintTicks(true);
        whiteSlider.setPaintTicks(true);
        blackSlider.setPaintLabels(true);
        whiteSlider.setPaintLabels(true);
        blackSlider.addChangeListener(e -> {
            if (GameFrame.controller.getThreadForBlackAI() != null) {
                GameFrame.controller.getThreadForBlackAI().setDeep((blackSlider.getValue() == 1) ? 1 : blackSlider.getValue() + 2);
            }
            GameFrame.controller.setBlackDeep((blackSlider.getValue() == 1) ? 1 : blackSlider.getValue() + 2);
        });
        whiteSlider.addChangeListener(e -> {
            if (GameFrame.controller.getThreadForWhiteAI() != null) {
                GameFrame.controller.getThreadForWhiteAI().setDeep((whiteSlider.getValue() == 1) ? 1 : whiteSlider.getValue() + 2);
            }
            GameFrame.controller.setWhiteDeep((whiteSlider.getValue() == 1) ? 1 : whiteSlider.getValue() + 2);
        });

        blackLevel.add(blackSlider);
        whiteLevel.add(whiteSlider);
        aiLevel.add(blackLevel);
        aiLevel.add(whiteLevel);

        menu.add(aiLevel);

        return menu;
    }

}
