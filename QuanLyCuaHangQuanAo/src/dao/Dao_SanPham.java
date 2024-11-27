package dao;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import connection.MyConnection;
import entity.DanhMuc;
import entity.SanPham;

public class Dao_SanPham {
    private Connection con;
    ArrayList<SanPham> dsSP;

    public Dao_SanPham() {
        // Sử dụng MyConnection để lấy kết nối
        con = MyConnection.getInstance().connect();
        dsSP = new ArrayList<SanPham>();
    }

    public ArrayList<SanPham> layTatCaSanPham() {
        try {
            String sql = "{CALL GetProductDetails}";
            CallableStatement cs = con.prepareCall(sql);
            ResultSet rs = cs.executeQuery();
            dsSP.clear();
            
            while (rs.next()) {
                String maSP = rs.getString("productID");
                String tenSP = rs.getString("productName");
                String tenDM = rs.getString("categoryName");
                int tonkho = rs.getInt("stockQuantity");
                double giaNhap = rs.getDouble("importPrice");
                double giaBan = rs.getDouble("sellPrice");
                String thuongHieu = rs.getString("brand");
                
                // Xác định tình trạng hàng dựa trên số lượng tồn kho
                String tinhTrang;
                if (tonkho > 10) {
                    tinhTrang = "Còn hàng";
                } else if (tonkho == 0) {
                    tinhTrang = "Hết hàng";
                } else {
                    tinhTrang = "Sắp hết";
                }

                SanPham sanpham = new SanPham(maSP, tenSP, tenDM, tonkho, giaNhap, giaBan, thuongHieu, tinhTrang);
                dsSP.add(sanpham);
            }
            
            rs.close();
            cs.close();
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return dsSP;
    }

    public boolean XoaSanPhamTheoMa(String maSP) {
        String sql = "{CALL DeleteProductByID(?)}";
        try (CallableStatement stmt = con.prepareCall(sql)) {
            stmt.setString(1, maSP);
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public ArrayList<SanPham> timSanPhamTheoMa(String maSP) {
        ArrayList<SanPham> ketQua = new ArrayList<>();
        String sql = "{CALL SearchProductByID(?)}";
        try (CallableStatement cs = con.prepareCall(sql)) {
            cs.setString(1, maSP);
            ResultSet rs = cs.executeQuery();

            while (rs.next()) {
                String maSP1 = rs.getString("productID");
                String tenSP = rs.getString("productName");
                String tenDM = rs.getString("categoryName");
                int tonkho = rs.getInt("stockQuantity");
                double giaNhap = rs.getDouble("importPrice");
                double giaBan = rs.getDouble("sellPrice");
                String thuongHieu = rs.getString("brand");

                String tinhTrang = tonkho > 10 ? "Còn hàng" : (tonkho == 0 ? "Hết hàng" : "Sắp hết");

                SanPham sp = new SanPham(maSP1, tenSP, tenDM, tonkho, giaNhap, giaBan, thuongHieu, tinhTrang);
                ketQua.add(sp);
            }
            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ketQua;
    }

    public ArrayList<SanPham> timSanPhamTheoTenSP(String tenSP) {
        ArrayList<SanPham> ketQua = new ArrayList<>();
        String sql = "{CALL SearchProductByName(?)}";
        try (CallableStatement cs = con.prepareCall(sql)) {
            cs.setString(1, tenSP);
            ResultSet rs = cs.executeQuery();

            while (rs.next()) {
                String maSP = rs.getString("productID");
                String tenSP1 = rs.getString("productName");
                String tenDM = rs.getString("categoryName");
                int tonkho = rs.getInt("stockQuantity");
                double giaNhap = rs.getDouble("importPrice");
                double giaBan = rs.getDouble("sellPrice");
                String thuongHieu = rs.getString("brand");

                String tinhTrang = tonkho > 10 ? "Còn hàng" : (tonkho == 0 ? "Hết hàng" : "Sắp hết");

                SanPham sp = new SanPham(maSP, tenSP1, tenDM, tonkho, giaNhap, giaBan, thuongHieu, tinhTrang);
                ketQua.add(sp);
            }
            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ketQua;
    }

    public ArrayList<SanPham> timSanPhamTheoThuongHieu(String thuongHieu) {
        ArrayList<SanPham> ketQua = new ArrayList<>();
        String sql = "{CALL SearchProductByBrand(?)}";
        try (CallableStatement cs = con.prepareCall(sql)) {
            cs.setString(1, thuongHieu);
            ResultSet rs = cs.executeQuery();

            while (rs.next()) {
                String maSP = rs.getString("productID");
                String tenSP = rs.getString("productName");
                String tenDM = rs.getString("categoryName");
                int tonkho = rs.getInt("stockQuantity");
                double giaNhap = rs.getDouble("importPrice");
                double giaBan = rs.getDouble("sellPrice");
                String thuongHieu1 = rs.getString("brand");

                String tinhTrang = tonkho > 10 ? "Còn hàng" : (tonkho == 0 ? "Hết hàng" : "Sắp hết");

                SanPham sp = new SanPham(maSP, tenSP, tenDM, tonkho, giaNhap, giaBan, thuongHieu1, tinhTrang);
                ketQua.add(sp);
            }
            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ketQua;
    }
    public ArrayList<SanPham> timSanPhamTheoTenDM(String tenDM) {
        ArrayList<SanPham> ketQua = new ArrayList<>();
        String sql = "{CALL SearchProductByName(?)}";
        try (CallableStatement cs = con.prepareCall(sql)) {
            cs.setString(1, tenDM);
            ResultSet rs = cs.executeQuery();

            while (rs.next()) {
                String maSP = rs.getString("productID");
                String tenSP = rs.getString("productName");
                String tenDM1 = rs.getString("categoryName");
                int tonkho = rs.getInt("stockQuantity");
                double giaNhap = rs.getDouble("importPrice");
                double giaBan = rs.getDouble("sellPrice");
                String thuongHieu1 = rs.getString("brand");

                String tinhTrang = tonkho > 10 ? "Còn hàng" : (tonkho == 0 ? "Hết hàng" : "Sắp hết");

                SanPham sp = new SanPham(maSP, tenSP, tenDM1, tonkho, giaNhap, giaBan, thuongHieu1, tinhTrang);
                ketQua.add(sp);
            }
            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ketQua;
    }

    public SanPham laySanPhamTheoMa(String maSP) {
        SanPham sanPham = null;
        String sql = "{CALL GetProductByID(?)}";

        try (CallableStatement cs = con.prepareCall(sql)) {
            cs.setString(1, maSP);
            ResultSet rs = cs.executeQuery();

            if (rs.next()) {
                String tenSP = rs.getString("productName");
                String tenDM = rs.getString("categoryName");
                int soLuongTon = rs.getInt("stockQuantity");
                double giaNhap = rs.getDouble("importPrice");
                double giaBan = rs.getDouble("sellPrice");
                String thuongHieu = rs.getString("brand");
                String linhAnh = rs.getString("imagePath");

                DanhMuc danhMuc = new DanhMuc(tenDM);

                sanPham = new SanPham(tenSP, danhMuc, soLuongTon, giaNhap, giaBan, thuongHieu, linhAnh);
            }
            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return sanPham;
    }

    public ArrayList<String> getAllCategoryNames() {
        ArrayList<String> categoryNames = new ArrayList<>();
        String sql = "{CALL GetAllCategoryNames()}";

        try (CallableStatement stmt = con.prepareCall(sql)) {
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                String categoryName = rs.getString("name");
                categoryNames.add(categoryName);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return categoryNames;
    }

    public boolean themSanPham(String productID, String productName, String categoryID, 
                               int stockQuantity, double importPrice, double sellPrice, 
                               String brand, String imagePath) {
        String sql = "{CALL AddProduct(?, ?, ?, ?, ?, ?, ?, ?)}";

        try (CallableStatement cs = con.prepareCall(sql)) {
            cs.setString(1, productID);
            cs.setString(2, productName);
            cs.setString(3, categoryID);
            cs.setInt(4, stockQuantity);
            cs.setDouble(5, importPrice);
            cs.setDouble(6, sellPrice);
            cs.setString(7, brand);
            cs.setString(8, imagePath);

            cs.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.err.println("Error while adding product: " + e.getMessage());
        }

        return false;
    }

    public String getCategoryIDByName(String categoryName) {
        String categoryID = null;
        String sql = "SELECT categoryID FROM Category WHERE name = ?";

        try (PreparedStatement preparedStatement = con.prepareStatement(sql)) {
            preparedStatement.setString(1, categoryName);
            ResultSet rs = preparedStatement.executeQuery();

            if (rs.next()) {
                categoryID = rs.getString("categoryID");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return categoryID;
    }
}
