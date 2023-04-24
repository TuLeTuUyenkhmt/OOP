package main.protocol.client;

import java.io.BufferedInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import javax.swing.JTextArea;

import main.protocol.common.FileInfoTcp;

public class TCPClient {
    // create Socket object
    private Socket client;
    private String host;
    private int port;
    private JTextArea textAreaLog;

    public TCPClient(String host, int port, JTextArea textAreaLog) {
        this.host = host;
        this.port = port;
        this.textAreaLog = textAreaLog;
    }
    
    public void connectServer() {
        try {
            client = new Socket(host, port);
            textAreaLog.append("connected to server.\n");
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendFile(String sourceFilePath, String destinationDir) {
        DataOutputStream outToServer = null;
        ObjectOutputStream oos = null;
        ObjectInputStream ois = null;

        try {
            // make greeting
            outToServer = new DataOutputStream(client.getOutputStream());
            outToServer.writeUTF("Hello from " + client.getLocalSocketAddress());

            // get file info
            FileInfoTcp fileInfo = getFileInfo(sourceFilePath, destinationDir);

            // send file
            oos = new ObjectOutputStream(client.getOutputStream());
            oos.writeObject(fileInfo);

            // get confirmation
            ois = new ObjectInputStream(client.getInputStream());
            fileInfo = (FileInfoTcp) ois.readObject();
            if (fileInfo != null) {
                textAreaLog.append("send file to server " + fileInfo.getStatus() + "\n");
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            // close all stream
            closeStream(oos);
            closeStream(ois);
            closeStream(outToServer);
        }
    }

    private FileInfoTcp getFileInfo(String sourceFilePath, String destinationDir) {
        FileInfoTcp fileInfo = null;
        BufferedInputStream bis = null;
        try {
            File sourceFile = new File(sourceFilePath);
            bis = new BufferedInputStream(new FileInputStream(sourceFile));
            fileInfo = new FileInfoTcp();
            byte[] fileBytes = new byte[(int) sourceFile.length()];
            // get file info
            bis.read(fileBytes, 0, fileBytes.length);
            fileInfo.setFilename(sourceFile.getName());
            fileInfo.setDataBytes(fileBytes);
            fileInfo.setDestinationDirectory(destinationDir);
        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            closeStream(bis);
        }
        return fileInfo;
    }

    public void closeSocket() {
        try {
            if (client != null) {
                client.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void closeStream(InputStream inputStream) {
        try {
            if (inputStream != null) {
                inputStream.close();
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public void closeStream(OutputStream outputStream) {
        try {
            if (outputStream != null) {
                outputStream.close();
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
