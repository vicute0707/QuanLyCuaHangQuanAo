package dao;

import java.sql.*;
import java.util.ArrayList;
import connection.MyConnection;
import entity.PhanLoaiSanPham;

public class Dao_PhanLoaiSanPham {
    private Connection con;
    private ArrayList<PhanLoaiSanPham> dsPL;

    public Dao_PhanLoaiSanPham() {
        // Lấy kết nối từ MyConnection
        con = MyConnection.getInstance().connect();
        dsPL = new ArrayList<>();
    }

    // Lấy thông tin ProductVariant theo productID
    public ArrayList<PhanLoaiSanPham> getProductVariantsByProductID(String productID) {
        ArrayList<PhanLoaiSanPham> result = new ArrayList<>();
        String storedProc = "{CALL GetProductVariantsByProductID(?)}";

        try (CallableStatement stmt = con.prepareCall(storedProc)) {
            stmt.setString(1, productID);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    String mapl = rs.getString("variantID");
                    String color = rs.getString("color");
                    String size = rs.getString("size");
                    String material = rs.getString("material");

                    PhanLoaiSanPham plsp = new PhanLoaiSanPham(mapl, size, color, material);
                    result.add(plsp);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error fetching product variants by productID", e);
        }

        return result;
    }

    // Đếm tổng số ProductVariant
    public int getTotalProductVariants() {
        int totalRows = 0;
        String sql = "SELECT COUNT(*) AS totalRows FROM ProductVariant";

        try (PreparedStatement stmt = con.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            if (rs.next()) {
                totalRows = rs.getInt("totalRows");
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error counting total product variants", e);
        }

        return totalRows;
    }

    // Thêm mới ProductVariant
    public boolean insertProductVariant(String variantID, String productID, String color, String size, String material) {
        String storedProc = "{CALL InsertProductVariant(?, ?, ?, ?, ?)}";

        try (CallableStatement stmt = con.prepareCall(storedProc)) {
            stmt.setString(1, variantID);
            stmt.setString(2, productID);
            stmt.setString(3, color);
            stmt.setString(4, size);
            stmt.setString(5, material);

            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Error inserting product variant", e);
        }
    }

    // Xóa ProductVariant theo variantID
    public boolean xoaPhanLoaiSanPham(String variantID) {
        String sql = "{CALL DeleteProductVariant(?)}";

        try (CallableStatement stmt = con.prepareCall(sql)) {
            stmt.setString(1, variantID);

            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Error deleting product variant", e);
        }
    }

    // Cập nhật ProductVariant
    public boolean capNhatProductVariant(String variantID, String color, String size, String material) {
        String sql = "{CALL UpdateProductVariant(?, ?, ?, ?)}";

        try (CallableStatement stmt = con.prepareCall(sql)) {
            stmt.setString(1, variantID);
            stmt.setString(2, color);
            stmt.setString(3, size);
            stmt.setString(4, material);

            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Error updating product variant", e);
        }
    }
}
