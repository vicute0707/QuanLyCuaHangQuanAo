package connection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Test {
    private static Test instance; // Singleton instance
    private static Connection connection;

    // Constructor riêng tư
    private Test() {
        String url = "jdbc:mysql://localhost:3306/store_management"; // URL kết nối chính xác
        String user = "root"; // Tên đăng nhập
        String password = "root@"; // Mật khẩu

        try {
            // Kết nối cơ sở dữ liệu
            connection = DriverManager.getConnection(url, user, password);
            System.out.println("Kết nối database thành công");
        } catch (SQLException e) {
            System.out.println("Kết nối database không thành công");
            e.printStackTrace();
        }
    }

    // Lấy instance của lớp Test (đảm bảo thread-safe)
    public synchronized static Test getInstance() {
        if (instance == null) {
            instance = new Test();
        }
        return instance;
    }

    // Lấy đối tượng Connection
    public Connection getConnection() {
        return connection;
    }
    
    public static void main(String[] args) {
        // Lấy instance của lớp Test
        Test databaseConnection = Test.getInstance();

        // Lấy đối tượng Connection
        Connection connection = databaseConnection.getConnection();

        // Kiểm tra kết nối
        if (connection != null) {
            System.out.println("Kết nối tới database thành công!");
        } else {
            System.out.println("Kết nối tới database thất bại!");
        }
    }
}
