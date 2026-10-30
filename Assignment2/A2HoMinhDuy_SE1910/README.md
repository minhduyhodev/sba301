# Assignment 02 - FUNewsManagementSystem

> **Môn học:** SBA301 - Server-side Web Development  
> **Sinh viên:** Hồ Minh Duy  
> **MSSV:** CE190378  
> **Lớp:** SE1910  
> **Tên Database & Project:** `A2HoMinhDuy_SE1910`

---

## 1. Giới thiệu tổng quan

Hệ thống **FUNewsManagementSystem** là ứng dụng quản lý và xuất bản tin tức đại học, đáp ứng đầy đủ yêu cầu trong tài liệu **Assignment 02**:
- **Backend:** Spring Boot 3, Spring Web MVC (RESTful API), Spring Data JPA, kết nối hệ quản trị cơ sở dữ liệu **Microsoft SQL Server**.
- **Kiến trúc:** 3 lớp chuẩn (`Controller` → `Service` → `Repository` → `Database`).
- **Frontend:** ReactJS (Vite, React 18, Axios) giao tiếp RESTful API thời gian thực.
- **Tuân thủ quy định:**
  - Khách vãng lai xem được các bài viết có `NewsStatus = 1` (Active) mà không cần đăng nhập.
  - Phân quyền nghiêm ngặt giữa **Admin (Role = 1)** và **Staff (Role = 2)**.
  - Thao tác Thêm/Sửa bắt buộc dùng **Popup Dialog (Modal)**.
  - Thao tác Xóa bắt buộc qua **Confirmation Dialog**.
  - Kiểm tra ràng buộc nghiệp vụ ở Backend và hiển thị thông báo rõ ràng trên Frontend:
    - **Không cho xóa tài khoản** nếu tài khoản đó đã tạo bài viết (`CreatedByID`).
    - **Không cho xóa danh mục** nếu danh mục đó đang được bài viết sử dụng (`CategoryID`).

---

## 2. Thông tin môi trường & Mẫu đặt tên

| Thành phần | Cấu hình thực tế |
| :--- | :--- |
| **Hệ điều hành** | Windows 11 |
| **Java SDK** | Oracle JDK 21.0.8 LTS |
| **Build Tool** | Apache Maven 3.9+ |
| **Node.js & npm** | Node v22.13.0, npm 10.9.2 |
| **SQL Server** | Microsoft SQL Server 2022 Developer Edition |
| **Host / Port** | `localhost:1433` (TCP connection verified) |
| **SQL Auth** | Username: `sa` / Password: `123456` |
| **Tên Database** | `A2HoMinhDuy_SE1910` |
| **Tên Backend Artifact** | `A2HoMinhDuy_SE1910` |
| **Tên Frontend App** | `a2hominhduy_se1910` |

---

## 3. Cấu trúc thư mục Assignment2

```text
Assignment2/
├── backend/                               # Spring Boot 3 REST API Project (A2HoMinhDuy_SE1910)
│   ├── pom.xml                            # Maven dependencies (Web, JPA, Validation, MSSQL Driver, Test)
│   └── src/
│       ├── main/
│       │   ├── java/fu/edu/sba301/assignment2/
│       │   │   ├── A2Application.java     # Main Entry Point
│       │   │   ├── config/                # CorsConfig, DataInitializer (seed data)
│       │   │   ├── controller/            # REST Controllers (Auth, Account, Category, News, Tag, Profile)
│       │   │   ├── domain/                # JPA Entities (SystemAccount, Category, NewsArticle, Tag)
│       │   │   ├── dto/                   # Request & Response DTOs, Bean Validations
│       │   │   ├── exception/             # ResourceNotFoundException, BadRequestException, GlobalExceptionHandler
│       │   │   ├── repository/            # Spring Data JPA Repositories
│       │   │   └── service/               # Business Services & Constraint Checks
│       │   └── resources/
│       │       └── application.properties # Cấu hình cổng 8080 & SQL Server datasource
│       └── test/
│           └── java/fu/edu/sba301/assignment2/
│               └── Assignment2IntegrationTest.java # 5 bộ integration test tự động
│
└── frontend/                              # ReactJS Client (Vite + Axios)
    ├── package.json
    ├── vite.config.js                     # Proxy API /api -> http://localhost:8080
    ├── index.html
    └── src/
        ├── api/                           # Axios Client & API modules (authApi, accountApi, categoryApi, newsApi, tagApi, profileApi)
        ├── components/                    # Navbar, Modal (Popup Dialog), ConfirmDialog
        ├── context/                       # AuthContext (quản lý session/role localStorage)
        ├── pages/                         # PublicNewsPage, LoginPage, AdminAccountsPage, StaffCategoriesPage, StaffNewsPage, StaffHistoryPage, StaffProfilePage
        ├── App.jsx                        # Layout & Router điều hướng phân quyền
        ├── main.jsx                       # React DOM Root
        └── index.css                      # Design system hiện đại, responsive
```

