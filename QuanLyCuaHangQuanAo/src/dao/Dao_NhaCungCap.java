package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;


import javax.swing.*;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import connection.MyConnection1;
import entity.NhaCC;

public class Dao_NhaCungCap {
    private Connection con;
    ArrayList<NhaCC> dsNcc;

    public Dao_NhaCungCap() {
        con = MyConnection1.getInstance().getConnection();
        dsNcc = new ArrayList<NhaCC>();
    }

    public ArrayList<NhaCC> layTatCaNhaCungCap() {
        try {
            // Gọi stored procedure
            String sql = "{CALL GetAllSuppliers}";
            CallableStatement cs = con.prepareCall(sql);
            ResultSet rs = cs.executeQuery();
            
            dsNcc.clear();  // Xóa dữ liệu cũ trước khi thêm mới
            while (rs.next()) {
                String maNCC = rs.getString("supplierID");
                String tenNCC = rs.getString("supplierName");
                String diaChi = rs.getString("supplierAddress");
                String email = rs.getString("supplierEmail");
                String soDienThoai = rs.getString("supplierPhone");
                
                NhaCC nhaCC = new NhaCC(maNCC, tenNCC, diaChi, email, soDienThoai);
                dsNcc.add(nhaCC);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return dsNcc;
    }
    
    public boolean insertNhaCungCap(NhaCC nhacc) {
        String sql = "{CALL InsertSupplier(?, ?, ?, ?, ?)}"; // Đóng ngoặc SQL
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, nhacc.getMaNCC());
            ps.setString(2, nhacc.getTenNCC());
            ps.setString(3, nhacc.getDiaChi());
            ps.setString(4, nhacc.getEmail());
            ps.setString(5, nhacc.getSdt());

            // Thực thi câu lệnh
            int rowAffected = ps.executeUpdate();
            return rowAffected > 0; // Kiểm tra xem có ảnh hưởng dòng dữ liệu nào không
        } catch (SQLException e) {
            e.printStackTrace(); // In lỗi ra console
            // Bạn có thể thêm logging ở đây hoặc trả về false nếu không muốn xử lý thêm
        }
        return false; // Nếu có lỗi xảy ra hoặc không có dòng nào bị ảnh hưởng, trả về false
    }

    
    public boolean xoaNhaCungCapTheoMa(String maNCC) {
        String sql = "{CALL DeleteSupplierByID(?)}";
        try (CallableStatement stmt = con.prepareCall(sql)) {
            stmt.setString(1, maNCC);  
            int rowsAffected = stmt.executeUpdate();  
            return rowsAffected > 0;  
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    public boolean capnhatNhaCungCap(String maNCC, String tenNCC, String diaChi, String email, String soDienThoai) {
        String sql = "{CALL UpdateSupplierByID(?, ?, ?, ?, ?)}";
        try (CallableStatement stmt = con.prepareCall(sql)) {
            stmt.setString(1, maNCC);  
            stmt.setString(2, tenNCC);
            stmt.setString(3, diaChi);
            stmt.setString(4, email);
            stmt.setString(5, soDienThoai);
            // Thực thi cập nhật và kiểm tra số dòng bị ảnh hưởng
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;  // Trả về true nếu cập nhật thành công
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    
    public ArrayList<NhaCC> timNhaCungCapTheoMa(String maNCC) {
        ArrayList<NhaCC> ketQua = new ArrayList<>();
        String sql = "{CALL SearchSupplierByID(?)}";
        try (CallableStatement stmt = con.prepareCall(sql)) {
            stmt.setString(1, maNCC);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                NhaCC nhaCC = new NhaCC(
                    rs.getString("supplierID"),
                    rs.getString("supplierName"),
                    rs.getString("supplierAddress"),
                    rs.getString("supplierEmail"),
                    rs.getString("supplierPhone")
                );
                ketQua.add(nhaCC);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ketQua; // Trả về danh sách (có thể trống nếu không tìm thấy)
    }

    
    public ArrayList<NhaCC> timNhaCungCapTheoSoDienThoai(String soDienThoai) {
        ArrayList<NhaCC> ketQua = new ArrayList<>();
        String sql = "{CALL SearchSupplierByPhone(?)}";
        try (CallableStatement stmt = con.prepareCall(sql)) {
            stmt.setString(1, soDienThoai);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                ketQua.add(new NhaCC(
            		 rs.getString("supplierID"),
                     rs.getString("supplierName"),
                     rs.getString("supplierAddress"),
                     rs.getString("supplierEmail"),
                     rs.getString("supplierPhone")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ketQua;
    }
    
    public ArrayList<NhaCC> timNhaCungCapTheoEmail(String email) {
        ArrayList<NhaCC> ketQua = new ArrayList<>();
        String sql = "{CALL SearchSupplierByEmail(?)}";
        try (CallableStatement stmt = con.prepareCall(sql)) {
            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                ketQua.add(new NhaCC(
        		 rs.getString("supplierID"),
                 rs.getString("supplierName"),
                 rs.getString("supplierAddress"),
                 rs.getString("supplierEmail"),
                 rs.getString("supplierPhone")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ketQua;
    }
    
    public ArrayList<NhaCC> timNhaCungCapTheoTen(String tenNCC) {
        ArrayList<NhaCC> ketQua = new ArrayList<>();
        String sql = "{CALL SearchSupplierByName(?)}";
        try (CallableStatement stmt = con.prepareCall(sql)) {
            stmt.setString(1, tenNCC);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                ketQua.add(new NhaCC(
            		 rs.getString("supplierID"),
                     rs.getString("supplierName"),
                     rs.getString("supplierAddress"),
                     rs.getString("supplierEmail"),
                     rs.getString("supplierPhone")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ketQua;
    }


    public ArrayList<NhaCC> timNhaCungTheoDiaCi(String diaChi) {
        ArrayList<NhaCC> ketQua = new ArrayList<>();
        String sql = "{CALL SearchSupplierByAddress(?)}";
        try (CallableStatement stmt = con.prepareCall(sql)) {
            stmt.setString(1, diaChi);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                ketQua.add(new NhaCC(
	        		 rs.getString("supplierID"),
	                 rs.getString("supplierName"),
	                 rs.getString("supplierAddress"),
	                 rs.getString("supplierEmail"),
	                 rs.getString("supplierPhone")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ketQua;
    }
    
  


}
