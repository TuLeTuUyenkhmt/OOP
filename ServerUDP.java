import java.io.FileOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class ServerUDP {
    public static void main(String[] args) throws IOException {
        int port = 1234; // Cổng mạng của server
        DatagramSocket socket = new DatagramSocket(port); // Tạo socket server
        System.out.println("Server is running...");

        byte[] buffer = new byte[1024];
        DatagramPacket packet = new DatagramPacket(buffer, buffer.length);

        FileOutputStream fileOutputStream = new FileOutputStream("file_received.txt"); // Tạo file để lưu dữ liệu nhận được
        while (true) {
            socket.receive(packet); // Nhận gói tin từ client
            System.out.println("Packet received from client: " + packet.getAddress().getHostName());

            byte[] data = packet.getData();
            fileOutputStream.write(data, 0, packet.getLength()); // Ghi dữ liệu từ gói tin vào file
            if (packet.getLength() < 1024) { // Nếu kích thước gói tin nhận được nhỏ hơn 1024 byte thì kết thúc quá trình nhận dữ liệu
                fileOutputStream.close(); // Đóng file output stream
                System.out.println("File received.");
                break;
            }
        }
        socket.close(); // Đóng socket
    }
}