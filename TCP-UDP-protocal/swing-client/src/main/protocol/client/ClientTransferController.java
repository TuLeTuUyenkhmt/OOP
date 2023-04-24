package main.protocol.client;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JOptionPane;

public class ClientTransferController implements ActionListener {
    private ClientTransferView view;
     public ClientTransferController(ClientTransferView view) {
        this.view = view;
        view.getBtnBrowse().addActionListener(this);
        view.getBtnSendFileTcp().addActionListener(this);
        view.getBtnSendFileUpd().addActionListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals(view.getBtnBrowse().getText())) {
            view.chooseFile();
        }
        if (e.getActionCommand().equals(view.getBtnSendFileTcp().getText())) {
            String host = "localhost";
            int port = 8080;
            String sourceFilePath = view.getTextFieldFilePath().getText();
            File f = new File(sourceFilePath);
            if ( f.exists() && !f.isDirectory()) {
                String destinationDir = "G:\\servertcp\\"; // định nghĩa thư mục đích trên server
                TCPClient tcpClient = new TCPClient(host, port, view.getTextAreaResult());
                tcpClient.connectServer();
                tcpClient.sendFile(sourceFilePath, destinationDir);
                tcpClient.closeSocket();
            } else {
                JOptionPane.showMessageDialog(view, "FilePath not exists.");
            }
        }

        if (e.getActionCommand().equals(view.getBtnSendFileUpd().getText())) {
            String host = "localhost";
            int port = 8081;
            String sourceFilePath = view.getTextFieldFilePath().getText();
            File f = new File(sourceFilePath);
            if (f.exists() && !f.isDirectory()) {
                String destinationDir = "G:\\serverupd\\"; // định nghĩa thư mục đích trên server
                UDPClient udpClient = new UDPClient(host, port, view.getTextAreaResult());
                udpClient.connectServer();
                udpClient.sendFile(sourceFilePath, destinationDir);
            } else {
                JOptionPane.showMessageDialog(view, "FilePath not exists.");
            }
        }

    }
}
