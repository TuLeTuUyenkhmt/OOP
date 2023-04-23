import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.*;
import java.util.Arrays;

public class UDP_TCP_File_Transfer extends JFrame {
    private JPanel panel;
    private JLabel hostLabel, portLabel, fileLabel;
    private JTextField hostField, portField, fileField;
    private JButton fileButton, udpButton, tcpButton;
    private JFileChooser fileChooser;
    private JProgressBar progressBar;
    private Socket tcpSocket;
    private DatagramSocket udpSocket;
    private InetAddress hostAddress;
    private int port;
    private File file;
    private byte[] buffer;
    private int bufferSize = 1024;
    private int fileSize;

    public UDP_TCP_File_Transfer() {
        super("File Transfer");

        // Create the UI components
        panel = new JPanel(new GridLayout(4, 2));
        hostLabel = new JLabel("Host:");
        portLabel = new JLabel("Port:");
        fileLabel = new JLabel("File:");
        hostField = new JTextField("localhost");
        portField = new JTextField("8080");
        fileField = new JTextField();
        fileButton = new JButton("Choose File");
        udpButton = new JButton("Send UDP");
        tcpButton = new JButton("Send TCP");
        progressBar = new JProgressBar(0, 100);

        // Create the file chooser
        fileChooser = new JFileChooser();
        fileChooser.setCurrentDirectory(new File(System.getProperty("user.dir")));

        // Add the UI components to the panel
        panel.add(hostLabel);
        panel.add(hostField);
        panel.add(portLabel);
        panel.add(portField);
        panel.add(fileLabel);
        panel.add(fileField);
        panel.add(fileButton);
        panel.add(new JLabel()); // Spacer
        panel.add(udpButton);
        panel.add(tcpButton);

        // Add the panel and progress bar to the frame
        add(panel, BorderLayout.NORTH);
        add(progressBar, BorderLayout.SOUTH);

        // Set up the file chooser button
        fileButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int result = fileChooser.showOpenDialog(UDP_TCP_File_Transfer.this);
                if (result == JFileChooser.APPROVE_OPTION) {
                    file = fileChooser.getSelectedFile();
                    fileField.setText(file.getName());
                    fileSize = (int) file.length();
                }
            }
        });

        // Set up the UDP button
        udpButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    // Get the host address and port
                    hostAddress = InetAddress.getByName(hostField.getText());
                    port = Integer.parseInt(portField.getText());

                    // Create the UDP socket and buffer
                    udpSocket = new DatagramSocket();
                    buffer = new byte[bufferSize];

                    // Send the file
                    FileInputStream fileInputStream = new FileInputStream(file);
                    DatagramPacket packet;
                    int bytesSent = 0;
                    int bytesRead;
                    while ((bytesRead = fileInputStream.read(buffer)) != -1) {
                        packet = new DatagramPacket(buffer, bytesRead, hostAddress, port);
                        udpSocket.send(packet);
                        bytesSent +=bytesRead;
                                            // Update the progress bar
                    int progress = (int) ((double) bytesSent / fileSize * 100);
                    progressBar.setValue(progress);
                }

                // Close the file input stream and socket
                fileInputStream.close();
                udpSocket.close();

                // Display a message when the transfer is complete
                JOptionPane.showMessageDialog(UDP_TCP_File_Transfer.this, "File transfer complete.");
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(UDP_TCP_File_Transfer.this, "An error occurred while sending the file.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    });

    // Set up the TCP button
    tcpButton.addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent e) {
            try {
                // Get the host address and port
                hostAddress = InetAddress.getByName(hostField.getText());
                port = Integer.parseInt(portField.getText());

                // Create the TCP socket and buffer
                tcpSocket = new Socket(hostAddress, port);
                OutputStream outputStream = tcpSocket.getOutputStream();
                buffer = new byte[bufferSize];

                // Send the file
                FileInputStream fileInputStream = new FileInputStream(file);
                int bytesSent = 0;
                int bytesRead;
                while ((bytesRead = fileInputStream.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, bytesRead);
                    bytesSent += bytesRead;

                    // Update the progress bar
                    int progress = (int) ((double) bytesSent / fileSize * 100);
                    progressBar.setValue(progress);
                }

                // Close the file input stream and socket
                fileInputStream.close();
                outputStream.close();
                tcpSocket.close();

                // Display a message when the transfer is complete
                JOptionPane.showMessageDialog(UDP_TCP_File_Transfer.this, "File transfer complete.");
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(UDP_TCP_File_Transfer.this, "An error occurred while sending the file.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    });

    // Set up the frame
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setSize(400, 200);
    setVisible(true);
}

public static void main(String[] args) {
    new UDP_TCP_File_Transfer();
    }
}

