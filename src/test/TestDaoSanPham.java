package test;

import java.util.ArrayList;
import dao.Dao_SanPham;
import entity.SanPham;

public class TestDaoSanPham {
    public static void main(String[] args) {
        Dao_SanPham daoSanPham = new Dao_SanPham();
        
        // Lấy danh sách sản phẩm từ cơ sở dữ liệu
        ArrayList<SanPham> danhSachSanPham = daoSanPham.layTatCaSanPham();
        
        // Kiểm tra và in danh sách sản phẩm
        if (danhSachSanPham.isEmpty()) {
            System.out.println("Không có sản phẩm nào trong cơ sở dữ liệu.");
        } else {
            System.out.println("Danh sách sản phẩm:");
            for (SanPham sp : danhSachSanPham) {
                System.out.println("Mã SP: " + sp.getMaSP());
                System.out.println("Tên SP: " + sp.getTenSP());
                System.out.println("Danh mục: " + sp.getDanhmuc());
                System.out.println("Tồn kho: " + sp.getSoLuongTonKho());
                System.out.println("Giá nhập: " + sp.getGiaNhap());
                System.out.println("Giá bán: " + sp.getGiaBan());
                System.out.println("Thương hiệu: " + sp.getThuongHieu());
                System.out.println("Tình trạng: " + sp.getTinhtrang());
                System.out.println("-------------------------");
            }
        }
    }
}

