package dao;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import connection.MyConnection;
import entity.NhaCC;

public class Dao_NhaCungCap {
    private final Connection con; // Sử dụng biến final để nhấn mạnh kết nối chỉ được khởi tạo một lần.
    private final ArrayList<NhaCC> dsNcc; // Danh sách nhà cung cấp.

    public Dao_NhaCungCap() {
        con = MyConnection.getInstance().getConnection();
        dsNcc = new ArrayList<>();
    }

    // Lấy tất cả nhà cung cấp
    public ArrayList<NhaCC> layTatCaNhaCungCap() {
        String sql = "{CALL GetAllSuppliers}";
        try (CallableStatement cs = con.prepareCall(sql);
             ResultSet rs = cs.executeQuery()) {
            dsNcc.clear(); // Xóa danh sách cũ
            while (rs.next()) {
                dsNcc.add(new NhaCC(
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
        return dsNcc;
    }

    // Thêm nhà cung cấp
    public boolean themNhaCungCap(NhaCC nhaCC) {
        String sql = "{CALL InsertSupplier(?, ?, ?, ?, ?)}";
        try (CallableStatement stmt = con.prepareCall(sql)) {
            stmt.setString(1, nhaCC.getMaNCC());
            stmt.setString(2, nhaCC.getTenNCC());
            stmt.setString(3, nhaCC.getDiaChi());
            stmt.setString(4, nhaCC.getEmail());
            stmt.setString(5, nhaCC.getSdt());
            return stmt.executeUpdate() > 0; // Kiểm tra dòng bị ảnh hưởng.
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Xóa nhà cung cấp theo mã
    public boolean xoaNhaCungCapTheoMa(String maNCC) {
        String sql = "{CALL DeleteSupplierByID(?)}";
        try (CallableStatement stmt = con.prepareCall(sql)) {
            stmt.setString(1, maNCC);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Cập nhật nhà cung cấp
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
    
    public boolean insertNhaCungCap(NhaCC nhacc) {
        String sql = "{CALL InsertSupplier(?, ?, ?, ?, ?)}"; // Đóng ngoặc SQL
        try (CallableStatement stmt = con.prepareCall(sql)) {
        	stmt.setString(1, nhacc.getMaNCC());
        	stmt.setString(2, nhacc.getTenNCC());
        	stmt.setString(3, nhacc.getDiaChi());
        	stmt.setString(4, nhacc.getEmail());
        	stmt.setString(5, nhacc.getSdt());

            // Thực thi câu lệnh
            int rowAffected = stmt.executeUpdate();
            return rowAffected > 0; 
        } catch (SQLException e) {
            e.printStackTrace(); 
            
        }
        return false; 
    }

    // Tìm nhà cung cấp theo mã
    public ArrayList<NhaCC> timNhaCungCapTheoMa(String maNCC) {
        String sql = "{CALL SearchSupplierByID(?)}";
        return layNhaCungCapTheoDieuKien(sql, maNCC);
    }

    // Tìm nhà cung cấp theo tên
    public ArrayList<NhaCC> timNhaCungCapTheoTen(String tenNCC) {
        String sql = "{CALL SearchSupplierByName(?)}";
        return layNhaCungCapTheoDieuKien(sql, tenNCC);
    }
    public ArrayList<NhaCC> timNhaCungTheoDiaCi(String diaChi) {
        
        String sql = "{CALL SearchSupplierByAddress(?)}";
        return layNhaCungCapTheoDieuKien(sql, diaChi);
    }

    // Tìm nhà cung cấp theo số điện thoại
    public ArrayList<NhaCC> timNhaCungCapTheoSoDienThoai(String soDienThoai) {
        String sql = "{CALL SearchSupplierByPhone(?)}";
        return layNhaCungCapTheoDieuKien(sql, soDienThoai);
    }

    // Tìm nhà cung cấp theo email
    public ArrayList<NhaCC> timNhaCungCapTheoEmail(String email) {
        String sql = "{CALL SearchSupplierByEmail(?)}";
        return layNhaCungCapTheoDieuKien(sql, email);
    }

    // Tìm nhà cung cấp theo địa chỉ
    public ArrayList<NhaCC> timNhaCungCapTheoDiaChi(String diaChi) {
        String sql = "{CALL SearchSupplierByAddress(?)}";
        return layNhaCungCapTheoDieuKien(sql, diaChi);
    }

    // Phương thức chung để lấy nhà cung cấp dựa vào điều kiện
    private ArrayList<NhaCC> layNhaCungCapTheoDieuKien(String sql, String param) {
        ArrayList<NhaCC> ketQua = new ArrayList<>();
        try (CallableStatement stmt = con.prepareCall(sql)) {
            stmt.setString(1, param);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    ketQua.add(new NhaCC(
                        rs.getString("supplierID"),
                        rs.getString("supplierName"),
                        rs.getString("supplierAddress"),
                        rs.getString("supplierEmail"),
                        rs.getString("supplierPhone")
                    ));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ketQua;
    }
}
