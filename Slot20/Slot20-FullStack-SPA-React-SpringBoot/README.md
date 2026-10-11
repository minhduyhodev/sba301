# Slot 20: Full-Stack SPA Integration (React <-> Spring Boot, CORS)

Dự án tích hợp Single Page Application (React + Vite) với RESTful Web Service (Spring Boot 3 + SQL Server) theo tài liệu **SBA301 - Slot 20 / Chapter 14 (Lab 05 Baseline)**.

---

## 1. Cấu trúc dự án
```text
Slot20/
├── backend/
│   ├── src/main/java/com/fpt/sba301/slot20/
│   │   ├── config/WebConfig.java          # Cấu hình CORS toàn cục cho origin http://localhost:5173
│   │   ├── controllers/OrchidController.java # REST API endpoints (/orchids)
│   │   ├── pojos/Orchid.java              # Entity Orchid map bảng orchid trong DB
│   │   ├── repositories/IOrchidRepository.java
│   │   ├── services/IOrchidService.java
│   │   ├── services/OrchidService.java
│   │   └── Slot20Application.java
│   ├── src/main/resources/application.properties # Cấu hình SQL Server (OrchidDB_Slot20)
│   └── pom.xml
└── frontend/
    ├── src/
    │   ├── api/httpClient.js              # Axios instance với baseURL từ env
    │   ├── api/orchidApi.js               # Các hàm gọi API Orchid (GET, POST, PUT, DELETE)
    │   ├── components/ListOfOrchids.jsx   # Component hiển thị danh sách, xử lý loading/error/data
    │   ├── App.jsx
    │   └── main.jsx
    ├── .env.development                  # VITE_API_BASE_URL=http://localhost:8080
    ├── vite.config.js                    # Vite dev server cấu hình port 5173
    ├── package.json
    └── index.html
```

---

## 2. Thông số môi trường và Origins
* **Frontend Origin:** `http://localhost:5173`
* **Backend Origin:** `http://localhost:8080`
* **Cơ chế CORS:** 
  * Cấu hình tại [WebConfig.java](file:///f:/SBA301/labs/Slot20/backend/src/main/java/com/fpt/sba301/slot20/config/WebConfig.java)
  * Cho phép: `allowedOrigins("http://localhost:5173")`
  * Cho phép methods: `GET`, `POST`, `PUT`, `DELETE`, `OPTIONS`
  * Preflight max age: `3600` giây
* **Database:** SQL Server
  * Tên Database: `OrchidDB_Slot20`
  * Port: `1433`

---

## 3. Hướng dẫn chạy ứng dụng

### Bước 1: Khởi động Backend (Spring Boot)
1. Mở terminal tại thư mục `Slot20/backend`:
   ```bash
   mvn spring-boot:run
   ```
2. Backend sẽ khởi động tại `http://localhost:8080`.

### Bước 2: Khởi động Frontend (React / Vite)
1. Mở terminal tại thư mục `Slot20/frontend`:
   ```bash
   npm run dev
   ```
2. Frontend sẽ khởi động tại `http://localhost:5173`.
3. Mở trình duyệt và truy cập `http://localhost:5173`.

---

## 4. Production Migration Note
* **API URL Strategy:** Trong môi trường staging/production, thay đổi `VITE_API_BASE_URL` qua file `.env.production` trỏ tới domain API thực tế hoặc sử dụng Reverse Proxy (Nginx) để cùng origin (`/api`).
* **CORS Scope:** Trong production, không mở wildcard cho headers/endpoints, giới hạn chặt chẽ origin của frontend và kết hợp xác thực Spring Security/JWT.
