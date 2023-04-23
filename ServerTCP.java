import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerTCP{
    public static void main(String[] args) throws IOException {
        int port = 1234; // Cổng mạng của server
        ServerSocket serverSocket = new ServerSocket(port); // Tạo socket server
        System.out.println("Server is running...");

        while (true) {
            Socket socket = serverSocket.accept(); // Chấp nhận yêu cầu kết nối từ client
            System.out.println("Client connected: " + socket.getInetAddress().getHostName());

            InputStream inputStream = socket.getInputStream(); // Lấy input stream từ socket
            FileOutputStream fileOutputStream = new FileOutputStream("file_received.txt"); // Tạo file để lưu dữ liệu nhận được
            byte[] buffer = new byte[1024];
            int length;
            while ((length = inputStream.read(buffer)) > 0) { // Đọc dữ liệu từ input stream và lưu vào file
                fileOutputStream.write(buffer, 0, length);
            }
            fileOutputStream.close(); // Đóng file output stream
            System.out.println("File received.");
        }
    }
}