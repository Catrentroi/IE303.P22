
## Cách chạy dự án

1. Đảm bảo đã cài đặt Maven
2. Chạy file run_new.bat hoặc sử dụng lệnh sau:
   ```
   mvn spring-boot:run
   ```
3. API sẽ chạy tại địa chỉ: http://localhost:8080

## API Endpoints

- GET `/api/products` - Lấy tất cả sản phẩm
- GET `/api/products/{id}` - Lấy sản phẩm theo ID
- POST `/api/products` - Thêm sản phẩm mới
- PUT `/api/products/{id}` - Cập nhật sản phẩm theo ID
- DELETE `/api/products/{id}` - Xóa sản phẩm theo ID

