package controller;

import view.GameFrame;

import java.util.List;

public class ThreadForLoading extends Thread {
    private List<String> fileData;

    ThreadForLoading(List<String> fileData) {
        this.fileData = fileData;
    }

    @Override
    public void run() {
        GameFrame.controller.canMouseClick = false;
        try {
            Thread.sleep(200);
        } catch (Exception e) {
            e.printStackTrace();
        }
        for (int i = 0; i < fileData.size(); i++) {
            try {
                Thread.sleep(300);
            } catch (Exception e) {
                e.printStackTrace();
            }
            int[] step = {Integer.parseInt(fileData.get(i).split(" ")[67]), Integer.parseInt(fileData.get(i).split(" ")[68])};
            //todo add a thread to review
            GameFrame.controller.getGamePanel().getChessGrids(step[0], step[1]).onMouseClicked();

//                gameRecord.getStep().add(arrayLine);

        }
        fileData.forEach(System.out::println);
        GameFrame.controller.canMouseClick = true;
    }
}
