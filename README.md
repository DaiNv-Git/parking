# Đồ án Quản lý Bãi đỗ xe (Parking Management System)

Đây là hệ thống quản lý bãi đỗ xe với giao diện thân thiện, sử dụng Java Spring Boot và cơ sở dữ liệu (tương thích H2 / MySQL). 

Hệ thống cung cấp các chức năng chính:
- **Trang chủ (Quản lý bãi đỗ xe):** Ghi nhận xe vào/ra bằng thẻ, đếm số xe đang đỗ.
- **Quản lý Sinh Viên:** Thêm, xem, xóa sinh viên. 
- **Quản lý Thẻ:** Tạo thẻ gửi xe, cấp phát cho sinh viên, khóa và mở khóa thẻ.
- **Báo cáo Thống kê:** Theo dõi số lượng vé xe, doanh thu và dữ liệu xe ra vào.

## Cách cài đặt & Khởi chạy

Chạy ứng dụng bằng lệnh:
```bash
mvn spring-boot:run
```
Sau đó truy cập trình duyệt tại địa chỉ: http://localhost:8080/
