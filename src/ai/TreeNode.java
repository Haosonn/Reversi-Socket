package ai;

import view.GameFrame;

import java.util.ArrayList;
import java.util.Arrays;

public class TreeNode {
    private int age;
    private int color;
    private int[] step = new int[2];
    private TreeNode protreenode;
    private ArrayList<TreeNode> treeNodes = new ArrayList<>();
    private int[][] nodeBoard = new int[8][8];
    private int value;
    private int[] values;
    public int[][] valueBoard = {{90, -60, 10, 10, 10, 10, -60, 90},
            {-60, -80, 5, 5, 5, 5, -80, -60},
            {10, 5, 1, 1, 1, 1, 5, 10},
            {10, 5, 1, 1, 1, 1, 5, 10},
            {10, 5, 1, 1, 1, 1, 5, 10},
            {10, 5, 1, 1, 1, 1, 5, 10},
            {-60, -80, 5, 5, 5, 5, -80, -60},
            {90, -60, 10, 10, 10, 10, -60, 90}};

    public int[][] getNodeBoard() {
        return nodeBoard;
    }

    public int getAge() {
        return age;
    }

    public void setTreeNodes(TreeNode treeNode) {
        this.treeNodes.add(treeNode);
    }

    public void setValue(int value) {
        this.value = value;
    }

    public int[] getStep() {
        return step;
    }

    public TreeNode getProtreenode() {
        return protreenode;
    }

    public ArrayList<TreeNode> getTreeNodes() {
        return treeNodes;
    }

    public int getValue() {
        return value;
    }

    public TreeNode(int age, int[] step, TreeNode protreenode) {
        this.age = age;
        this.color = (age % 2 == 0) ? 1 : -1;
        this.step = step;
        this.protreenode = protreenode;
        for (int i = 0; i < 8; i++) {
            this.nodeBoard[i] = protreenode.getNodeBoard()[i].clone();
        }
        this.nodeBoard = reverse(this.color, this.step[0], this.step[1], this.nodeBoard);
        protreenode.setTreeNodes(this);
    }

    public TreeNode(int age, int[][] nodeBoard) {
        this.age = age;
        this.color = 1;
        for (int i = 0; i < 8; i++) {
            this.nodeBoard[i] = nodeBoard[i].clone();
        }
    }

    public void addTreeNodes() {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (GameFrame.controller.getGamePanel().getChessGrids(i, j).getReminder()) {
                    this.treeNodes.add(new TreeNode(this.age + 1, new int[]{i, j}, this));
                }
            }
        }
    }

    public void addValue() {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (this.nodeBoard[i][j] == color) {
                    this.value += valueBoard[i][j];
                }
            }
        }
    }

    public void addMaxValue() {
        values = new int[treeNodes.size()];
        for (int i = 0; i < values.length; i++) {
            values[i] = treeNodes.get(i).getValue();
        }
        Arrays.sort(values);
        this.value = values[values.length - 1];
    }

    public void addMiniValue() {
        values = new int[treeNodes.size()];
        for (int i = 0; i < values.length; i++) {
            values[i] = treeNodes.get(i).getValue();
        }
        Arrays.sort(values);
        this.value = values[0];
    }

    public static int[][] reverse(int color, int x, int y, int[][] chessboard) {
        int[][] nextchessboard = new int[8][8];
        int result;
        for (int i = 0; i < 8; i++) {
            nextchessboard[i] = chessboard[i].clone();
        }
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                result = 1;
                if (chessboard[i][j] == color && (Math.abs(x - i) > 1 || Math.abs(y - j) > 1)) {
                    if (Math.abs(i - x) == Math.abs(j - y)) {
                        for (int k = 1; k < Math.abs(i - x); k++) {
                            if (chessboard[x + Math.abs(i - x) / (i - x) * k][y + Math.abs(j - y) / (j - y) * k] != -color) {
                                result = 0;
                            }
                        }
                        if (result == 1) {
                            for (int k = 1; k < Math.abs(i - x); k++) {
                                nextchessboard[x + Math.abs(i - x) / (i - x) * k][y + Math.abs(j - y) / (j - y) * k] = color;
                            }
                        }
                    }
                    if (i == x) {
                        for (int k = 1; k < Math.abs(j - y); k++) {
                            if (chessboard[x][y + Math.abs(j - y) / (j - y) * k] != -color) {
                                result = 0;
                            }
                        }
                        if (result == 1) {
                            for (int k = 1; k < Math.abs(j - y); k++) {
                                nextchessboard[x][y + Math.abs(j - y) / (j - y) * k] = color;
                            }
                        }
                    }
                    if (j == y) {
                        for (int k = 1; k < Math.abs(i - x); k++) {
                            if (chessboard[x + Math.abs(i - x) / (i - x) * k][y] != -color) {
                                result = 0;
                            }
                        }
                        if (result == 1) {
                            for (int k = 1; k < Math.abs(i - x); k++) {
                                nextchessboard[x + Math.abs(i - x) / (i - x) * k][y] = color;
                            }
                        }
                    }
                }
            }
        }
        nextchessboard[x][y] = color;
        return nextchessboard;
    }

//    public static boolean canClick(int color, int x, int y, int[][] chessboard) {
//        boolean result;
//        if (chessboard[x][y] != 0) {
//            return false;
//        }
//        for (int i = 0; i < 8; i++) {
//            for (int j = 0; j < 8; j++) {
//                result = true;
//                if (chessboard[i][j] == color && (Math.abs(x - i) > 1 || Math.abs(y - j) > 1)) {
//                    if (Math.abs(i - x) == Math.abs(j - y)) {
//                        for (int k = 1; k < Math.abs(i - x); k++) {
//                            if (chessboard[x + Math.abs(i - x) / (i - x) * k][y + Math.abs(j - y) / (j - y) * k] != -color) {
//                                result = false;
//                            }
//                        }
//                        if (result == true) {
//                            return result;
//                        }
//                    }
//                    if (i == x) {
//                        for (int k = 1; k < Math.abs(j - y); k++) {
//                            if (chessboard[x][y + Math.abs(j - y) / (j - y) * k] != -color) {
//                                result = false;
//                            }
//                        }
//                        if (result == true) {
//                            return result;
//                        }
//                    }
//                    if (j == y) {
//                        for (int k = 1; k < Math.abs(i - x); k++) {
//                            if (chessboard[x + Math.abs(i - x) / (i - x) * k][y] != -color) {
//                                result = false;
//                            }
//                        }
//                        if (result == true) {
//                            return result;
//                        }
//                    }
//                }
//            }
//        }
//        return false;
//    }
}
