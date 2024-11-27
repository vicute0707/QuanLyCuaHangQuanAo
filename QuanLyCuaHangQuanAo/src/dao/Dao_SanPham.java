package dao;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import connection.MyConnection1;
import entity.DanhMuc;
import entity.SanPham;

public class Dao_SanPham {
    private Connection con;
    ArrayList<SanPham> dsSP;

    public Dao_SanPham() {
        con = MyConnection1.getInstance().getConnection();
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
            
            // Đóng ResultSet và CallableStatement sau khi sử dụng
            rs.close();
            cs.close();
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return dsSP;
    }
    public boolean XoaSanPhamTheoMa(String maSP) {
    	String sql = "{CALL DeleteProductByID(?)}";
    	try(CallableStatement stmt = con.prepareCall(sql)) {
    		stmt.setString(1, maSP);
    		int rowsAffected = stmt.executeUpdate();  
            return rowsAffected > 0; 
			
		} catch (SQLException e) {
			// TODO: handle exception
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
                String thuongHieu = rs.getString("brand");

                String tinhTrang = tonkho > 10 ? "Còn hàng" : (tonkho == 0 ? "Hết hàng" : "Sắp hết");

                SanPham sp = new SanPham(maSP, tenSP, tenDM1, tonkho, giaNhap, giaBan, thuongHieu, tinhTrang);
                ketQua.add(sp);
            }
            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ketQua;
    }

//    public ArrayList<SanPham> timSanPhamTheoGiaNhap(double giaNhapMin, double giaNhapMax) {
//        ArrayList<SanPham> ketQua = new ArrayList<>();
//        String sql = "{CALL TimSanPhamTheoGiaNhap(?, ?)}";
//        try (CallableStatement cs = con.prepareCall(sql)) {
//            cs.setDouble(1, giaNhapMin);
//            cs.setDouble(2, giaNhapMax);
//            ResultSet rs = cs.executeQuery();
//
//            while (rs.next()) {
//                String maSP = rs.getString("maSP");
//                String tenSP = rs.getString("tenSP");
//                String tenDM = rs.getString("Danh mục");
//                int tonkho = rs.getInt("Số lượng tồn");
//                double giaNhap = rs.getDouble("Giá nhập");
//                double giaBan = rs.getDouble("Giá bán");
//                String thuongHieu = rs.getString("Thương hiệu");
//
//                String tinhTrang = tonkho > 10 ? "Còn hàng" : (tonkho == 0 ? "Hết hàng" : "Sắp hết");
//
//                SanPham sp = new SanPham(maSP, tenSP, tenDM, tonkho, giaNhap, giaBan, thuongHieu, tinhTrang);
//                ketQua.add(sp);
//            }
//            rs.close();
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//        return ketQua;
//    }
//
//    public ArrayList<SanPham> timSanPhamTheoGiaBan(double giaBanMin, double giaBanMax) {
//        ArrayList<SanPham> ketQua = new ArrayList<>();
//        String sql = "{CALL TimSanPhamTheoGiaBan(?, ?)}";
//        try (CallableStatement cs = con.prepareCall(sql)) {
//            cs.setDouble(1, giaBanMin);
//            cs.setDouble(2, giaBanMax);
//            ResultSet rs = cs.executeQuery();
//
//            while (rs.next()) {
//                String maSP = rs.getString("maSP");
//                String tenSP = rs.getString("tenSP");
//                String tenDM = rs.getString("Danh mục");
//                int tonkho = rs.getInt("Số lượng tồn");
//                double giaNhap = rs.getDouble("Giá nhập");
//                double giaBan = rs.getDouble("Giá bán");
//                String thuongHieu = rs.getString("Thương hiệu");
//
//                String tinhTrang = tonkho > 10 ? "Còn hàng" : (tonkho == 0 ? "Hết hàng" : "Sắp hết");
//
//                SanPham sp = new SanPham(maSP, tenSP, tenDM, tonkho, giaNhap, giaBan, thuongHieu, tinhTrang);
//                ketQua.add(sp);
//            }
//            rs.close();
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//        return ketQua;
//    }
    
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
    
//    public SanPham laySanPhamTheoMa(String maSP) {
//        SanPham sanPham = null; // Đối tượng để lưu kết quả
//        String sql = "{CALL GetProductByID(?)}"; // Stored procedure
//
//        try (CallableStatement cs = con.prepareCall(sql)) {
//            cs.setString(1, maSP); // Gán tham số mã sản phẩm vào stored procedure
//            ResultSet rs = cs.executeQuery(); // Thực thi stored procedure
//
//            if (rs.next()) { // Nếu có kết quả trả về
//                String tenSP = rs.getString("productName");
//                String tenDM = rs.getString("categoryName");
//                int soLuongTon = rs.getInt("stockQuantity");
//                double giaNhap = rs.getDouble("importPrice");
//                double giaBan = rs.getDouble("sellPrice");
//                String thuongHieu = rs.getString("brand");
//                String linhAnh = rs.getString("imagePath");
//
//      
//             
//
//                // Tạo đối tượng SanPham
//                sanPham = new SanPham(tenSP, tenDM,soLuongTon, giaNhap, giaBan, thuongHieu, linhAnh);
//            }
//            rs.close(); // Đóng ResultSet
//        } catch (SQLException e) {
//            e.printStackTrace(); // Xử lý lỗi nếu xảy ra
//        }
//
//        return sanPham; // Trả về đối tượng SanPham hoặc null nếu không tìm thấy
//    }
    public SanPham laySanPhamTheoMa(String maSP) {
        SanPham sanPham = null; // Đối tượng để lưu kết quả
        String sql = "{CALL GetProductByID(?)}"; // Stored procedure

        try (CallableStatement cs = con.prepareCall(sql)) {
            cs.setString(1, maSP); // Gán tham số mã sản phẩm vào stored procedure
            ResultSet rs = cs.executeQuery(); // Thực thi stored procedure

            if (rs.next()) { // Nếu có kết quả trả về
                String tenSP = rs.getString("productName");
                String tenDM = rs.getString("categoryName");
                int soLuongTon = rs.getInt("stockQuantity");
                double giaNhap = rs.getDouble("importPrice");
                double giaBan = rs.getDouble("sellPrice");
                String thuongHieu = rs.getString("brand");
                String linhAnh = rs.getString("imagePath");

                // Tạo đối tượng DanhMuc
                DanhMuc danhMuc = new DanhMuc(tenDM);

                // Tạo đối tượng SanPham
                sanPham = new SanPham(tenSP, danhMuc, soLuongTon, giaNhap, giaBan, thuongHieu, linhAnh);
            }
            rs.close(); // Đóng ResultSet
        } catch (SQLException e) {
            e.printStackTrace(); // Xử lý lỗi nếu xảy ra
        }

        return sanPham; // Trả về đối tượng SanPham hoặc null nếu không tìm thấy
    }
    public ArrayList<String> getAllCategoryNames() {
        ArrayList<String> categoryNames = new ArrayList<>();
        String sql = "{CALL GetAllCategoryNames()}";  // Stored procedure call

        try (CallableStatement stmt = con.prepareCall(sql)) {
            // Thực thi stored procedure
            ResultSet rs = stmt.executeQuery();

            // Duyệt qua kết quả và thêm vào danh sách
            while (rs.next()) {
                String categoryName = rs.getString("name");
                categoryNames.add(categoryName);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return categoryNames;  // Trả về danh sách tên danh mục
    }

    
 // Phương thức thêm sản phẩm
    public boolean themSanPham(String productID, String productName, String categoryID, 
                               int stockQuantity, double importPrice, double sellPrice, 
                               String brand, String imagePath) {
        // Câu lệnh gọi stored procedure
        String sql = "{CALL AddProduct(?, ?, ?, ?, ?, ?, ?, ?)}";  
        
        try (CallableStatement cs = con.prepareCall(sql)) {
            // Truyền các tham số vào stored procedure
            cs.setString(1, productID);        // IN productID
            cs.setString(2, productName);     // IN productName
            cs.setString(3, categoryID);      // IN categoryID
            cs.setInt(4, stockQuantity);      // IN stockQuantity
            cs.setDouble(5, importPrice);     // IN importPrice
            cs.setDouble(6, sellPrice);       // IN sellPrice
            cs.setString(7, brand);           // IN brand
            cs.setString(8, imagePath);       // IN imagePath
            
            // Thực thi stored procedure
            cs.executeUpdate();

            return true;  // Thành công
        } catch (SQLException e) {
            // In lỗi ra console
            System.err.println("Error while adding product: " + e.getMessage());
        }

        return false;  // Thất bại
    }

    
 // Phương thức để lấy categoryID theo name
    public String getCategoryIDByName(String categoryName) {
        String categoryID = null;
        String sql = "SELECT categoryID FROM Category WHERE name = ?";

        try (PreparedStatement preparedStatement = con.prepareStatement(sql)) {  // Sử dụng kết nối con đã được khởi tạo
            preparedStatement.setString(1, categoryName);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    categoryID = resultSet.getString("categoryID");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return categoryID; 
    }



    
}
