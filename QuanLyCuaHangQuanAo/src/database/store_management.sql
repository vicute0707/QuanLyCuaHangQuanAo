-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Máy chủ: 127.0.0.1:3307
-- Thời gian đã tạo: Th10 26, 2024 lúc 11:22 AM
-- Phiên bản máy phục vụ: 10.4.32-MariaDB
-- Phiên bản PHP: 8.0.30

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Cơ sở dữ liệu: `store_management`
-- Tạo cơ sở dữ liệu
DROP DATABASE IF EXISTS store_management;
CREATE DATABASE store_management DEFAULT CHARACTER SET utf8 COLLATE utf8_unicode_ci;
USE store_management;

-- Tạo bảng `category`
CREATE TABLE `category` (
  `categoryID` varchar(36) NOT NULL,
  `name` varchar(100) NOT NULL,
  `description` text DEFAULT NULL,
  PRIMARY KEY (`categoryID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- Tạo bảng `import`
CREATE TABLE `import` (
  `importID` varchar(36) NOT NULL,
  `importDate` datetime NOT NULL,
  `supplier` varchar(36) DEFAULT NULL,
  `staff` varchar(36) DEFAULT NULL,
  `totalAmount` decimal(10,2) DEFAULT 0.00,
  `details` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL CHECK (json_valid(`details`)),
  PRIMARY KEY (`importID`),
  KEY `supplier` (`supplier`),
  KEY `staff` (`staff`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- Tạo bảng `importdetail`
CREATE TABLE `importdetail` (
  `importID` varchar(36) NOT NULL,
  `variantID` varchar(36) NOT NULL,
  `quantity` int(11) NOT NULL,
  `price` decimal(10,2) NOT NULL,
  PRIMARY KEY (`importID`, `variantID`),
  KEY `variantID` (`variantID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- Tạo bảng `order`
CREATE TABLE `order` (
  `orderID` varchar(36) NOT NULL,
  `orderDate` datetime NOT NULL,
  `staff` varchar(36) DEFAULT NULL,
  `totalAmount` decimal(10,2) DEFAULT 0.00,
  `paymentMethod` varchar(20) DEFAULT NULL,
  `details` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL CHECK (json_valid(`details`)),
  PRIMARY KEY (`orderID`),
  KEY `staff` (`staff`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- Tạo bảng `orderdetail`
CREATE TABLE `orderdetail` (
  `orderID` varchar(36) NOT NULL,
  `variantID` varchar(36) NOT NULL,
  `quantity` int(11) NOT NULL,
  `price` decimal(10,2) NOT NULL,
  PRIMARY KEY (`orderID`, `variantID`),
  KEY `variantID` (`variantID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- Tạo bảng `password_reset_log`
CREATE TABLE `password_reset_log` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `username` varchar(50) DEFAULT NULL,
  `reset_time` datetime DEFAULT NULL,
  `status` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `username` (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci AUTO_INCREMENT=2;

-- Tạo bảng `permission`
CREATE TABLE `permission` (
  `permissionID` varchar(36) NOT NULL,
  `name` varchar(50) NOT NULL,
  `description` text DEFAULT NULL,
  PRIMARY KEY (`permissionID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- Tạo bảng `product`
CREATE TABLE `product` (
  `productID` varchar(36) NOT NULL,
  `name` varchar(200) NOT NULL,
  `category` varchar(36) DEFAULT NULL,
  `stockQuantity` int(11) DEFAULT 0,
  `importPrice` decimal(10,2) DEFAULT NULL,
  `sellPrice` decimal(10,2) DEFAULT NULL,
  `brand` varchar(100) DEFAULT NULL,
  `imagePath` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`productID`),
  KEY `category` (`category`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- Tạo bảng `productvariant`
CREATE TABLE `productvariant` (
  `variantID` varchar(36) NOT NULL,
  `productID` varchar(36) DEFAULT NULL,
  `size` varchar(10) DEFAULT NULL,
  `color` varchar(50) DEFAULT NULL,
  `quantity` int(11) DEFAULT 0,
  `material` NVARCHAR(255) DEFAULT NULL,
  PRIMARY KEY (`variantID`),
  KEY `productID` (`productID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- Tạo bảng `supplier`
CREATE TABLE `supplier` (
  `supplierID` varchar(36) NOT NULL,
  `name` varchar(100) NOT NULL,
  `address` text DEFAULT NULL,
  `email` varchar(100) DEFAULT NULL,
  `phone` varchar(15) DEFAULT NULL,
  PRIMARY KEY (`supplierID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- Tạo bảng `user`
CREATE TABLE `user` (
  `userID` varchar(36) NOT NULL,
  `username` varchar(50) NOT NULL,
  `password` varchar(255) NOT NULL,
  `fullName` varchar(100) NOT NULL,
  `gender` varchar(10) DEFAULT NULL,
  `birthDate` date DEFAULT NULL,
  `phone` varchar(15) DEFAULT NULL,
  `email` varchar(100) DEFAULT NULL,
  `role` varchar(36) DEFAULT NULL,
  `status` varchar(20) DEFAULT 'active',
  PRIMARY KEY (`userID`),
  UNIQUE KEY `username` (`username`),
  KEY `role` (`role`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- Tạo bảng `userrole`
CREATE TABLE `userrole` (
  `roleID` varchar(36) NOT NULL,
  `roleName` varchar(50) NOT NULL,
  `permissions` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL CHECK (json_valid(`permissions`)),
  PRIMARY KEY (`roleID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- insert -----------------------------------------------
INSERT INTO `password_reset_log` (`id`, `username`, `reset_time`, `status`) VALUES
(1, 'admin', '2024-11-26 14:18:12', 'SUCCESS');


INSERT INTO `permission` (`permissionID`, `name`, `description`) VALUES
('PERM001', 'Quản lý người dùng', 'Quyền quản lý tài khoản người dùng'),
('PERM002', 'Quản lý sản phẩm', 'Quyền quản lý danh mục và sản phẩm'),
('PERM003', 'Quản lý đơn hàng', 'Quyền quản lý đơn hàng và bán hàng');


INSERT INTO `user` (`userID`, `username`, `password`, `fullName`, `gender`, `birthDate`, `phone`, `email`, `role`, `status`) VALUES
('USER001', 'admin', 'password', 'Quản trị viên', 'Nam', '1990-01-01', '0123456789', 'vi0978294041@gmail.com', 'ROLE001', 'active'),
('USER002', 'kho1', 'kho1', 'Nhân viên kho 1', 'Nam', '1995-02-02', '0123456788', 'kho1@gmail.com', 'ROLE002', 'active'),
('USER003', 'banhang1', 'banhang1', 'Nhân viên bán hàng 1', 'Nữ', '1998-03-03', '0123456787', 'banhang1@gmail.com', 'ROLE003', 'active');

INSERT INTO `user` (`userID`, `username`, `password`, `fullName`, `gender`, `birthDate`, `phone`, `email`, `role`, `status`) VALUES
('USER004', 'bao', '12345678', 'Quản trị viên', 'Nam', '1990-01-01', '0123456789', 'naythipha2k4@gmail.com', 'ROLE001', 'active');

INSERT INTO `userrole` (`roleID`, `roleName`, `permissions`) VALUES
('ROLE001', 'Admin', '[\"PERM001\",\"PERM002\",\"PERM003\"]'),
('ROLE002', 'Nhân viên kho', '[\"PERM002\"]'),
('ROLE003', 'Nhân viên bán hàng', '[\"PERM003\"]');


INSERT INTO Supplier VALUES
('SUP001', 'Công ty may mặc ABC', '123 Nguyễn Văn Cừ, Q.5, TP.HCM', 'abc@gmail.com', '0987654321'),
('SUP002', 'Xưởng may XYZ', '456 Lê Hồng Phong, Q.10, TP.HCM', 'xyz@gmail.com', '0987654322'),
('SUP003', 'Nhà máy dệt may DEF', '789 Lý Thường Kiệt, Q.11, TP.HCM', 'def@gmail.com', '0987654323'),
('SUP004', 'Công ty dệt may XYZ', '12 Đường Nguyễn Văn Linh, Q.7, TP.HCM', 'xyz1@gmail.com', '0987654324'),
('SUP005', 'Nhà máy giày dép ABC', '56 Đường Lê Văn Sỹ, Q.3, TP.HCM', 'abc1@gmail.com', '0987654325'),
('SUP006', 'Công ty balo T&T', '123 Đường Trần Hưng Đạo, Q.1, TP.HCM', 'ttbalo@gmail.com', '0987654326'),
('SUP007', 'Nhà cung cấp đồ thể thao', '67 Đường Cộng Hòa, Q.Tân Bình, TP.HCM', 'do-the-thao@gmail.com', '0987654327'),
('SUP008', 'Công ty phụ kiện thời trang', '89 Đường Võ Thị Sáu, Q.3, TP.HCM', 'phu-kien@gmail.com', '0987654328'),
('SUP009', 'Nhà máy sản xuất thắt lưng', '45 Đường Lý Tự Trọng, Q.1, TP.HCM', 'that-lung@gmail.com', '0987654329'),
('SUP010', 'Nhà cung cấp sản phẩm chăm sóc da', '101 Đường Phan Đình Phùng, Q. Phú Nhuận, TP.HCM', 'chamsocda@gmail.com', '0987654330');


INSERT INTO Category VALUES
('CAT001', 'Áo', 'Các loại áo'),
('CAT002', 'Quần', 'Các loại quần'),
('CAT003', 'Váy', 'Các loại váy'),
('CAT004', 'Giày', 'Các loại giày'),
('CAT005', 'Phụ kiện', 'Các loại phụ kiện thời trang'),
('CAT006', 'Đồ thể thao', 'Các loại đồ thể thao'),
('CAT007', 'Balo', 'Các loại balo'),
('CAT008', 'Đồ ngủ', 'Các loại đồ ngủ'),
('CAT009', 'Sản phẩm chăm sóc da', 'Các sản phẩm chăm sóc da'),
('CAT010', 'Thắt lưng', 'Các loại thắt lưng');


INSERT INTO Product VALUES
('PRD001', 'Áo thun nam', 'CAT001', 100, 80000, 150000, 'Nike', '/images/products/ao-thun-nam.jpg'),
('PRD002', 'Quần jean nữ', 'CAT002', 50, 150000, 300000, 'Levis', '/images/products/quan-jean-nu.jpg'),
('PRD003', 'Váy công sở', 'CAT003', 30, 200000, 400000, 'Zara', '/images/products/vay-cong-so.jpg'),
('PRD004', 'Giày thể thao nam', 'CAT004', 200, 500000, 800000, 'Nike', '/images/products/vay-cong-so.jpg'),
('PRD005', 'Giày công sở nữ', 'CAT004', 150, 700000, 1200000, 'Adidas', '/images/products/vay-cong-so.jpg'),
('PRD006', 'Balo thời trang nam', 'CAT007', 80, 300000, 600000, 'Samsonite', '/images/products/vay-cong-so.jpg'),
('PRD007', 'Balo học sinh', 'CAT007', 120, 200000, 400000, 'Targus', '/images/products/vay-cong-so.jpg'),
('PRD008', 'Đồ thể thao nam', 'CAT006', 100, 350000, 700000, 'Under Armour', '/images/products/vay-cong-so.jpg'),
('PRD009', 'Đồ thể thao nữ', 'CAT006', 90, 300000, 650000, 'Reebok', '/images/products/vay-cong-so.jpg'),
('PRD010', 'Phụ kiện thời trang', 'CAT005', 250, 150000, 400000, 'Puma', '/images/products/vay-cong-so.jpg');


INSERT INTO ProductVariant VALUES
('VAR001', 'PRD001', 'M', 'Đen', 30, 'Cotton'),
('VAR002', 'PRD001', 'L', 'Đen', 20, 'Polyester'),
('VAR003', 'PRD001', 'M', 'Trắng', 25, 'Cotton'),
('VAR004', 'PRD002', '29', 'Xanh nhạt', 15, 'Denim'),
('VAR005', 'PRD002', '30', 'Xanh nhạt', 20, 'Denim'),
('VAR006', 'PRD003', 'S', 'Đen', 10, 'Linen'),
('VAR007', 'PRD003', 'M', 'Đen', 15, 'Linen'),
('VAR008', 'PRD004', '42', 'Đen', 50, 'Da'),
('VAR009', 'PRD004', '43', 'Đen', 40, 'Da'),
('VAR010', 'PRD005', '36', 'Đỏ', 60, 'Da'),
('VAR011', 'PRD005', '37', 'Đỏ', 50, 'Da'),
('VAR012', 'PRD006', 'M', 'Đen', 100, 'Vải dù'),
('VAR013', 'PRD006', 'L', 'Đen', 50, 'Vải dù'),
('VAR014', 'PRD007', 'M', 'Xám', 80, 'Nylon'),
('VAR015', 'PRD007', 'L', 'Xám', 40, 'Nylon'),
('VAR016', 'PRD008', 'M', 'Xanh', 30, 'Vải thun'),
('VAR017', 'PRD009', 'L', 'Hồng', 25, 'Vải thun');





-- --------------------------------------------------------------------------------------------------------------------------
-- -------------------nhà cung cấp
---insert
DELIMITER $$

CREATE PROCEDURE InsertSupplier(
    IN p_supplierID VARCHAR(36),
    IN p_name VARCHAR(100),
    IN p_address TEXT,
    IN p_email VARCHAR(100),
    IN p_phone VARCHAR(15)
)
BEGIN
    -- Kiểm tra trùng lặp trước khi thêm nhà cung cấp
    IF NOT EXISTS (
        SELECT 1 
        FROM Supplier 
        WHERE supplierID = p_supplierID
    ) THEN
        INSERT INTO Supplier (supplierID, name, address, email, phone)
        VALUES (p_supplierID, p_name, p_address, p_email, p_phone);
    ELSE
        SIGNAL SQLSTATE '45000' 
        SET MESSAGE_TEXT = 'Supplier with the given ID already exists';
    END IF;
END$$

DELIMITER ;

-- Stored Procedure để lấy toàn bộ dữ liệu từ bảng NHACUNGCAP
DELIMITER $$

CREATE PROCEDURE GetAllSuppliers()
BEGIN
    SELECT 
        supplierID,
        name AS supplierName,
        address AS supplierAddress,
        email AS supplierEmail,
        phone AS supplierPhone
    FROM Supplier;
END$$

DELIMITER ;
-- -- xóa nhà cung cấp
DELIMITER $$

CREATE PROCEDURE DeleteSupplierByID(
    IN p_supplierID VARCHAR(36)
)
BEGIN
    DELETE FROM Supplier
    WHERE supplierID = p_supplierID;
END$$

DELIMITER ;

-- - cập nhật nhà cung cấp
DELIMITER $$

CREATE PROCEDURE UpdateSupplierByID(
    IN p_supplierID VARCHAR(36),
    IN p_name VARCHAR(100),
    IN p_address TEXT,
    IN p_email VARCHAR(100),
    IN p_phone VARCHAR(15)
)
BEGIN
    UPDATE Supplier
    SET 
        name = p_name,
        address = p_address,
        email = p_email,
        phone = p_phone
    WHERE supplierID = p_supplierID;
END$$

DELIMITER ;
-- -------tìm kiếm nhà cung cấp ----------------
-- tìm kiếm theo id
DELIMITER $$

CREATE PROCEDURE SearchSupplierByID(
    IN p_supplierID VARCHAR(36)
)
BEGIN
    SELECT 
        supplierID,
        name AS supplierName,
        address AS supplierAddress,
        email AS supplierEmail,
        phone AS supplierPhone
    FROM Supplier
    WHERE supplierID = p_supplierID;
END$$

DELIMITER ;

-- tìm kiếm theo tên
DELIMITER $$

CREATE PROCEDURE SearchSupplierByName(
    IN p_name VARCHAR(100)
)
BEGIN
    SELECT 
        supplierID,
        name AS supplierName,
        address AS supplierAddress,
        email AS supplierEmail,
        phone AS supplierPhone
    FROM Supplier
    WHERE name LIKE CONCAT('%', p_name, '%');
END$$

DELIMITER ;
-- - tìm kiếm theo địa chỉ
DELIMITER $$

CREATE PROCEDURE SearchSupplierByAddress(
    IN p_address TEXT
)
BEGIN
    SELECT 
        supplierID,
        name AS supplierName,
        address AS supplierAddress,
        email AS supplierEmail,
        phone AS supplierPhone
    FROM Supplier
    WHERE address LIKE CONCAT('%', p_address, '%');
END$$

DELIMITER ;
-- - tìm kiếm theo email
DELIMITER $$

CREATE PROCEDURE SearchSupplierByEmail(
    IN p_email VARCHAR(100)
)
BEGIN
    SELECT 
        supplierID,
        name AS supplierName,
        address AS supplierAddress,
        email AS supplierEmail,
        phone AS supplierPhone
    FROM Supplier
    WHERE email LIKE CONCAT('%', p_email, '%');
END$$

DELIMITER ;
-- - tìm kiếm theo sdt
DELIMITER $$

CREATE PROCEDURE SearchSupplierByPhone(
    IN p_phone VARCHAR(15)
)
BEGIN
    SELECT 
        supplierID,
        name AS supplierName,
        address AS supplierAddress,
        email AS supplierEmail,
        phone AS supplierPhone
    FROM Supplier
    WHERE phone LIKE CONCAT('%', p_phone, '%');
END$$

DELIMITER ;




-- ----------------------------danh mục -----------------------------------------------------------------
-- insert
DELIMITER $$

CREATE PROCEDURE InsertCategory(
    IN p_categoryID VARCHAR(36),
    IN p_name VARCHAR(100),
    IN p_description TEXT
)
BEGIN
    INSERT INTO Category (categoryID, name, description)
    VALUES (p_categoryID, p_name, p_description);
END$$

DELIMITER ;

-- -lấy toàn bộ dữ liệu table danh mục
DELIMITER $$

CREATE PROCEDURE GetAllCategories()
BEGIN
    SELECT 
        categoryID,
        name AS categoryName,
        description AS categoryDescription
    FROM Category;
END$$

DELIMITER ;
-- --------------------
DELIMITER $$

CREATE PROCEDURE GetAllCategoryNames()
BEGIN
   
    SELECT name FROM Category;
END $$

DELIMITER ;

-- - xóa danh mục
DELIMITER $$

CREATE PROCEDURE DeleteCategoryByID(
    IN p_categoryID VARCHAR(36)
)
BEGIN
    DELETE FROM Category
    WHERE categoryID = p_categoryID;
END$$

DELIMITER ;
-- ----cập nhật danh mục
DELIMITER $$

CREATE PROCEDURE UpdateCategoryByID(
    IN p_categoryID VARCHAR(36),
    IN p_name VARCHAR(100),
    IN p_description TEXT
)
BEGIN
    UPDATE Category
    SET 
        name = p_name,
        description = p_description
    WHERE categoryID = p_categoryID;
END$$

DELIMITER ;
-- - tìm kiếm theo tên 
DELIMITER $$

CREATE PROCEDURE SearchCategoryByName(
    IN p_name VARCHAR(100)
)
BEGIN
    SELECT 
        categoryID,
        name AS categoryName,
        description AS categoryDescription
    FROM Category
    WHERE name LIKE CONCAT('%', p_name, '%');
END$$

DELIMITER ;
-- lấy sản phẩm theo mã danh mục
DELIMITER $$

CREATE PROCEDURE GetProductsByCategory(
    IN p_categoryID VARCHAR(36)
)
BEGIN
    SELECT 
        p.name AS productName,
        p.stockQuantity,
        p.imagePath
    FROM Product p
    WHERE p.category = p_categoryID;
END$$

DELIMITER ;









-- -------------------------------------Sản Phẩm -----------------------------------------------------
-- lấy dữ liệu sản phẩm
DELIMITER $$

CREATE PROCEDURE GetProductDetails()
BEGIN
    SELECT 
        p.productID,
        p.name AS productName,
        c.name AS categoryName,
        p.stockQuantity,
        p.importPrice,
        p.sellPrice,
		p.brand
    FROM Product p
    LEFT JOIN Category c ON p.category = c.categoryID;
END$$

DELIMITER ;
-- xóa sản phẩm
DELIMITER $$

CREATE PROCEDURE DeleteProductByID(
    IN p_productID VARCHAR(36)
)
BEGIN
    DELETE FROM Product
    WHERE productID = p_productID;
END$$

DELIMITER ;
-- lấy sản phẩm theo mã
DELIMITER $$

CREATE PROCEDURE GetProductByID(IN p_productID VARCHAR(36))
BEGIN
    SELECT 
        p.name AS productName,
        c.name AS categoryName,
        p.stockQuantity,
        p.importPrice,
        p.sellPrice,
        p.brand,
        p.imagePath
    FROM 
        Product p
    LEFT JOIN 
        Category c ON p.category = c.categoryID
    WHERE 
        p.productID = p_productID;
END$$

DELIMITER ;
-- thêm sản phẩm
DELIMITER $$

CREATE PROCEDURE AddProduct(
    IN productID VARCHAR(36),
    IN productName VARCHAR(200),
    IN categoryID VARCHAR(36),
    IN stockQuantity int(11),
    IN importPrice DECIMAL(10, 2),
    IN sellPrice DECIMAL(10, 2),
    IN brand VARCHAR(100),
    IN imagePath VARCHAR(255)
    
)
BEGIN
    -- Kiểm tra xem categoryID có tồn tại hay không
    DECLARE categoryExists INT;

    -- Kiểm tra categoryID có tồn tại trong bảng Category
    SELECT COUNT(*)
    INTO categoryExists
    FROM Category
    WHERE categoryID = categoryID;

    -- Nếu categoryID không tồn tại, trả về lỗi
    IF categoryExists = 0 THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Category does not exist';
    ELSE
        -- Nếu categoryID tồn tại, thêm sản phẩm vào bảng Product
        INSERT INTO Product (
            productID, name, category,stockQuantity,importPrice, sellPrice, brand, imagePath
        )
        VALUES (
            productID, productName, categoryID, stockQuantity,importPrice, sellPrice, brand, imagePath
        );
    END IF;
END$$

DELIMITER ;





-- tìm kiếm ---------------------------------------------------------
-- tìm kếm theo id

DELIMITER $$

CREATE PROCEDURE SearchProductByID(IN searchProductID VARCHAR(36))
BEGIN
    SELECT p.productID, 
           p.name AS productName, 
           c.name AS categoryName, 
           p.stockQuantity,
           p.importPrice, 
           p.sellPrice, 
           p.brand
    FROM Product p
    LEFT JOIN Category c ON p.category = c.categoryID
    WHERE p.productID = searchProductID;
END$$


DELIMITER ;
-- theo tên
DELIMITER $$

CREATE PROCEDURE SearchProductByName(IN searchProductName VARCHAR(200))
BEGIN
    SELECT p.productID, 
           p.name AS productName, 
           c.name AS categoryName, 
		   p.stockQuantity,
           p.importPrice, 
           p.sellPrice, 
           p.brand
    FROM Product p
    LEFT JOIN Category c ON p.category = c.categoryID
    WHERE p.name LIKE CONCAT('%', searchProductName, '%');
END $$

DELIMITER ;
-- theo tên danh mục

DELIMITER $$

CREATE PROCEDURE SearchProductByCategory(IN searchCategoryName VARCHAR(100))
BEGIN
    SELECT p.productID, 
           p.name AS productName, 
           c.name AS categoryName, 
		   p.stockQuantity,
           p.importPrice, 
           p.sellPrice, 
           p.brand
    FROM Product p
    LEFT JOIN Category c ON p.category = c.categoryID
    WHERE c.name LIKE CONCAT('%', searchCategoryName, '%');
END $$

DELIMITER ;
----- theo brand
DELIMITER $$

CREATE PROCEDURE SearchProductByBrand(IN searchBrand VARCHAR(100))
BEGIN
    SELECT p.productID, 
           p.name AS productName, 
           c.name AS categoryName, 
		   p.stockQuantity,
           p.importPrice, 
           p.sellPrice, 
           p.brand
    FROM Product p
    LEFT JOIN Category c ON p.category = c.categoryID
    WHERE p.brand LIKE CONCAT('%', searchBrand, '%');
END $$

DELIMITER ;
-- ----------------- phân loại sản phẩm---------------------------------
-- lấy thông tin phân loại 
DELIMITER $$

CREATE PROCEDURE GetProductVariantsByProductID(IN inputProductID VARCHAR(36))
BEGIN
    SELECT 
		pv.variantID,
        pv.color,
        pv.size,
        pv.material
    FROM 
        ProductVariant pv
    WHERE 
        pv.productID = inputProductID;
END $$

DELIMITER ;
-- insert phân loại lúc thêm phân loại 
DELIMITER $$

CREATE PROCEDURE InsertProductVariant(
	IN inputvariantID VARCHAR(36),
    IN inputProductID VARCHAR(36),
    IN inputColor VARCHAR(50),
    IN inputSize VARCHAR(10),
    IN inputMaterial NVARCHAR(255)
)
BEGIN
    INSERT INTO ProductVariant (variantID,productID, color, size, material)
    VALUES (inputvariantID,inputProductID, inputColor, inputSize, inputMaterial);
END $$

DELIMITER ;
-- xóa phân loại sản phẩm
DELIMITER $$

CREATE PROCEDURE DeleteProductVariant(
    IN inputVariantID VARCHAR(36)
)
BEGIN
    DELETE FROM ProductVariant
    WHERE variantID = inputVariantID;
END $$

DELIMITER ;
-- sửa phân loại 
DELIMITER $$

CREATE PROCEDURE UpdateProductVariant(
    IN inputVariantID VARCHAR(36),
    IN inputColor VARCHAR(50),
    IN inputSize VARCHAR(10),
    IN inputMaterial NVARCHAR(255)
)
BEGIN
    -- Cập nhật thông tin ProductVariant
    UPDATE ProductVariant
    SET color = inputColor,
        size = inputSize,
        material = inputMaterial
    WHERE variantID = inputVariantID;
END $$

DELIMITER ;
-- ----------------------------------------------------------
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
