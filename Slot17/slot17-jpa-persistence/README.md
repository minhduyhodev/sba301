# SBA301 - Slot 17: Data Persistence with JPA (Chapter 13 Part A)

**Môn học:** SBA301 – Integrate Single Page Application with Spring Boot  
**Chủ đề:** Chapter 13 Part A – JPA, Entity, Repository, Service, Relationships + PA02  
**Môi trường:** JDK 21 • Spring Boot 3.3.4 • Spring Data JPA • H2 Database / Hibernate ORM  

---

## 1. Giới thiệu & Mục tiêu Slot 17
Slot 17 đánh dấu bước chuyển trọng tâm từ **REST API mô tả giao tiếp** sang **Dữ liệu bền vững (Persistence)**:
- **JPA & Hibernate:** Ánh xạ mô hình đối tượng Java (Object Graph) sang mô hình cơ sở dữ liệu quan hệ (Relational Tables, Foreign Keys, Join Tables).
- **Spring Data JPA & JpaRepository:** Cung cấp cơ chế trừu tượng hóa tầng dữ liệu (Data Access Layer), tự động sinh implementation cho CRUD và Derived Queries tại runtime.
- **Service & Transaction Boundary:** Đặt business rules và ranh giới giao dịch `@Transactional`, tận dụng cơ chế **Dirty Checking** của Persistence Context.
- **Relationship Mapping:** Thiết kế chuẩn mực quan hệ **1-N, N-1, N-N** và **Association Entity**, phân định rõ **Owning side** và **Inverse side (`mappedBy`)**.
- **DTO Boundary:** Tách biệt Entity nội bộ và API Response DTO để ngăn chặn **Infinite JSON Recursion** và rò rỉ cấu trúc database ra ngoài client.

---

## 2. Kiến trúc & Cấu trúc Dự án

```
slot17-jpa-persistence/
├── src/main/java/com/fpt/sba301/slot17/
│   ├── Slot17Application.java              # Main Spring Boot Entrypoint
│   ├── config/
│   │   └── DataInitializer.java           # CommandLineRunner seed H2 lúc khởi động
│   ├── domain/                            # JPA Persistent Entities
│   │   ├── Category.java                  # 1-N với News (Inverse side: mappedBy="category")
│   │   ├── News.java                      # N-1 Category (Owning side), N-N Tag (Owning side)
│   │   ├── Tag.java                       # N-N với News (Inverse side: mappedBy="tags")
│   │   ├── Department.java                # Bài 1-5: 1-N với Employee
│   │   ├── Employee.java                  # Bài 4-5: N-1 với Department (FK department_id)
│   │   ├── Student.java                   # Bài 7: Sinh viên
│   │   ├── Course.java                    # Bài 7: Khóa học
│   │   ├── Enrollment.java                # Bài 7: Association Entity có metadata (enrolledAt, status, finalGrade)
│   │   └── EnrollmentStatus.java          # Enum trạng thái ghi danh
│   ├── repository/                        # Spring Data JPA Repositories
│   │   ├── CategoryRepository.java        # findByNameIgnoreCase, findByActiveTrueOrderByNameAsc, existsByNameIgnoreCase
│   │   ├── NewsRepository.java            # findByCategoryId, findByTitleContainingIgnoreCase, Paging/Top10
│   │   ├── TagRepository.java             # findByNameIgnoreCase, existsByNameIgnoreCase
│   │   ├── DepartmentRepository.java      # Bài 1-3
│   │   ├── EmployeeRepository.java        # Bài 4-5
│   │   ├── StudentRepository.java         # Bài 7
│   │   ├── CourseRepository.java          # Bài 7
│   │   └── EnrollmentRepository.java      # Bài 7: Unique (student_id, course_id)
│   ├── service/                           # Business logic & Transaction boundaries
│   │   ├── CategoryService.java           # Validation, unique check, transaction control
│   │   ├── NewsService.java               # CRUD news, add tags, moveCategory (Dirty Checking demo)
│   │   ├── TagService.java                # Quản lý tag
│   │   ├── DepartmentService.java         # Bài 3 rule không cho tạo trùng tên
│   │   └── EnrollmentService.java         # Bài 7 logic ghi danh học viên
│   ├── dto/                               # DTO Boundary (ngăn recursion JSON & lộ schema)
│   │   ├── CategoryRequest.java / CategoryResponse.java
│   │   ├── NewsCreateRequest.java / NewsUpdateRequest.java
│   │   ├── NewsSummaryResponse.java       # Chứa id, title, categoryName, tagNames (DTO Boundary)
│   │   ├── NewsDetailResponse.java
│   │   └── TagRequest.java / TagResponse.java
│   ├── controller/                        # REST Controllers
│   │   ├── CategoryController.java        # /api/categories
│   │   ├── NewsController.java            # /api/news
│   │   └── TagController.java             # /api/tags
│   └── exception/                         # Global Exception Handling
│       ├── ResourceNotFoundException.java
│       ├── DuplicateResourceException.java
│       └── GlobalExceptionHandler.java
└── src/test/java/com/fpt/sba301/slot17/
    ├── repository/                        # @DataJpaTest Slice Tests
    │   ├── CategoryRepositoryTest.java
    │   └── NewsRepositoryTest.java
    ├── service/                           # Business & Dirty Checking Tests
    │   ├── CategoryServiceTest.java
    │   └── NewsServiceTest.java
    ├── domain/                            # Relationship & Association Entity Tests
    │   ├── DepartmentEmployeeTest.java    # Bài 1-5 Verification
    │   └── EnrollmentAssociationTest.java # Bài 7 Association Entity Verification
    └── controller/                        # MockMvc Integration Tests
        ├── CategoryControllerTest.java
        └── NewsControllerTest.java
```

