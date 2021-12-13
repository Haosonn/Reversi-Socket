package ai;

import model.ChessPiece;
import view.GameFrame;

import java.util.ArrayList;

public class Node {
    private int deep;

    private Node proNode;
    private ArrayList<Node> nodes = new ArrayList<>();
    private int[][] nodeBoard = new int[8][8];
    private int[] step = new int[2];
    private int index;

    private boolean maxNode;
    private boolean lastNode = false;
    private int alpha = -10000;
    private int beta = 10000;
    private int color;

    private static int[][] valueBoard =
            {{500, -25, 10, 5, 5, 10, -25, 500},
                    {-25, -45, 1, 1, 1, 1, -45, -25},
                    {10, 1, 3, 2, 2, 3, 1, 10},
                    {5, 1, 2, 1, 1, 2, 1, 5},
                    {5, 1, 2, 1, 1, 2, 1, 5},
                    {10, 1, 3, 2, 2, 3, 1, 10},
                    {-25, -45, 1, 1, 1, 1, -45, -25},
                    {500, -25, 10, 5, 5, 10, -25, 500}};

    public boolean isMaxNode() {
        return maxNode;
    }

    public int getAlpha() {
        return alpha;
    }

    public int getBeta() {
        return beta;
    }

    public int[][] getNodeBoard() {
        return nodeBoard;
    }

    public int getIndex() {
        return index;
    }

    public int getDeep() {
        return deep;
    }

    public int[] getStep() {
        return step;
    }

    public ArrayList<Node> getNodes() {
        return nodes;
    }

    public Node(int color, int[] step, Node proNode) {
        this.color = color;
        if ((color == 1 && GameFrame.controller.getCurrentPlayer() == ChessPiece.BLACK) ||
                (color == -1 && GameFrame.controller.getCurrentPlayer() == ChessPiece.WHITE)) {
            this.maxNode = false;
        } else {
            this.maxNode = true;
        }
        this.step = step.clone();
        this.proNode = proNode;
        this.index = proNode.getIndex() + 1;
        this.deep = proNode.getDeep();
        for (int i = 0; i < 8; i++) {
            this.nodeBoard[i] = proNode.getNodeBoard()[i].clone();
        }
        this.nodeBoard = reverse(color, step[0], step[1], this.nodeBoard);

        if (this.index < deep) {
            addNodes();
        }
    }

    public Node(int deep, int color, int[][] nodeBoard) {
        this.deep = deep;
        this.index = 0;
        this.color = color;
        this.nodeBoard = nodeBoard;
        this.maxNode = true;

        addNodes();
    }

    public int getValue() {
        if (this.index != 0) {
            this.alpha = this.proNode.getAlpha();
            this.beta = this.proNode.getBeta();
        }
        int value;
//        if (this.lastNode) {
//            value = finalValue();
//            this.alpha = value;
//            this.beta = value;
//            return value;
//        }
        if (this.nodes.size() == 0) {
            value = boradValue();
            this.alpha = value;
            this.beta = value;
            return value;
        }
        if (this.isMaxNode()) {
            for (int i = 0; i < nodes.size(); i++) {
                if (this.alpha >= this.beta) {
                    return beta;
                }
                value = nodes.get(i).getValue();
                if (value > this.alpha) {
                    this.alpha = value;
                }
            }
            return this.alpha;
        } else {
            for (int i = 0; i < nodes.size(); i++) {
                if (this.beta <= alpha) {
                    return alpha;
                }
                value = nodes.get(i).getValue();
                if (value < this.beta) {
                    this.beta = value;
                }
            }
            return this.beta;
        }
    }

    public int getFinalValue() {
        if (this.index != 0) {
            this.alpha = this.proNode.getAlpha();
            this.beta = this.proNode.getBeta();
        }
        int value;
        if (this.nodes.size() == 0) {
            value = finalValue();
            if (value == 0) {
                this.alpha = 0;
                this.beta = 0;
                return 0;
            }
            value /= Math.abs(value);
            this.alpha = value;
            this.beta = value;
            return value;
        }
        if (this.isMaxNode()) {
            for (int i = 0; i < nodes.size(); i++) {
                if (this.alpha >= this.beta) {
                    return beta;
                }
                value = nodes.get(i).getFinalValue();
                if (value > this.alpha) {
                    this.alpha = value;
                }
            }
            return this.alpha;
        } else {
            for (int i = 0; i < nodes.size(); i++) {
                if (this.beta <= alpha) {
                    return alpha;
                }
                value = nodes.get(i).getFinalValue();
                if (value < this.beta) {
                    this.beta = value;
                }
            }
            return this.beta;
        }
    }

    public int boradValue() {
        int value = 0;
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (this.nodeBoard[i][j] == ((GameFrame.controller.getCurrentPlayer() == ChessPiece.BLACK) ? 1 : -1)) {
                    value += valueBoard[i][j];
                }
            }
        }
        return value;
    }

    public int finalValue() {
        int value = 0;
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (this.nodeBoard[i][j] == ((GameFrame.controller.getCurrentPlayer() == ChessPiece.BLACK) ? 1 : -1)) {
                    value++;
                }
            }
        }
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (this.nodeBoard[i][j] == ((GameFrame.controller.getCurrentPlayer() == ChessPiece.BLACK) ? -1 : 1)) {
                    value--;
                }
            }
        }
        return value;
    }

    public void addNodes() {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (canClick(-this.color, i, j, this.nodeBoard)) {
                    this.nodes.add(new Node(-this.color, new int[]{i, j}, this));
                }
            }
        }
        if (this.nodes.size() == 0) {
            for (int i = 0; i < 8; i++) {
                for (int j = 0; j < 8; j++) {
                    if (canClick(this.color, i, j, this.nodeBoard)) {
                        this.nodes.add(new Node(this.color, new int[]{i, j}, this));
                    }
                }
            }
//            if (boradSpace() == 0) {
//                this.lastNode = true;
//            }
        }
    }

//    public int boradSpace() {
//        int space = 64;
//        for (int i = 0; i < 8; i++) {
//            for (int j = 0; j < 8; j++) {
//                if (this.nodeBoard[i][j] != 0) {
//                    space--;
//                }
//            }
//        }
//        return space;
//    }

    public static boolean canClick(int color, int x, int y, int[][] chessboard) {
        boolean result;
        if (chessboard[x][y] != 0) {
            return false;
        }
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                result = true;
                if (chessboard[i][j] == color && (Math.abs(x - i) > 1 || Math.abs(y - j) > 1)) {
                    if (Math.abs(i - x) == Math.abs(j - y)) {
                        for (int k = 1; k < Math.abs(i - x); k++) {
                            if (chessboard[x + Math.abs(i - x) / (i - x) * k][y + Math.abs(j - y) / (j - y) * k] != -color) {
                                result = false;
                            }
                        }
                        if (result == true) {
                            return true;
                        }
                    }
                    if (i == x) {
                        for (int k = 1; k < Math.abs(j - y); k++) {
                            if (chessboard[x][y + Math.abs(j - y) / (j - y) * k] != -color) {
                                result = false;
                            }
                        }
                        if (result == true) {
                            return true;
                        }
                    }
                    if (j == y) {
                        for (int k = 1; k < Math.abs(i - x); k++) {
                            if (chessboard[x + Math.abs(i - x) / (i - x) * k][y] != -color) {
                                result = false;
                            }
                        }
                        if (result == true) {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
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
}
