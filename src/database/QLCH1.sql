USE [master];
GO
CREATE DATABASE QLCH;
GO
USE QLCH;
GO

CREATE TABLE NHACUNGCAP (
    maNCC VARCHAR(10) PRIMARY KEY NOT NULL,
    tenNCC NVARCHAR(255),
    diaChi NVARCHAR(255),
	email VARCHAR(255),
    soDienThoai VARCHAR(20)
);
GO


CREATE TABLE DANHMUC (
    maDM VARCHAR(10) PRIMARY KEY NOT NULL,
    tenDM NVARCHAR(255),
    ghiChu NVARCHAR(MAX)
);
GO

CREATE TABLE SANPHAM (
    maSP VARCHAR(10) PRIMARY KEY NOT NULL,
    tenSP NVARCHAR(MAX),
    tenDM NVARCHAR(255),
    soLuongTonKho INT,
    giaNhap DECIMAL(18, 2),
    giaBan DECIMAL(18, 2),
    thuongHieu NVARCHAR(MAX),
    maDM VARCHAR(10),
    maNCC VARCHAR(10),
	linkAnh NVARCHAR(MAX),
    FOREIGN KEY (maDM) REFERENCES DANHMUC(maDM),
    FOREIGN KEY (maNCC) REFERENCES NHACUNGCAP(maNCC)
);
GO
--ALTER TABLE SANPHAM
--ADD linkAnh NVARCHAR(MAX);

-- khu vực insert
--DELETE FROM NHACUNGCAP;


INSERT INTO NHACUNGCAP (maNCC, tenNCC, diaChi, soDienThoai, email)
VALUES 
    ('NCC001', N'Xưởng dệt may Duy Tân', N'12 Nguyễn Văn Bảo, Phường 4, Gò Vấp', '0867947616', 'duytan@example.com'),
    ('NCC002', N'Công ty TNHH May Mặc ABC', N'45 Lê Lợi, Phường Bến Nghé, Quận 1', '0901234567', 'maymacabc@example.com'),
    ('NCC003', N'Xưởng May Thành Công', N'78 Võ Văn Tần, Phường 6, Quận 3', '0912345678', 'thanhcong@example.com'),
    ('NCC004', N'Công ty CP May Việt Tiến', N'123 Cách Mạng Tháng 8, Quận Tân Bình', '0923456789', 'mayviettien@example.com'),
    ('NCC005', N'Xưởng May Phương Nam', N'56 Nguyễn Thị Minh Khai, Quận 3', '0934567890', 'phuongnam@example.com'),
	('NCC006', N'Công ty Dệt Kim Đông Xuân', N'23 Trần Hưng Đạo, Hoàn Kiếm', '0945678910', 'dongxuan@example.com'),
    ('NCC007', N'Xưởng May Nam Phương', N'89 Lý Tự Trọng, Quận 1', '0987654321', 'namphuong@example.com'),
    ('NCC008', N'Công ty TNHH May Hồng Hà', N'78 Phạm Ngọc Thạch, Đống Đa', '0975647382', 'hongha@example.com'),
    ('NCC009', N'Xưởng Dệt Phong Phú', N'45 Tôn Đức Thắng, Quận 1', '0912345689', 'phongphu@example.com'),
    ('NCC010', N'Tập Đoàn May 10', N'10 Nguyễn Du, Hai Bà Trưng', '0901234876', 'may10@example.com'),
    ('NCC011', N'Xưởng May Duyên Hải', N'67 Nguyễn Văn Linh, Quận 7', '0934876591', 'duyenhai@example.com'),
    ('NCC012', N'Công ty TNHH Dệt Nam Định', N'56 Hoàng Văn Thụ, Hà Nội', '0954678901', 'detnamdinh@example.com'),
    ('NCC013', N'Xưởng May Kim Liên', N'12 Nguyễn Trãi, Thanh Xuân', '0934672183', 'kimlien@example.com'),
    ('NCC014', N'Công ty TNHH May Hùng Dũng', N'33 Cầu Giấy, Hà Nội', '0923684756', 'hungdung@example.com'),
    ('NCC015', N'Xưởng May Phú Thành', N'45 Hai Bà Trưng, Quận 1', '0967341825', 'phuthanh@example.com');
GO


