package connection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MyConnection1 {
	private static MyConnection1 instance;
	private static Connection connection;
	private MyConnection1() {
//     String url = "jdbc:sqlserver://VANBAOVR:1433;DatabaseName=QuanLyCuaHangQuanAo;trustServerCertificate=true;integratedSecurity=true;";
		String url = "jdbc:mysql://localhost:3306/store_management"; // URL kết nối chính xác
        String user = "root"; // Tên đăng nhập
        String password = "123456"; // Mật khẩu

       try {       	
           connection = DriverManager.getConnection(url,user, password);
           System.out.println("Kết nối database thành công");
       } catch (SQLException e1) {
       	System.out.println("Kết nối database không thành công");
           e1.printStackTrace();
       }
   }
	public synchronized static MyConnection1 getInstance() {
        if(instance == null)
            instance = new MyConnection1();
        return instance;
    }
	public Connection getConnection() {
        return connection;
    }


}
