package test;

import dao.Dao_NhaCungCap;
import entity.NhaCC;

import java.util.ArrayList;

public class TestDaoNhaCungCap {

    public static void main(String[] args) {
        // Tạo đối tượng Dao_NhaCungCap để gọi phương thức lấy dữ liệu
        Dao_NhaCungCap dao = new Dao_NhaCungCap();
        
        // Gọi phương thức lấy tất cả nhà cung cấp
        ArrayList<NhaCC> dsNhaCungCap = dao.layTatCaNhaCungCap();
        
        // Kiểm tra và in ra kết quả
        if (dsNhaCungCap.isEmpty()) {
            System.out.println("Không có dữ liệu nhà cung cấp.");
        } else {
            // In thông tin nhà cung cấp
            for (NhaCC nhaCC : dsNhaCungCap) {
                System.out.println("Mã NCC: " + nhaCC.getMaNCC());
                System.out.println("Tên NCC: " + nhaCC.getTenNCC());
                System.out.println("Địa chỉ: " + nhaCC.getDiaChi());
                System.out.println("Email: " + nhaCC.getEmail());
                System.out.println("Số điện thoại: " + nhaCC.getSdt());
                System.out.println("-------------------------------");
            }
        }
    }
}