---

## 4. Thiết kế Database & Quyết định kỹ thuật

Dựa trên sơ đồ Trang 3 của PDF:

1. **`SystemAccount`**:
   - `AccountID`: Khóa chính (Integer IDENTITY).
   - `AccountName`: NVARCHAR(100).
   - `AccountEmail`: NVARCHAR(100) UNIQUE.
   - `AccountRole`: INT (1 = Admin, 2 = Staff).
   - `AccountPassword`: NVARCHAR(100).
2. **`Category`**:
   - `CategoryID`: Khóa chính (Integer IDENTITY).
   - `CategoryName`: NVARCHAR(100).
   - `CategoryDesciption`: NVARCHAR(500) *(giữ đúng tên cột theo đề bài)*.
   - `ParentCategoryID`: INT (Khóa ngoại trỏ về Category, hỗ trợ cấp cha-con).
   - `IsActive`: BIT (1 = Active, 0 = Inactive).
3. **`Tag`**:
   - `TagID`: Khóa chính (Integer IDENTITY).
   - `TagName`: NVARCHAR(100) UNIQUE.
   - `Note`: NVARCHAR(255).
4. **`NewsArticle`**:
   - `NewsArticleID`: Khóa chính (NVARCHAR(50), hỗ trợ mã linh hoạt như `NA0001` hoặc mã số).
   - `NewsTitle`: NVARCHAR(255).
   - `Headline`: NVARCHAR(255).
   - `CreatedDate`: DATETIME2.
   - `NewsContent`: NVARCHAR(4000).
   - `NewsSource`: NVARCHAR(100).
   - `CategoryID`: Khóa ngoại trỏ về `Category`.
   - `NewsStatus`: BIT (1 = Active, 0 = Inactive).
   - `CreatedByID`: Khóa ngoại trỏ về `SystemAccount`.
   - `UpdatedByID`: INT (Nullable).
   - `ModifiedDate`: DATETIME2 (Nullable).
5. **`NewsTag` (Bảng trung gian)**:
   - `NewsArticleID`: Khóa ngoại trỏ về `NewsArticle`.
   - `TagID`: Khóa ngoại trỏ về `Tag`.
   - Khóa chính tổng hợp `(NewsArticleID, TagID)`.

### Điểm mâu thuẫn Trang 3 & Quyết định kỹ thuật:
- **Mâu thuẫn:** Văn bản Trang 3 ghi: *"one tag will belong to zero or one news article"* (quan hệ 1-N), nhưng sơ đồ ERD ngay bên trên lại thiết kế bảng trung gian `NewsTag(NewsArticleID, TagID)` (đặc trưng của quan hệ Nhiều - Nhiều N-N).
- **Quyết định đã thống nhất:** Triển khai **quan hệ Nhiều - Nhiều (N-N)** chuẩn thông qua bảng `NewsTag` và `@ManyToMany` trong JPA (tương tự như bài thực hành Slot 17). Một bài viết có nhiều tag và một tag có thể được tái sử dụng cho nhiều bài viết.

---

## 5. Hướng dẫn khởi chạy

### Bước 1: Chuẩn bị cơ sở dữ liệu SQL Server
Đảm bảo SQL Server đang chạy tại `localhost:1433`. Khởi tạo cơ sở dữ liệu `A2HoMinhDuy_SE1910`:
```sql
IF NOT EXISTS (SELECT * FROM sys.databases WHERE name = 'A2HoMinhDuy_SE1910')
BEGIN
    CREATE DATABASE [A2HoMinhDuy_SE1910];
END
```
*(Hibernate với `ddl-auto=update` và `DataInitializer` sẽ tự động tạo bảng và nạp dữ liệu mẫu ban đầu khi backend khởi động).*

### Bước 2: Chạy Backend (Spring Boot 3)
```powershell
cd f:\SBA301\labs\Assignment2\backend
mvn clean spring-boot:run
```
Backend sẽ khởi chạy tại: **`http://localhost:8080`**.