INSERT INTO DANHMUC (maDM, tenDM, ghiChu)
VALUES 
    ('DM001', N'Váy', N'Các loại váy ngắn, váy dài'),
    ('DM002', N'Quần', N'Quần jean, quần tây, quần short'),
    ('DM003', N'Áo', N'Áo thun, áo sơ mi, áo khoác'),
    ('DM004', N'Phụ kiện', N'Túi xách, ví, dây nịt'),
    ('DM005', N'Giày dép', N'Giày cao gót, giày thể thao'),
	('DM006', N'Mũ', N'Mũ len, mũ lưỡi trai, mũ rộng vành'),
    ('DM007', N'Đồng hồ', N'Đồng hồ đeo tay nam, nữ, unisex'),
    ('DM008', N'Trang sức', N'Nhẫn, vòng cổ, lắc tay, hoa tai'),
    ('DM009', N'Balo', N'Balo du lịch, balo thời trang'),
    ('DM010', N'Kính mắt', N'Kính râm, kính cận, kính thời trang'),
    ('DM011', N'Đầm dự tiệc', N'Đầm dạ hội, đầm cocktail'),
    ('DM012', N'Áo polo', N'Áo polo ngắn tay, dài tay'),
    ('DM013', N'Quần legging', N'Quần legging thời trang, thể thao'),
    ('DM014', N'Áo vest', N'Áo vest nam, nữ'),
    ('DM015', N'Giày cao gót', N'Giày cao gót các loại');
GO
--DELETE FROM SANPHAM;
INSERT INTO SANPHAM (maSP, tenSP, soLuongTonKho, giaNhap, giaBan, thuongHieu, maDM, maNCC, linkAnh)
VALUES 
    ('SP001', N'Váy ngắn kẻ caro', 36, 200000, 250000, N'VietNam', 'DM001', 'NCC001', '\img\vay.png'),
    ('SP002', N'Quần dài', 3, 200000, 250000, N'Shien', 'DM002', 'NCC002', '\img\vay.png'),
    ('SP003', N'Áo thun trơn', 0, 120000, 199000, N'T-Lab', 'DM003', 'NCC003', '\img\vay.png'),
    ('SP004', N'Áo sơ mi', 15, 180000, 250000, N'Local Brand', 'DM003', 'NCC004', '\img\vay.png'),
    ('SP005', N'Quần jean', 20, 300000, 450000, N'Levi''s', 'DM002', 'NCC001', '\img\vay.png'),
    ('SP006', N'Váy dài', 8, 250000, 350000, N'Zara', 'DM001', 'NCC002', '\img\vay.png'),
    ('SP007', N'Áo khoác', 12, 400000, 600000, N'H&M', 'DM003', 'NCC003', '\img\vay.png'),
    ('SP008', N'Quần short', 25, 150000, 220000, N'Uniqlo', 'DM002', 'NCC004', '\img\vay.png'),
    ('SP009', N'Áo len', 5, 280000, 380000, N'Mango', 'DM003', 'NCC005', '\img\vay.png'),
    ('SP010', N'Váy hoa', 0, 220000, 320000, N'F21', 'DM001', 'NCC001', '\img\vay.png'),
    ('SP011', N'Mũ len', 15, 80000, 120000, N'Adidas', 'DM006', 'NCC006', '\img\vay.png'),
    ('SP012', N'Đồng hồ đeo tay nam', 5, 500000, 800000, N'Casio', 'DM007', 'NCC007', '\img\vay.png'),
    ('SP013', N'Nhẫn bạc', 20, 150000, 300000, N'Silver', 'DM008', 'NCC008', '\img\vay.png'),
    ('SP014', N'Balo du lịch', 25, 250000, 400000, N'The North Face', 'DM009', 'NCC009', '\img\vay.png'),
    ('SP015', N'Kính râm', 30, 180000, 250000, N'Ray-Ban', 'DM010', 'NCC010', '\img\vay.png'),
    ('SP016', N'Đầm dạ hội', 12, 450000, 650000, N'Zara', 'DM011', 'NCC011', '\img\vay.png'),
    ('SP017', N'Áo polo nam', 18, 220000, 300000, N'Polo', 'DM012', 'NCC012', '\img\vay.png'),
    ('SP018', N'Quần legging thể thao', 20, 150000, 230000, N'Nike', 'DM013', 'NCC013', '\img\vay.png'),
    ('SP019', N'Áo vest nữ', 10, 500000, 750000, N'H&M', 'DM014', 'NCC014', '\img\vay.png'),
    ('SP020', N'Giày cao gót', 22, 350000, 500000, N'Nine West', 'DM015', 'NCC015', '\img\vay.png');

GO


-- khu vực viết store
----------------------- Nhà Cung Cấp ----------------------------------------
-- Stored Procedure để lấy toàn bộ dữ liệu từ bảng NHACUNGCAP
CREATE PROCEDURE sp_LayToanBoNhaCungCap
AS
BEGIN
    SELECT * FROM NHACUNGCAP;
END;
GO
-- xóa nhà cung cấp
CREATE PROCEDURE sp_XoaNhaCungCap
    @maNCC VARCHAR(10)
AS
BEGIN
    DELETE FROM NHACUNGCAP
    WHERE maNCC = @maNCC;
