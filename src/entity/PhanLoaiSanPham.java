package entity;

public class PhanLoaiSanPham {
	private String  maPL;
	private SanPham sanpham;
	private int size;
	private String color;
	private int soluong;
	private double giaban;
	public PhanLoaiSanPham(String maPL, SanPham sanpham, int size, String color, int soluong, double giaban) {
		super();
		this.maPL = maPL;
		this.sanpham = sanpham;
		this.size = size;
		this.color = color;
		this.soluong = soluong;
		this.giaban = giaban;
	}
	public PhanLoaiSanPham() {
		super();
		// TODO Auto-generated constructor stub
	}
	
}
