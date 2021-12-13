package server;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.UnknownHostException;
import java.util.Vector;

public class ServerFrame extends JFrame {
    Server father;
    JLabel portLabel = new JLabel("Port");
    JLabel ipLabel;
    JTextField portTextField = new JTextField("9090");
    JButton startButton = new JButton("Start");
    JButton stopButton = new JButton("Close");
    JList usersOnlineList = new JList();
    JPanel serverPanel = new JPanel();
    JScrollPane usersScrollPane = new JScrollPane(usersOnlineList);
    JSplitPane divider = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, usersOnlineList, serverPanel);

    class Listener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            if(e.getSource() == startButton){
                startEvent();
            }else if(e.getSource() == stopButton){
                stopEvent();
            }
        }
    }

    public void startEvent() {

        int port = 0;
        try {

            port = Integer.parseInt(this.portTextField.getText().trim());
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Integer plz!", "Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (port > 65535 || port < 0) {
            JOptionPane.showMessageDialog(this, "Integer in range of [0,65535]!", "Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {

            this.startButton.setEnabled(false);
            this.portTextField.setEnabled(false);
            this.stopButton.setEnabled(true);

            this.father.serverSocket = new ServerSocket(port);
            this.father.serverThread = new ServerThread(this.father);
            this.father.serverThread.start();

            JOptionPane.showMessageDialog(this, "Successful", "Info",
                    JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Fail", "Error",
                    JOptionPane.ERROR_MESSAGE);
            this.startButton.setEnabled(true);
            this.portTextField.setEnabled(true);
            this.stopButton.setEnabled(false);
        }
    }

    public void stopEvent() {

        try {

            Vector vec = this.father.onlineList;
            int size = vec.size();
            for (int i = 0; i < size; i++) {

                ServerAgentThread tempSat = (ServerAgentThread) vec.get(i);
                tempSat.dataOutputStream.writeUTF("<!SERVER_DOWN!>");
                tempSat.flag = false;
            }
            this.father.serverThread.flag = false;//关闭服务器线程
            this.father.serverThread = null;
            this.father.serverSocket.close();//关闭ServerSocket

            vec.clear();//将在线用户列表清空
            refreshList();
            this.startButton.setEnabled(true);
            this.portTextField.setEnabled(true);
            this.stopButton.setEnabled(false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void refreshList() {

        Vector vec = new Vector();
        int size = this.father.onlineList.size();
        for (int i = 0; i < size; i++) {
            ServerAgentThread tempSat = (ServerAgentThread) this.father.onlineList.get(i);
            String strName = tempSat.name;
            vec.add(strName);
        }
        this.usersOnlineList.setListData(vec);
    }





    public ServerFrame(Server server) {
        this.father = server;
        this.serverPanel.setLayout(null);
        this.portLabel.setBounds(20, 20, 100, 40);
        this.serverPanel.add(this.portLabel);

        this.portTextField.setBounds(150, 20, 120, 40);
        this.serverPanel.add(this.portTextField);

        this.startButton.setBounds(18, 80, 120, 40);
        this.serverPanel.add(this.startButton);

        this.stopButton.setBounds(150, 80, 120, 40);
        this.serverPanel.add(this.stopButton);

        this.stopButton.setEnabled(false);

        this.startButton.addActionListener(new Listener());
        this.stopButton.addActionListener(new Listener());
        try {
            InetAddress host = InetAddress.getLocalHost();
            String hostAddress = host.getHostAddress();
            this.ipLabel = new JLabel("This ip:" + hostAddress);
            this.ipLabel.setFont(new java.awt.Font(Font.DIALOG, 1, 50));
            this.ipLabel.setBounds(20,130,600,50);
            this.serverPanel.add(this.ipLabel);
        } catch (UnknownHostException ex) {
            ex.printStackTrace();
        }
        this.usersOnlineList.setFont(new java.awt.Font(Font.DIALOG, 0, 30));
        this.setBounds(20, 20, 800, 600);
        this.add(divider);
        this.divider.setDividerLocation(250);
        this.divider.setDividerSize(4);

        this.serverPanel.setVisible(true);
        this.setVisible(true);

    }





//        public void initialFrame() {
//
//            this.setTitle("");
//            this.addWindowListener(
//                    new WindowAdapter() {
//                        public void windowClosing(WindowEvent e) {
//                            if (st == null)//当服务器线程为空时直接退出
//                            {
//                                System.exit(0);//退出
//                                return;
//                            }
//                            try {
//                                Vector v = onlineList;
//                                int size = v.size();
//                                for (int i = 0; i < size; i++) {
//                                    //当不为空时，向在线用户发送离线信息
//                                    ServerAgentThread tempSat = (ServerAgentThread) v.get(i);
//
//                                    tempSat.dout.writeUTF("<#SERVER_DOWN#>");
//                                    tempSat.flag = false;//终止服务器代理线程
//                                }
//
//                                st.flag = false;//终止服务器线程
//                                st = null;
//                                ss.close();
//                                v.clear();
//                                refreshList();
//
//                            } catch (Exception ee) {
//                                ee.printStackTrace();
//                            }
//                            System.exit(0);
//                        }
//                    }
//            );
//        }


    }




