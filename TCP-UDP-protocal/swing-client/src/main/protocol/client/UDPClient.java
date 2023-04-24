package main.protocol.client;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

import javax.swing.JTextArea;

import main.protocol.common.FileInfoUdp;

public class UDPClient {
    private static final int PIECES_OF_FILE_SIZE = 1024 * 32;
    private DatagramSocket clientSocket;
    private String host;
    private int port;
    private JTextArea textAreaLog;
    public UDPClient(String host, int port, JTextArea textAreaLog) {
        this.host = host;
        this.port = port;
        this.textAreaLog = textAreaLog;
    }
    public void connectServer() {
        try {
            textAreaLog.append("connected to server.\n");
            clientSocket = new DatagramSocket();
        } catch (SocketException e) {
            e.printStackTrace();
        }
    }

    public void sendFile(String sourcePath, String destinationDir) {
        InetAddress inetAddress;
        DatagramPacket sendPacket;

        try {
            File fileSend = new File(sourcePath);
            InputStream inputStream = new FileInputStream(fileSend);
            BufferedInputStream bis = new BufferedInputStream(inputStream);
            inetAddress = InetAddress.getByName(host);
            byte[] bytePart = new byte[PIECES_OF_FILE_SIZE];
            
            // get file size
            long fileLength = fileSend.length();
            int piecesOfFile = (int) (fileLength / PIECES_OF_FILE_SIZE);
            int lastByteLength = (int) (fileLength % PIECES_OF_FILE_SIZE);

            // check last bytes of file
            if (lastByteLength > 0) {
                piecesOfFile++;
            }

            // split file into pieces and assign to fileBytess
            byte[][] fileBytess = new byte[piecesOfFile][PIECES_OF_FILE_SIZE];
            int count = 0;
            while (bis.read(bytePart, 0, PIECES_OF_FILE_SIZE) > 0) {
                fileBytess[count++] = bytePart;
                bytePart = new byte[PIECES_OF_FILE_SIZE];
            }

            // read file info
            FileInfoUdp fileInfo = new FileInfoUdp();
            fileInfo.setFilename(fileSend.getName());
            fileInfo.setFileSize(fileSend.length());
            fileInfo.setPiecesOfFile(piecesOfFile);
            fileInfo.setLastByteLength(lastByteLength);
            fileInfo.setDestinationDirectory(destinationDir);

            // send file info
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(baos);
            oos.writeObject(fileInfo);
            sendPacket = new DatagramPacket(baos.toByteArray(), baos.toByteArray().length,
                    inetAddress, port);
            clientSocket.send(sendPacket);

            // send file content
            textAreaLog.append("Sending file...\r\n" );
            // send pieces of file
            for (int i = 0; i < (count - 1); i++) {
                sendPacket = new DatagramPacket(fileBytess[i], PIECES_OF_FILE_SIZE,
                        inetAddress, port);
                clientSocket.send(sendPacket);
                waitMillisecond(40);
            }
            // send last bytes of file
            sendPacket = new DatagramPacket(fileBytess[count - 1], PIECES_OF_FILE_SIZE,
                    inetAddress, port);
            clientSocket.send(sendPacket);
            waitMillisecond(40);

            // close stream
            bis.close();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        textAreaLog.append("Sented.\r\n");
    }

    public void waitMillisecond(long millisecond) {
        try {
            Thread.sleep(millisecond);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
