package connection;

import java.sql.Connection;

public class TestMyConnection {
    public static void main(String[] args) {
        // Lấy instance của MyConnection
        MyConnection myConnectionInstance = MyConnection.getInstance();
        
        // Kiểm tra kết nối
        Connection connection = myConnectionInstance.getConnection();
        
        // Xác minh kết nối
        if (connection != null) {
            System.out.println("Kết nối tới database thành công!");
        } else {
            System.out.println("Kết nối tới database thất bại.");
        }
    }
}