Để chạy kiểm thử tự động (Unit/Integration Tests):
```powershell
cd f:\SBA301\labs\Assignment2\backend
mvn test
```

### Bước 3: Chạy Frontend (ReactJS)
Mở một cửa sổ dòng lệnh khác:
```powershell
cd f:\SBA301\labs\Assignment2\frontend
npm install
npm run dev
```
Frontend sẽ mở tại: **`http://localhost:5173`** (hoặc `http://localhost:5174`).

---

## 6. Tài khoản Demo & Kịch bản kiểm thử thực tế

Dữ liệu mẫu đã được nạp sẵn qua `DataInitializer`:

| Tài khoản | Email | Mật khẩu | Quyền hạn | Mục đích kiểm thử |
| :--- | :--- | :--- | :--- | :--- |
| **Admin** | `admin@fpt.edu.vn` | `admin123` | **Admin (Role = 1)** | Quản lý SystemAccount (CRUD, Search, kiểm tra ràng buộc xóa) |
| **Staff chính** | `staff@fpt.edu.vn` | `staff123` | **Staff (Role = 2)** | Đã tạo bài viết `NA0001`, `NA0002`, `NA0003`. Dùng kiểm tra chặn xóa tài khoản và lịch sử bài viết |
| **Staff chưa có bài** | `staff_idle@fpt.edu.vn` | `staff123` | **Staff (Role = 2)** | Chưa tạo bài viết nào. Dùng kiểm tra xóa tài khoản **thành công** |

### Kịch bản kiểm thử:
1. **Khách vãng lai (Chưa đăng nhập):**
   - Truy cập trang chủ `Public News`.
   - Xem 2 bài viết Active (`NA0001`, `NA0002`). Bài viết Inactive/Draft (`NA0003`) **không hiển thị**.
   - Thử tìm kiếm theo từ khóa hoặc lọc theo Category.
   - Nhấn "Read Article →" mở popup modal xem chi tiết bài viết, tags và tác giả.
2. **Đăng nhập với vai trò Admin:**
   - Đăng nhập bằng `admin@fpt.edu.vn` / `admin123` (hoặc bấm nút nhanh "👑 Fill Admin").
   - Nhấn "➕ Add New Account" để mở Popup Modal thêm tài khoản mới.
   - Thử nhấn nút **Delete** tài khoản `Ho Minh Duy` (`staff@fpt.edu.vn`) → Xác nhận trong Confirm Dialog → Hệ thống hiển thị thông báo lỗi màu đỏ:
     > `⚠️ Cannot delete account: Cannot delete account because it has already created news articles.`
   - Thử nhấn **Delete** tài khoản `Test Staff (No Articles)` → Xóa thành công do chưa tạo bài viết nào.
3. **Đăng nhập với vai trò Staff:**
   - Đăng nhập bằng `staff@fpt.edu.vn` / `staff123` (hoặc bấm nút nhanh "✍️ Fill Staff").
   - Vào **Categories**:
     - Thêm/sửa danh mục qua Popup Modal.
     - Nhấn nút **Delete** danh mục `Technology & Engineering` (đang được bài `NA0001` sử dụng) → Xác nhận → Bị chặn:
       > `⚠️ Cannot delete category: Cannot delete category because it is currently used by news articles.`
     - Nhấn nút **Delete** danh mục `Archived Notices` (danh mục trống) → Xóa thành công.
   - Vào **News Articles**:
     - Nhấn "➕ Create News Article" mở Popup Modal.
     - Điền Tiêu đề, Headline, Nội dung, chọn Category, bật/tắt Active, chọn/thêm Tags.
     - Lưu bài viết và xem cập nhật tức thì trên bảng.
   - Vào **My News History**:
     - Xem danh sách toàn bộ các bài viết do tài khoản hiện tại tạo (kèm bài Active và Draft).
   - Vào **Profile**:
     - Xem thông tin tài khoản, cập nhật Họ Tên và Mật khẩu mới.

---

## 7. Giải thích luồng End-to-End (Chuẩn bị cho Interview - Tiêu chí C4)

Dưới đây là phân tích chi tiết luồng xử lý **"Tạo bài viết tin tức mới (Create News Article)"** xuyên suốt qua tất cả các tầng:

