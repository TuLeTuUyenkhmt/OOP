import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class ClientUDP {
    public static void main(String[] args) throws IOException {
        String serverAddress = "localhost"; // Địa chỉ IP của server
        int port = 1234; // Cổng mạng của server
        File fileToSend = new File("file_to_send.txt"); // Tập tin cần truyền đi
        FileInputStream fileInputStream = new FileInputStream(fileToSend); // Tạo file input stream để đọc tập tin

        DatagramSocket socket = new DatagramSocket(); // Tạo socket client
        InetAddress serverInetAddress = InetAddress.getByName(serverAddress); // Chuyển đổi địa chỉ IP của server thành đối tượng InetAddress
        byte[] buffer = new byte[1024];
        DatagramPacket packet = new DatagramPacket(buffer, buffer.length, serverInetAddress, port); // Tạo gói tin UDP

        int length;
        while ((length = fileInputStream.read(buffer)) > 0) { // Đọc dữ liệu từ file input stream và gửi đi qua gói tin UDP
            packet.setData(buffer, 0, length);
            socket.send(packet);
        }
        fileInputStream.close(); // Đóng file input stream
        socket.close(); // Đóng socket
        System.out.println("File sent.");
    }
}
