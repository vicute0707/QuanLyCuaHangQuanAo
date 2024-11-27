package dao;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import connection.MyConnection;
import entity.DanhMuc;
import entity.SanPham;

public class Dao_DanhMuc {
	  private Connection con;
	    ArrayList<DanhMuc> dsDM;

	    public Dao_DanhMuc() {
	        con = MyConnection.getInstance().getConnection();
	        dsDM = new ArrayList<DanhMuc>();
	    }
	    public ArrayList<DanhMuc> layTatCaDanhMuc(){
	    	try {
	    		String sql = "{CALL GetAllCategories}";
	    		CallableStatement cs = con.prepareCall(sql);
	    		ResultSet rs = cs.executeQuery();
	    		dsDM.clear();
	    		while(rs.next()) {
	    			String maDM = rs.getString("categoryID");
	    			String tenDM = rs.getString("categoryName");
	    			String ghiChu = rs.getString("categoryDescription");
	    			DanhMuc danhmuc  = new DanhMuc(maDM, tenDM, ghiChu);
	    			dsDM.add(danhmuc);
	    		}
				
			} catch (SQLException e) {
				// TODO: handle exception
				e.printStackTrace();
			}
	    	return dsDM;
	    }
	    
	    // Method to insert a new category
	    public boolean themDanhMuc(String maDM, String tenDM, String ghiChu) {
	        String sql = "{CALL InsertCategory(?, ?, ?)}";
	        try (CallableStatement stmt = con.prepareCall(sql)) {
	            stmt.setString(1, maDM);
	            stmt.setString(2, tenDM);
	            if (ghiChu != null) {
	                stmt.setString(3, ghiChu);
	            } else {
	                stmt.setNull(3, java.sql.Types.NVARCHAR);
	            }
	            int rowsAffected = stmt.executeUpdate();
	            return rowsAffected > 0;
	        } catch (SQLException e) {
	            e.printStackTrace();
	            // TODO: handle exception
	        }
	        return false;
	    }

	    // Method to update category name
	    public boolean capNhatTenDanhMuc(String maDM, String tenDM, String ghichu) {
	        String sql = "{CALL UpdateCategoryByID(?, ?,?)}";
	        try (CallableStatement stmt = con.prepareCall(sql)) {
	            stmt.setString(1, maDM);
	            stmt.setString(2, tenDM);
	            stmt.setString(3, ghichu);
	            
	            int rowsAffected = stmt.executeUpdate();
	            return rowsAffected > 0;
	        } catch (SQLException e) {
	            e.printStackTrace();
	            // TODO: handle exception
	        }
	        return false;
	    }
	    
	    public boolean XoaDanhMuc(String maDM) {
	    	String sql = "{CALL DeleteCategoryByID(?)}";
	    	try(CallableStatement stmt = con.prepareCall(sql)){
	    		stmt.setString(1, maDM);
	    		int rowsAffected = stmt.executeUpdate();
	    		return rowsAffected > 0;
	    	}catch (SQLException e) {
	    		e.printStackTrace();
				// TODO: handle exception
			}
	    	return false;
	    }
	    
	    public ArrayList<DanhMuc> timDanhMucTheoTen(String tenDM) {
	        String sql = "{CALL SearchCategoryByName(?)}";
	        ArrayList<DanhMuc> ketQuaTimKiem = new ArrayList<>();

	        try (CallableStatement stmt = con.prepareCall(sql)) {
	            // Thiết lập tham số cho stored procedure
	            stmt.setString(1, tenDM);

	            // Thực thi stored procedure và lấy kết quả
	            try (ResultSet rs = stmt.executeQuery()) {
	                while (rs.next()) {
	                    // Lấy dữ liệu từ ResultSet
	                    String maDM = rs.getString("categoryID");
	                    String tenDanhMuc = rs.getString("categoryName");
	                    String ghiChu = rs.getString("categoryDescription");

	                    // Tạo đối tượng DanhMuc và thêm vào danh sách kết quả
	                    DanhMuc danhMuc = new DanhMuc(maDM, tenDanhMuc, ghiChu);
	                    ketQuaTimKiem.add(danhMuc);
	                }
	            }
	        } catch (SQLException e) {
	            e.printStackTrace();
	        }

	        return ketQuaTimKiem;
	    }
	    
	    
	    
	    
	 // Phương thức lấy sản phẩm theo mã danh mục
	    public List<SanPham> LaySPTheomaDM(String maDM) {
	        String sql = "{CALL GetProductsByCategory(?)}";
	        List<SanPham> danhSachSanPhamtheoDM = new ArrayList<>();

	        try (CallableStatement stmt = con.prepareCall(sql)) {
	            // Thiết lập tham số cho stored procedure
	            stmt.setString(1, maDM);

	            // Thực thi stored procedure và lấy kết quả
	            try (ResultSet rs = stmt.executeQuery()) {
	                while (rs.next()) {
	                    // Lấy dữ liệu từ ResultSet
	                    String tenSP = rs.getString("productName");
	                    int soLuongTonKho = rs.getInt("stockQuantity");
	                    String linkAnh = rs.getString("imagePath");

	                    // Tạo đối tượng SanPham và thêm vào danh sách
	                    SanPham sp = new SanPham(tenSP, soLuongTonKho, linkAnh);
	                    danhSachSanPhamtheoDM.add(sp);
	                }
	            }
	        } catch (SQLException e) {
	            e.printStackTrace();
	        }

	        return danhSachSanPhamtheoDM;
	    }
	    
	

	    
	  
}
