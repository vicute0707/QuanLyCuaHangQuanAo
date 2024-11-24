package dao;

import java.sql.*;
import java.util.ArrayList;
import connection.MyConnection;
import entity.PhanLoaiSanPham;

public class Dao_PhanLoaiSanPham {
    private Connection con;
    ArrayList<PhanLoaiSanPham> dsPL;

    public Dao_PhanLoaiSanPham() {
        con = MyConnection.getInstance().getConnection();
        dsPL = new ArrayList<PhanLoaiSanPham>();
    }

    // Phương thức lấy thông tin ProductVariant theo productID
    public ArrayList<PhanLoaiSanPham> getProductVariantsByProductID(String productID) {
        ArrayList<PhanLoaiSanPham> result = new ArrayList<>();
        String storedProc = "{CALL GetProductVariantsByProductID(?)}";
        
        try (CallableStatement stmt = con.prepareCall(storedProc)) {
            stmt.setString(1, productID);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
            	String mapl = rs.getString("variantID");
                String color = rs.getString("color");
                String size = rs.getString("size");
                String material = rs.getString("material");
                PhanLoaiSanPham plsp = new PhanLoaiSanPham(mapl,size, color, material);
                result.add(plsp);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return result;
    }
    
    public int getTotalProductVariants() {
        int totalRows = 0; 

       
        String sql = "SELECT COUNT(*) AS totalRows FROM ProductVariant";

        try (PreparedStatement stmt = con.prepareStatement(sql)) {
            ResultSet rs = stmt.executeQuery();

            // Lấy kết quả từ ResultSet
            if (rs.next()) {
                totalRows = rs.getInt("totalRows"); 
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return totalRows; 
    }
    public boolean insertProductVariant(String variantID, String productID, String color, String size, String material) {
        String storedProc = "{CALL InsertProductVariant(?, ?, ?, ?, ?)}";
        boolean isSuccess = false;

        try (CallableStatement stmt = con.prepareCall(storedProc)) {
            // Thiết lập các tham số cho stored procedure
            stmt.setString(1, variantID);
            stmt.setString(2, productID);
            stmt.setString(3, color);
            stmt.setString(4, size);
            stmt.setString(5, material);

            // Thực thi stored procedure
            int rowsAffected = stmt.executeUpdate();
            
            // Kiểm tra nếu có ít nhất một hàng được thêm
            isSuccess = rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return isSuccess; // Trả về true nếu thêm thành công
    }
    
    public boolean XoaPhanLoaiSanPham(String variantID) {
        String sql = "{CALL DeleteProductVariant(?)}"; 
        try (CallableStatement stmt = con.prepareCall(sql)) {
            stmt.setString(1, variantID);
           int rowsAffected = stmt.executeUpdate();
     
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            e.printStackTrace();
           
        }
        
        return false; 
    }
    public boolean capNhatProductVariant(String variantID, String color, String size, String material) {
        String sql = "{CALL UpdateProductVariant(?, ?, ?, ?)}";  // Gọi stored procedure UpdateProductVariant
        boolean isSuccess = false;  // Biến lưu trạng thái cập nhật thành công hay không

        try (CallableStatement stmt = con.prepareCall(sql)) {
      
            stmt.setString(1, variantID); 
            stmt.setString(2, color);    
            stmt.setString(3, size);       
            stmt.setString(4, material);  
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return isSuccess;  // Trả về true nếu cập nhật thành công, false nếu thất bại
    }



}