---

## 3. Cách chạy ứng dụng & Kiểm thử

### 3.1 Chạy Unit & Integration Tests (19 tests)
```bash
mvn test
```
*Tất cả 19 test cases bao gồm `@DataJpaTest`, `@SpringBootTest`, Dirty Checking, Service validation và MockMvc Controller tests đều chạy thành công 100%.*

### 3.2 Khởi động ứng dụng
```bash
mvn spring-boot:run
```
Ứng dụng sẽ chạy tại: `http://localhost:8080`

### 3.3 Truy cập H2 Web Console
- **URL:** `http://localhost:8080/h2-console`
- **JDBC URL:** `jdbc:h2:mem:funewsdb`
- **User Name:** `sa`
- **Password:** *(để trống)*

Các bảng được Hibernate tự động sinh:
1. `CATEGORIES`
2. `NEWS` (có foreign key `category_id` tham chiếu `CATEGORIES`)
3. `TAGS`
4. `NEWS_TAGS` (Join Table chứa `news_id` và `tag_id`)
5. `DEPARTMENTS`
6. `EMPLOYEES` (có foreign key `department_id`)
7. `STUDENTS`
8. `COURSES`
9. `ENROLLMENTS` (có composite unique constraint `(student_id, course_id)`)

---

## 4. Các điểm lý thuyết & thiết kế cốt lõi (Human Verification Gate)

### 4.1 Owning Side và `mappedBy`
- **Owning side:** Là phía nắm giữ Foreign Key trong database (Ví dụ: `News` nắm cột `category_id`). Phía này được cấu hình `@ManyToOne` và `@JoinColumn(name = "category_id")`. Mọi thay đổi về quan hệ trên database phải được thực hiện thông qua owning side.
- **Inverse side:** Phía còn lại (Ví dụ: `Category.newsItems`), sử dụng `mappedBy = "category"`.
  > **Chú ý quan trọng:** `mappedBy` phải trỏ đúng tên thuộc tính Java ở owning side (`private Category category;`), **không bao giờ** điền tên cột DB (như `category_id`).

### 4.2 Dirty Checking trong Persistence Context
- Khi một Entity đang ở trạng thái **Managed** trong `@Transactional`, Hibernate tự động theo dõi trạng thái các trường của entity đó (Dirty Checking).
- Khi kết thúc transaction (commit), Hibernate tự động so sánh snapshot ban đầu và sinh câu lệnh `UPDATE` xuống database mà lập trình viên **không cần gọi `repository.save()` lần thứ 2**.
- Minh chứng trong code: phương thức `moveCategory(newsId, targetCategoryId)` trong [NewsService.java](file:///f:/SBA301/labs/Slot17/slot17-jpa-persistence/src/main/java/com/fpt/sba301/slot17/service/NewsService.java).

### 4.3 DTO Boundary & Chống Infinite JSON Recursion
- Quan hệ bidirectional (`Category` trỏ tới `News`, `News` trỏ tới `Category`) nếu trả trực tiếp Entity ra Controller sẽ khiến Jackson tuần tự hóa vô tận (`Category -> News -> Category -> News...`) dẫn tới `StackOverflowError` hoặc tải thừa dữ liệu.
- Giải pháp chuẩn: Luôn map từ Entity sang DTO chuyên biệt như `NewsSummaryResponse` và `CategoryResponse` trước khi trả về HTTP response.

### 4.4 Association Entity thay cho Many-to-Many thuần (Bài 7)
- Quan hệ Many-to-Many thuần (`News` - `Tag`) chỉ phù hợp khi bảng nối không có thuộc tính riêng.
- Khi quan hệ có dữ liệu phát sinh (như `enrolledAt`, `status`, `finalGrade`), ta chuyển đổi bảng nối thành một **Association Entity** độc lập (`Enrollment`) với hai quan hệ `@ManyToOne` tới `Student` và `Course`.
