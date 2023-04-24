package main.protocol.client;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class ClientTransferView extends JFrame {
    private static final long serialVersionUID = 1L;

    private JButton btnBrowse;
    private JTextField textFieldFilePath;
    private JButton btnSendFileTcp;
    private JButton btnSendFileUdp;
    private JTextArea textAreaResult;

    public ClientTransferView() {
        setTitle("Transfer using protocol TCP/IP and UDP");
        textFieldFilePath = new JTextField();
        textFieldFilePath.setBounds(20, 50, 450, 25);
        textFieldFilePath.enableInputMethods(false);
        btnBrowse = new JButton("Browse");
        btnBrowse.setBounds(470, 50, 80, 25);
        btnSendFileTcp = new JButton("Send File TCP");
        btnSendFileUdp = new JButton("Send File UDP");
        btnSendFileTcp.setBounds(20, 80, 160, 25);
        btnSendFileUdp.setBounds(200, 80, 160, 25);
        textAreaResult = new JTextArea();
        textAreaResult.setBounds(20, 110, 490, 150);

        add(textFieldFilePath);
        add(btnBrowse);
        add(btnSendFileTcp);
        add(btnSendFileUdp);
        add(textAreaResult);

        setLayout(null);
        setSize(600, 350);
        setVisible(true);
        // thoát chương trình khi tắt window
        setDefaultCloseOperation(EXIT_ON_CLOSE);
    }

    public void chooseFile() {
        final JFileChooser fc = new JFileChooser();
        fc.showOpenDialog(this);
        try {
            if (fc.getSelectedFile() != null) {
                textFieldFilePath.setText(fc.getSelectedFile().getPath());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public JButton getBtnBrowse() {
        return btnBrowse;
    }

    public void setBtnBrowse(JButton btnBrowse) {
        this.btnBrowse = btnBrowse;
    }

    public JTextField getTextFieldFilePath() {
        return textFieldFilePath;
    }

    public void setTextFieldFilePath(JTextField textFieldFilePath) {
        this.textFieldFilePath = textFieldFilePath;
    }

    public JButton getBtnSendFileTcp() {
        return btnSendFileTcp;
    }

    public void setBtnSendFileTcp(JButton btnSendFileTcp) {
        this.btnSendFileTcp = btnSendFileTcp;
    }
    public JButton getBtnSendFileUpd() {
        return btnSendFileUdp;
    }

    public void setBtnSendFileUpd(JButton btnSendFileUdp) {
        this.btnSendFileUdp = btnSendFileUdp;
    }

    public JTextArea getTextAreaResult() {
        return textAreaResult;
    }

    public void setTextAreaResult(JTextArea textAreaResult) {
        this.textAreaResult = textAreaResult;
    }
}