END;
GO
-- cập nhật lại nhà cung cấp
CREATE PROCEDURE sp_CapNhatNhaCungCap
    @maNCC VARCHAR(10),
    @tenNCC NVARCHAR(255),
    @diaChi NVARCHAR(255),
    @email VARCHAR(255),
    @soDienThoai VARCHAR(20)
AS
BEGIN
    UPDATE NHACUNGCAP
    SET tenNCC = @tenNCC,
        diaChi = @diaChi,
        email = @email,
        soDienThoai = @soDienThoai
    WHERE maNCC = @maNCC;
END;
GO

--------------- tìm kiếm ---------------------
-- tìm kiếm nhà cung cấp theo mã
CREATE PROCEDURE TimNCC_TheoMa
    @maNCC VARCHAR(10)
AS
BEGIN
    SELECT *
    FROM NHACUNGCAP
    WHERE maNCC = @maNCC;
END;
GO


-- tìm kiếm theo tên
CREATE PROCEDURE TimNCC_TheoTen
    @tenNCC NVARCHAR(255)
AS
BEGIN
    SELECT *
    FROM NHACUNGCAP
    WHERE tenNCC LIKE '%' + @tenNCC + '%';
END;
GO
-- tìm kiếm theo địa chỉ
CREATE PROCEDURE TimNCC_TheoDiaChi
    @diaChi NVARCHAR(255)
AS
BEGIN
    SELECT *
    FROM NHACUNGCAP
    WHERE diaChi LIKE '%' + @diaChi + '%';
END;
GO
-- tìm kiếm theo email
CREATE PROCEDURE TimNCC_TheoEmail
    @email VARCHAR(255)
AS
BEGIN
    SELECT *
    FROM NHACUNGCAP
    WHERE email LIKE '%' + @email + '%';
END;
GO
-- tìm kiếm theo số điện thoại
CREATE PROCEDURE TimNCC_TheoSoDienThoai
    @soDienThoai VARCHAR(20)
AS
BEGIN
    SELECT *
    FROM NHACUNGCAP
    WHERE soDienThoai LIKE '%' + @soDienThoai + '%';
END;
GO




-------------------DANH MụC --------------------------------------------------
-- Stored Procedure để lấy toàn bộ dữ liệu từ bảng DANHMUC
CREATE PROCEDURE sp_LayToanBoDanhMuc
AS
BEGIN
    SELECT * FROM DANHMUC;
END;
GO
-- xóa nhà Danh Mục
CREATE PROCEDURE sp_XoaDanhMuc
    @maDM  VARCHAR(10) 
AS
BEGIN
    DELETE FROM DANHMUC 
    WHERE maDM  = @maDM;
END;

GO
-- stored insert Danh Mục
CREATE PROCEDURE sp_InsertDanhMuc
    @maDM VARCHAR(10),
    @tenDM NVARCHAR(255),
    @ghiChu NVARCHAR(MAX) = NULL  -- có thể là NULL nếu ghi chú không bắt buộc
AS
BEGIN
    INSERT INTO DANHMUC (maDM, tenDM, ghiChu)
    VALUES (@maDM, @tenDM, @ghiChu);
END;
GO

-- stored update danh mục
CREATE PROCEDURE sp_UpdateDanhMuc
    @maDM VARCHAR(10),
    @tenDM NVARCHAR(255),
    @ghiChu NVARCHAR(MAX)
AS
BEGIN
    UPDATE DANHMUC
    SET 
        tenDM = @tenDM,
        ghiChu = @ghiChu 
    WHERE maDM = @maDM;
END;

-- tìm kiếm danh mục theo tên
CREATE PROCEDURE TimDanhMUC_TheoTen
    @tenDM NVARCHAR(255)
AS
BEGIN
    SELECT *
    FROM DANHMUC
    WHERE tenDM LIKE '%' + @tenDM + '%';
END;

GO
-- Stored Procedure để lấy toàn bộ dữ liệu từ bảng SANPHAM
CREATE PROCEDURE sp_LayToanBoSanPham
AS
BEGIN
    SELECT * FROM SANPHAM;
END;
GO
-- lấy thông tin sản phẩm 
CREATE PROCEDURE sp_LaySanPhamVaDanhMuc
AS
BEGIN
    SELECT 
        sp.maSP,
        sp.tenSP,
        dm.tenDM AS tenDanhMuc,
        sp.soLuongTonKho,
        sp.giaNhap,
        sp.giaBan,
        sp.thuongHieu
    FROM 
        SANPHAM sp
    JOIN 
        DANHMUC dm ON sp.maDM = dm.maDM;
END;
GO
-- lấy thông tin sản phẩm theo mã danh mục 
CREATE PROCEDURE sp_LayThongTinSanPhamTheoMaDM
    @maDM VARCHAR(10)
