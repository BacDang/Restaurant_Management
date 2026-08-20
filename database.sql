-- =========================================================
-- DATABASE INITIALIZATION SCRIPT FOR RESTAURANT MANAGEMENT
-- Cơ Sở Dữ Liệu: nhahang
-- =========================================================

CREATE DATABASE IF NOT EXISTS `nhahang` CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE `nhahang`;

-- 1. Bảng tài khoản người dùng (users)
DROP TABLE IF EXISTS `users`;
CREATE TABLE `users` (
  `username` VARCHAR(50) NOT NULL,
  `password` VARCHAR(100) NOT NULL,
  `role` VARCHAR(30) NOT NULL,
  PRIMARY KEY (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 2. Bảng thông tin nhân viên (employee)
DROP TABLE IF EXISTS `employee`;
CREATE TABLE `employee` (
  `employee_id` VARCHAR(50) NOT NULL,
  `employee_name` VARCHAR(100) NOT NULL,
  `employee_birth` VARCHAR(20) NOT NULL,
  `employee_role` VARCHAR(50) NOT NULL,
  `employee_sex` VARCHAR(10) NOT NULL,
  `employee_phone` VARCHAR(20) NOT NULL,
  PRIMARY KEY (`employee_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 3. Bảng quản lý bàn ăn (tables)
DROP TABLE IF EXISTS `tables`;
CREATE TABLE `tables` (
  `table_name` VARCHAR(50) NOT NULL,
  `table_seat` VARCHAR(10) NOT NULL,
  `table_status` VARCHAR(30) DEFAULT 'Trống',
  `table_cook` VARCHAR(30) DEFAULT 'Chưa nấu',
  `table_serve` VARCHAR(30) DEFAULT 'Chưa phục vụ',
  `table_pay` VARCHAR(30) DEFAULT 'Chưa thanh toán',
  `order_id` VARCHAR(50) DEFAULT NULL,
  PRIMARY KEY (`table_name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 4. Bảng thực đơn món ăn (food)
DROP TABLE IF EXISTS `food`;
CREATE TABLE `food` (
  `food_id` VARCHAR(20) NOT NULL,
  `food_name` VARCHAR(100) NOT NULL,
  `food_category` VARCHAR(50) NOT NULL,
  `food_cost` INT NOT NULL,
  PRIMARY KEY (`food_id`),
  UNIQUE KEY `uq_food_name` (`food_name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 5. Bảng đơn hàng (orders)
DROP TABLE IF EXISTS `orders`;
CREATE TABLE `orders` (
  `order_id` VARCHAR(50) NOT NULL,
  `order_date` VARCHAR(20) NOT NULL,
  `table_name` VARCHAR(50) NOT NULL,
  `payment_status` VARCHAR(30) DEFAULT 'Chưa thanh toán',
  PRIMARY KEY (`order_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 6. Bảng chi tiết đơn hàng (order_detail)
DROP TABLE IF EXISTS `order_detail`;
CREATE TABLE `order_detail` (
  `id` INT AUTO_INCREMENT,
  `order_id` VARCHAR(50) NOT NULL,
  `food_name` VARCHAR(100) NOT NULL,
  `food_quantity` INT NOT NULL DEFAULT 1,
  PRIMARY KEY (`id`),
  KEY `idx_order_id` (`order_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 7. Bảng khách hàng (customer)
DROP TABLE IF EXISTS `customer`;
CREATE TABLE `customer` (
  `cus_phone` VARCHAR(20) NOT NULL,
  `cus_name` VARCHAR(100) NOT NULL,
  `cus_amount` INT DEFAULT 0,
  `cus_orders` INT DEFAULT 0,
  PRIMARY KEY (`cus_phone`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- =========================================================
-- SAMPLE DATA (DỮ LIỆU MẪU)
-- =========================================================

-- Dữ liệu mẫu bảng users (Mật khẩu mẫu tương ứng các vai trò)
INSERT INTO `users` (`username`, `password`, `role`) VALUES
('admin', 'admin123', 'Admin'),
('thungan', '123456', 'Thu ngân'),
('phucvu', '123456', 'Phục vụ'),
('bep', '123456', 'Bếp'),
('NV01', 'NV01', 'Admin'),
('NV02', 'NV02', 'Thu ngân'),
('NV03', 'NV03', 'Phục vụ'),
('NV04', 'NV04', 'Bếp');

-- Dữ liệu mẫu bảng employee
INSERT INTO `employee` (`employee_id`, `employee_name`, `employee_birth`, `employee_role`, `employee_sex`, `employee_phone`) VALUES
('NV01', 'Nguyễn Quản Lý', '1990-05-15', 'Admin', 'Nam', '0912345678'),
('NV02', 'Trần Thu Ngân', '1998-08-20', 'Thu ngân', 'Nữ', '0987654321'),
('NV03', 'Lê Phục Vụ', '2001-11-10', 'Phục vụ', 'Nam', '0901234567'),
('NV04', 'Phạm Đầu Bếp', '1992-03-25', 'Bếp', 'Nam', '0934567890');

-- Dữ liệu mẫu bảng tables
INSERT INTO `tables` (`table_name`, `table_seat`, `table_status`, `table_cook`, `table_serve`, `table_pay`, `order_id`) VALUES
('Bàn 01', '4', 'Trống', 'Chưa nấu', 'Chưa phục vụ', 'Chưa thanh toán', NULL),
('Bàn 02', '4', 'Trống', 'Chưa nấu', 'Chưa phục vụ', 'Chưa thanh toán', NULL),
('Bàn 03', '6', 'Trống', 'Chưa nấu', 'Chưa phục vụ', 'Chưa thanh toán', NULL),
('Bàn 04', '2', 'Trống', 'Chưa nấu', 'Chưa phục vụ', 'Chưa thanh toán', NULL),
('Bàn 05', '8', 'Trống', 'Chưa nấu', 'Chưa phục vụ', 'Chưa thanh toán', NULL),
('Bàn 06', '4', 'Trống', 'Chưa nấu', 'Chưa phục vụ', 'Chưa thanh toán', NULL),
('Bàn 07', '6', 'Trống', 'Chưa nấu', 'Chưa phục vụ', 'Chưa thanh toán', NULL),
('Bàn 08', '10', 'Trống', 'Chưa nấu', 'Chưa phục vụ', 'Chưa thanh toán', NULL),
('Bàn 09', '4', 'Trống', 'Chưa nấu', 'Chưa phục vụ', 'Chưa thanh toán', NULL),
('Bàn 10', '4', 'Trống', 'Chưa nấu', 'Chưa phục vụ', 'Chưa thanh toán', NULL);

-- Dữ liệu mẫu bảng food
INSERT INTO `food` (`food_id`, `food_name`, `food_category`, `food_cost`) VALUES
('1', 'Khai vị - Salad Cá Hồi', 'Khai vị', 85000),
('2', 'Khai vị - Súp Gà Nấm', 'Khai vị', 45000),
('3', 'Khai vị - Nem Rán Hà Nội', 'Khai vị', 60000),
('4', 'Món chính - Bò Bít Tết Sốt Tiêu Đen', 'Món chính', 165000),
('5', 'Món chính - Sườn Heo Nướng BBQ', 'Món chính', 145000),
('6', 'Món chính - Lẩu Thái Hải Sản', 'Món chính', 299000),
('7', 'Món chính - Cơm Chiên Hải Sản', 'Món chính', 75000),
('8', 'Món chính - Mì Ý Sốt Bò Bằm', 'Món chính', 89000),
('9', 'Đồ uống - Bia Heineken', 'Đồ uống', 30000),
('10', 'Đồ uống - Nước Ép Cam Tươi', 'Đồ uống', 40000),
('11', 'Đồ uống - Trà Đào Cam Sả', 'Đồ uống', 35000),
('12', 'Đồ uống - Coca Cola', 'Đồ uống', 20000),
('13', 'Tráng miệng - Kem Dừa Côn Đảo', 'Tráng miệng', 35000),
('14', 'Tráng miệng - Bánh Flan Trứng Sữa', 'Tráng miệng', 25000),
('15', 'Tráng miệng - Chè Hạt Sen Long Nhãn', 'Tráng miệng', 30000);

-- Dữ liệu mẫu bảng customer
INSERT INTO `customer` (`cus_phone`, `cus_name`, `cus_amount`, `cus_orders`) VALUES
('0912111222', 'Nguyễn Văn An', 540000, 2),
('0988333444', 'Hoàng Thị Mai', 890000, 3),
('0909555666', 'Đặng Quốc Bảo', 1250000, 4);

-- Dữ liệu mẫu orders & order_detail đã hoàn thành mẫu
INSERT INTO `orders` (`order_id`, `order_date`, `table_name`, `payment_status`) VALUES
('ODR00001', '2026-08-20', 'Bàn 01', 'Đã thanh toán'),
('ODR00002', '2026-08-20', 'Bàn 02', 'Đã thanh toán');

INSERT INTO `order_detail` (`order_id`, `food_name`, `food_quantity`) VALUES
('ODR00001', 'Bò Bít Tết Sốt Tiêu Đen', 2),
('ODR00001', 'Nước Ép Cam Tươi', 2),
('ODR00002', 'Lẩu Thái Hải Sản', 1),
('ODR00002', 'Bia Heineken', 4);
