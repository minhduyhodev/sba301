# Slot 18: Connecting RESTful Web Service to JPA (Lab 04)

Dự án triển khai RESTful Web Service với Spring Boot 3, Spring Data JPA và Microsoft SQL Server theo tài liệu **SBA301 - Slot 18 / Chapter 13 Part B**.

---

## 1. Cấu trúc dự án
```text
Slot18/
├── pom.xml
├── README.md
└── src/
    ├── main/
    │   ├── java/com/fpt/sba301/slot18/
    │   │   ├── Slot18Application.java           # Entry point
    │   │   ├── controller/
    │   │   │   └── OrchidController.java        # REST Controller (/api/orchids)
    │   │   ├── entity/
    │   │   │   ├── Orchid.java                  # Entity Orchid (@ManyToOne sang OrchidCategory)
    │   │   │   └── OrchidCategory.java          # Entity OrchidCategory (@OneToMany, @JsonIgnore)
    │   │   ├── repository/
    │   │   │   ├── OrchidRepository.java        # JpaRepository + findByOrchidNameContainingIgnoreCase
    │   │   │   └── OrchidCategoryRepository.java# JpaRepository cho Category
    │   │   └── service/
    │   │       ├── OrchidService.java           # Service interface
    │   │       └── OrchidServiceImpl.java       # Service implementation (Constructor Injection, @Transactional)
    │   └── resources/
    │       └── application.properties           # Cấu hình SQL Server (OrchidDB_Slot18)
    └── test/
```

---

## 2. Thông số môi trường và Cơ sở dữ liệu
* **Java:** JDK 21
* **Spring Boot:** 3.4.1
* **Database:** Microsoft SQL Server
  * Tên Database: `OrchidDB_Slot18`
  * Cổng: `1433`
  * Server port: `8080`

---

## 3. Danh sách REST API Endpoints

| HTTP Method | Endpoint | Mô tả | Status Code thành công |
| :--- | :--- | :--- | :--- |
| **GET** | `/api/orchids` | Lấy danh sách tất cả hoa lan | `200 OK` |
| **GET** | `/api/orchids/{id}` | Lấy chi tiết hoa lan theo ID | `200 OK` (hoặc `404 Not Found`) |
| **GET** | `/api/orchids/search?name={name}` | Tìm kiếm hoa lan theo tên | `200 OK` |
| **POST** | `/api/orchids` | Tạo mới hoa lan | `201 Created` |
| **PUT** | `/api/orchids/{id}` | Cập nhật hoa lan theo ID | `200 OK` (hoặc `404 Not Found`) |
| **DELETE** | `/api/orchids/{id}` | Xóa hoa lan theo ID | `204 No Content` (hoặc `404 Not Found`) |

### Mẫu JSON gửi POST/PUT `/api/orchids`:
```json
{
  "orchidName": "Lan Cattleya Hoàng Gia",
  "natural": true,
  "orchidDescription": "Loài lan quý hiếm cánh hoa to",
  "attractive": true,
  "orchidUrl": "https://example.com/orchid.jpg",
  "category": {
    "id": 1
  }
}
```

---

## 4. Hướng dẫn chạy ứng dụng

1. Mở terminal tại thư mục `Slot18`:
   ```bash
   mvn spring-boot:run
   ```
2. Kiểm tra API bằng Postman hoặc trình duyệt:
   * GET all: `http://localhost:8080/api/orchids`
   * GET by ID: `http://localhost:8080/api/orchids/1`
   * Search: `http://localhost:8080/api/orchids/search?name=Hồ Điệp`