AS
BEGIN
    -- Kiểm tra điều kiện nếu tham số đầu vào không được cung cấp
    IF @maDM IS NULL
    BEGIN
        PRINT 'Vui lòng cung cấp mã danh mục.';
        RETURN;
    END;

    -- Lấy thông tin sản phẩm theo mã danh mục
    SELECT 
        tenSP AS TenSanPham,
        soLuongTonKho AS SoLuongTonKho,
        linkAnh AS LinkAnh
    FROM 
        SANPHAM
    WHERE 
        maDM = @maDM;
END;
GO
-- xóa sản phẩm
CREATE PROCEDURE sp_XoaSanPham
    @maSP VARCHAR(10)
AS
BEGIN
    -- Xóa sản phẩm dựa trên mã sản phẩm
    DELETE FROM SANPHAM
    WHERE maSP = @maSP;
END;
GO

------------ Tìm kiếm -----------------
CREATE PROCEDURE TimSanPhamTheoMaSP
    @maSP VARCHAR(10)
AS
BEGIN
    SELECT 
        SP.maSP AS [Mã SP],
        SP.tenSP AS [Tên sản phẩm],
        DM.tenDM AS [Danh mục],
        SP.soLuongTonKho AS [Số lượng tồn],
        SP.giaNhap AS [Giá nhập],
        SP.giaBan AS [Giá bán],
        SP.thuongHieu AS [Thương hiệu]
    FROM 
        SANPHAM SP
    LEFT JOIN 
        DANHMUC DM ON SP.maDM = DM.maDM
    WHERE 
        SP.maSP = @maSP;
END;
GO
CREATE PROCEDURE TimSanPhamTheoTenSP
    @tenSP NVARCHAR(MAX)
AS
BEGIN
    SELECT 
        SP.maSP AS [Mã SP],
        SP.tenSP AS [Tên sản phẩm],
        DM.tenDM AS [Danh mục],
        SP.soLuongTonKho AS [Số lượng tồn],
        SP.giaNhap AS [Giá nhập],
        SP.giaBan AS [Giá bán],
        SP.thuongHieu AS [Thương hiệu]
    FROM 
        SANPHAM SP
    LEFT JOIN 
        DANHMUC DM ON SP.maDM = DM.maDM
    WHERE 
        SP.tenSP LIKE '%' + @tenSP + '%';
END;
GO
CREATE PROCEDURE TimSanPhamTheoGiaNhap
    @giaNhapMin DECIMAL(18, 2),
    @giaNhapMax DECIMAL(18, 2)
AS
BEGIN
    SELECT 
        SP.maSP AS [Mã SP],
        SP.tenSP AS [Tên sản phẩm],
        DM.tenDM AS [Danh mục],
        SP.soLuongTonKho AS [Số lượng tồn],
        SP.giaNhap AS [Giá nhập],
        SP.giaBan AS [Giá bán],
        SP.thuongHieu AS [Thương hiệu]
    FROM 
        SANPHAM SP
    LEFT JOIN 
        DANHMUC DM ON SP.maDM = DM.maDM
    WHERE 
        SP.giaNhap BETWEEN @giaNhapMin AND @giaNhapMax;
END;
GO
CREATE PROCEDURE TimSanPhamTheoGiaBan
    @giaBanMin DECIMAL(18, 2),
    @giaBanMax DECIMAL(18, 2)
AS
BEGIN
    SELECT 
        SP.maSP AS [Mã SP],
        SP.tenSP AS [Tên sản phẩm],
        DM.tenDM AS [Danh mục],
        SP.soLuongTonKho AS [Số lượng tồn],
        SP.giaNhap AS [Giá nhập],
        SP.giaBan AS [Giá bán],
        SP.thuongHieu AS [Thương hiệu]
    FROM 
        SANPHAM SP
    LEFT JOIN 
        DANHMUC DM ON SP.maDM = DM.maDM
    WHERE 
        SP.giaBan BETWEEN @giaBanMin AND @giaBanMax;
END;
GO
CREATE PROCEDURE TimSanPhamTheoThuongHieu
    @thuongHieu NVARCHAR(MAX)
AS
BEGIN
    SELECT 
        SP.maSP AS [Mã SP],
        SP.tenSP AS [Tên sản phẩm],
        DM.tenDM AS [Danh mục],
        SP.soLuongTonKho AS [Số lượng tồn],
        SP.giaNhap AS [Giá nhập],
        SP.giaBan AS [Giá bán],
        SP.thuongHieu AS [Thương hiệu]
    FROM 
        SANPHAM SP
    LEFT JOIN 
        DANHMUC DM ON SP.maDM = DM.maDM
    WHERE 
        SP.thuongHieu LIKE '%' + @thuongHieu + '%';
END;
GO





