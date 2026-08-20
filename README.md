# Hệ Thống Quản Lý Nhà Hàng (Restaurant Management System)

Ứng dụng Desktop quản lý nhà hàng xây dựng bằng **Java Swing** và **MySQL**, áp dụng mô hình kiến trúc phân lớp chuẩn (Model - DAO - UI) và phân quyền vai trò (Role-based access).

---

## 🌟 Các Tính Năng Chính

### 1. Phân Quyền & Đăng Nhập
* **Quản trị viên (Admin):**
  * Theo dõi doanh thu theo ngày (số đơn hàng, tổng doanh thu).
  * Quản lý thực đơn món ăn (Thêm, Sửa, Xóa, Tìm kiếm, Top món bán chạy / doanh thu cao).
  * Quản lý danh sách bàn ăn (Thêm, Sửa số ghế, Xóa).
  * Quản lý danh sách khách hàng và lịch sử chi tiêu.
  * Quản lý nhân viên và tự động cấp tài khoản đăng nhập.
* **Thu ngân (Cashier):**
  * Xem danh sách bàn và trạng thái thanh toán.
  * Xem danh sách các hóa đơn chưa thanh toán.
  * Xem chi tiết món ăn trong đơn và thực hiện thanh toán.
  * Tích điểm/cộng dồn doanh số cho khách hàng thân thiết.
* **Phục vụ (Waiter):**
  * Mở bàn cho khách, tạo đơn gọi món (`Order`).
  * Thêm món vào đơn, xem món đã gọi, giảm/hủy món.
  * Cập nhật trạng thái phục vụ ("Chưa phục vụ", "Đã gọi món", "Đã phục vụ").
* **Bếp (Kitchen):**
  * Theo dõi danh sách bàn đang chờ nấu.
  * Xem chi tiết các món cần nấu của từng bàn.
  * Cập nhật trạng thái nấu ("Chưa nấu", "Đã nấu").

---

## 🛠️ Công Nghệ Sử Dụng

* **Ngôn ngữ:** Java (JDK 8+ / JDK 17 / JDK 21+).
* **Giao diện:** Java Swing / AWT.
* **Cơ sở dữ liệu:** MySQL (kết nối qua JDBC `mysql-connector-j`).
* **Công cụ xây dựng:** Apache NetBeans / Ant / VS Code / IntelliJ IDEA.

---

## 🚀 Hướng Dẫn Cài Đặt & Chạy Dự Án

### 1. Khởi Tạo Cơ Sở Dữ Liệu
1. Mở MySQL Workbench, phpMyAdmin hoặc Command Prompt MySQL.
2. Chạy lệnh import file `database.sql`:
   ```sql
   source /path/to/database.sql;
   ```

### 2. Cấu Hình Kết Nối Database
Mở file `src/DAO/DBConnection.java` và chỉnh sửa mật khẩu MySQL (nếu cần):
```java
private static final String USER = "root";
private static final String PASS = "mật_khẩu_mysql_của_bạn";
```

### 3. Tài Khoản Đăng Nhập Mẫu

| Vai trò | Tên đăng nhập | Mật khẩu |
| :--- | :--- | :--- |
| **Quản trị viên (Admin)** | `admin` | `admin123` |
| **Thu ngân** | `thungan` | `123456` |
| **Phục vụ** | `phucvu` | `123456` |
| **Bếp** | `bep` | `123456` |

### 4. Khởi Chạy Ứng Dụng
Mở file `src/Login/LoginForm.java` và chọn **Run File** (hoặc `Shift + F6` trong NetBeans).
