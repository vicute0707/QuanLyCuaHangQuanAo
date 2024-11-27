	package entity;

public class SanPham {
	private String maSP, tenSP;
	private DanhMuc danhmuc;
	private int soLuongTonKho;
	private double giaNhap, giaBan;
	private  String thuongHieu;
	private String tinhtrang;
	private String linhAnh;
	private NhaCC nhacc;
	

	
	public SanPham(String maSP, String tenSP, DanhMuc danhmuc, int soLuongTonKho, double giaNhap, double giaBan,
			String thuongHieu, String tinhtrang, String linhAnh, NhaCC nhacc) {
		super();
		this.maSP = maSP;
		this.tenSP = tenSP;
		this.danhmuc = danhmuc;
		this.soLuongTonKho = soLuongTonKho;
		this.giaNhap = giaNhap;
		this.giaBan = giaBan;
		this.thuongHieu = thuongHieu;
		this.tinhtrang = tinhtrang;
		this.linhAnh = linhAnh;
		this.nhacc = nhacc;
	}
	
	public SanPham(String tenSP, DanhMuc danhmuc, int soLuongTonKho, double giaNhap, double giaBan, String thuongHieu,
			String linhAnh) {
		super();
		
		this.tenSP = tenSP;
		this.danhmuc = danhmuc;
		this.soLuongTonKho = soLuongTonKho;
		this.giaNhap = giaNhap;
		this.giaBan = giaBan;
		this.thuongHieu = thuongHieu;
		this.linhAnh = linhAnh;
	}

	public SanPham(String maSP, String tenSP, String tenDM, int soLuongTonKho, double giaNhap, double giaBan,
		 String thuongHieu, String tinhtrang) {
		 this.maSP = maSP;
		 this.tenSP = tenSP;
		 this.danhmuc = new DanhMuc(tenDM); // Giả sử bạn khởi tạo DanhMuc từ tên danh mục
		 this.soLuongTonKho = soLuongTonKho;
		 this.giaNhap = giaNhap;
		 this.giaBan = giaBan;
		 this.thuongHieu = thuongHieu;
		 this.tinhtrang = tinhtrang;
		 this.nhacc = null; // Bạn có thể truyền vào giá trị nhacc khác nếu cần
	}
	
	public SanPham(String tenSP, int soLuongTonKho, String linhAnh) {
		super();
		this.tenSP = tenSP;
		this.soLuongTonKho = soLuongTonKho;
		this.linhAnh = linhAnh;
	}

	public SanPham() {
		super();
		// TODO Auto-generated constructor stub
	}
	

	public String getMaSP() {
		return maSP;
	}

	public void setMaSP(String maSP) {
		this.maSP = maSP;
	}

	public String getTenSP() {
		return tenSP;
	}

	public void setTenSP(String tenSP) {
		this.tenSP = tenSP;
	}

	public DanhMuc getDanhmuc() {
		return danhmuc;
	}

	public void setDanhmuc(DanhMuc danhmuc) {
		this.danhmuc = danhmuc;
	}

	public int getSoLuongTonKho() {
		return soLuongTonKho;
	}

	public void setSoLuongTonKho(int soLuongTonKho) {
		this.soLuongTonKho = soLuongTonKho;
	}

	public double getGiaNhap() {
		return giaNhap;
	}

	public void setGiaNhap(double giaNhap) {
		this.giaNhap = giaNhap;
	}
	
	public String getLinhAnh() {
		return linhAnh;
	}

	public void setLinhAnh(String linhAnh) {
		this.linhAnh = linhAnh;
	}

	public double getGiaBan() {
		return giaBan;
	}

	public void setGiaBan(double giaBan) {
		this.giaBan = giaBan;
	}

	public String getThuongHieu() {
		return thuongHieu;
	}

	public void setThuongHieu(String thuongHieu) {
		this.thuongHieu = thuongHieu;
	}

	public String getTinhtrang() {
		return tinhtrang;
	}

	public void setTinhtrang(String tinhtrang) {
		this.tinhtrang = tinhtrang;
	}

	public NhaCC getNhacc() {
		return nhacc;
	}

	public void setNhacc(NhaCC nhacc) {
		this.nhacc = nhacc;
	}

	
	
}