```
[1. User Action]
       │
       ▼
[2. React Component: StaffNewsPage.jsx]
       │ (State: formData, tagIds)
       ▼
[3. Axios Client: newsApi.create() / axiosClient.js]
       │ (HTTP POST /api/news, Headers: X-User-Id, X-User-Role)
       ▼
[4. Controller: NewsArticleController.java]
       │ (@Valid @RequestBody NewsArticleRequest, checkStaffRole())
       ▼
[5. Service: NewsArticleService.java]
       │ (Business Logic: Kiểm tra Category, Creator, gán Tags)
       ▼
[6. Repository: NewsArticleRepository.java]
       │ (Spring Data JPA / Hibernate ORM)
       ▼
[7. SQL Server Database: Tables NewsArticle & NewsTag]
       │ (INSERT INTO NewsArticle; INSERT INTO NewsTag)
       ▼
[8. Response DTO: NewsArticleResponse]
       │ (HTTP 201 Created JSON)
       ▼
[9. React State Update & Re-render]
       │ (setIsModalOpen(false), setArticles([...prev, newArticle]))
```

### Chi tiết từng bước:
1. **Trigger / User Action:** Staff mở modal "Create News Article", nhập tiêu đề, nội dung, chọn chuyên mục, chọn tags và bấm **"Create Article"**.
2. **React State (`StaffNewsPage.jsx`):** Sự kiện `onSubmit` thu thập dữ liệu từ form state (`formData` và `tagIds`), đóng gói payload.
3. **Axios Client (`api/newsApi.js` & `api/axiosClient.js`):**
   - Interceptor tự động đính kèm thông tin phiên làm việc từ `localStorage`: `X-User-Id: 2`, `X-User-Role: 2`.
   - Gửi yêu cầu `POST http://localhost:8080/api/news` với body JSON.
4. **Controller Layer (`NewsArticleController.java`):**
   - `@PostMapping`: Bắt request, phương thức `checkStaffRole(userRole)` kiểm tra quyền (yêu cầu Role = 2).
   - `@Valid`: Kiểm tra tính hợp lệ của dữ liệu đầu vào (tiêu đề không để trống, độ dài không vượt quá giới hạn). Nếu vi phạm, `GlobalExceptionHandler` trả về lỗi HTTP 400.
5. **Service Layer (`NewsArticleService.java`):**
   - Kiểm tra `Category` tồn tại thông qua `categoryRepository.findById()`.
   - Lấy thông tin tài khoản tạo bài qua `accountRepository.findById(currentUserId)`.
   - Lấy danh sách các Entity `Tag` từ `tagRepository.findAllById(tagIds)`.
   - Thiết lập `createdDate = LocalDateTime.now()`, gắn tập hợp tags vào entity `NewsArticle`.
6. **Repository Layer & Hibernate (`NewsArticleRepository.java`):**
   - Gọi `newsArticleRepository.save(article)`.
   - Hibernate tự sinh các câu lệnh SQL:
     - `INSERT INTO news_article (news_articleid, news_title, categoryid, created_byid, ...) VALUES (...)`
     - `INSERT INTO news_tag (news_articleid, tagid) VALUES (?, ?)`
7. **SQL Server Database:**
   - Thực thi các câu lệnh INSERT, kiểm tra khóa ngoại và lưu trữ dữ liệu vào đĩa cứng.
8. **Response Return:**
   - Service chuyển đổi Entity thành `NewsArticleResponse` (DTO) để che giấu mật khẩu và tránh lỗi vòng lặp tuần tự hóa (cyclic serialization).
   - Controller trả về `ResponseEntity.status(HttpStatus.CREATED).body(response)`.
9. **UI Update:**
   - React nhận response thành công (`res.status === 201`), đóng Popup Modal (`setIsModalOpen(false)`), hiển thị thông báo alert xanh, và kích hoạt `fetchArticles()` để bảng dữ liệu cập nhật bài viết mới ngay lập tức mà không cần reload trang.

---

## 8. Giới hạn còn lại & Hướng phát triển

- Hiện tại hệ thống sử dụng cơ chế truyền `X-User-Role` và `X-User-Id` trên Header HTTP kết hợp lưu trữ `currentUser` ở frontend để mã nguồn tinh giản, trực quan, phù hợp với tiêu chuẩn đánh giá đồ án cá nhân SBA301. Trong các môi trường sản xuất (Production), có thể nâng cấp lên JSON Web Token (JWT) có chữ ký số mã hóa và Redis session store.
- Tính năng phân trang (Pagination) có thể được bổ sung thông qua `Pageable` của Spring Data JPA khi số lượng bài viết lên tới hàng nghìn bài.
