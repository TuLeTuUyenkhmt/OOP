import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;

public class ClientTCP {
    public static void main(String[] args) throws IOException {
        String serverAddress = "localhost"; // Địa chỉ IP của server
        int port = 1234; // Cổng mạng của server
        File fileToSend = new File("file_to_send.txt"); // Tập tin cần truyền đi
        FileInputStream fileInputStream = new FileInputStream(fileToSend); // Tạo file input stream để đọc tập tin

        Socket socket = new Socket(serverAddress, port); // Tạo socket client và kết nối đến server
        OutputStream outputStream = socket.getOutputStream(); // Lấy output stream từ socket

        byte[] buffer = new byte[1024];
        int length;
        while ((length = fileInputStream.read(buffer)) > 0) { // Đọc dữ liệu từ file input stream và ghi vào output stream
            outputStream.write(buffer, 0, length);
        }
        outputStream.close(); // Đóng output stream
        System.out.println("File sent.");
    }
}