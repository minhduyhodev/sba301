# FUNewsManagementSystem - Assignment 01

**Môn học:** SBA301 – Integrate Single Page Application with Spring Boot  
**Bài làm:** Assignment 01 – Working with ReactJS application  
**Project:** `StudentName_ClassCode`

---

## 1. Giới thiệu dự án
Ứng dụng frontend quản trị cho hệ thống **FUNewsManagementSystem** xây dựng bằng **ReactJS** và **Vite**. Hệ thống mô phỏng đầy đủ luồng quản lý thông tin tin tức, danh mục và tài khoản quản trị viên với giao diện chuẩn mực, không phụ thuộc backend ngoài và duy trì tính toàn vẹn dữ liệu bằng `localStorage`.

---

## 2. Hướng dẫn cài đặt & Chạy ứng dụng

### 2.1 Yêu cầu môi trường
- Node.js (phiên bản 18+ hoặc 20+)
- npm (phiên bản 9+)

### 2.2 Các bước chạy
1. Mở terminal tại thư mục project:
   ```bash
   cd Assignment1/StudentName_ClassCode
   ```
2. Cài đặt các thư viện phụ thuộc:
   ```bash
   npm install
   ```
3. Chạy môi trường phát triển:
   ```bash
   npm run dev
   ```
   Truy cập trên trình duyệt tại: `http://127.0.0.1:5173/`

4. Chạy kiểm thử tự động (Unit Test suite):
   ```bash
   npm test
   ```

5. Đóng gói sản phẩm (Production build):
   ```bash
   npm run build
   ```

---

## 3. Tài khoản kiểm thử (Mock Authentication)
- **Username:** `Admin`
- **Password:** `Admin`
- **Quyền hạn:** Administrator (Role ID: 1)

*(Lưu ý: Hệ thống kiểm tra phân biệt chữ hoa/thường theo đúng yêu cầu đề bài. Bỏ trống hoặc nhập sai sẽ hiển thị thông báo lỗi tương ứng).*

---

## 4. Danh sách chức năng triển khai

| Mã Yêu Cầu | Chức năng | Mô tả chi tiết |
|---|---|---|
| **R01** | Cấu trúc Project | Dự án Vite + ReactJS, tuân thủ quy ước đặt tên và cấu trúc mô-đun. |
| **R02** | Login Page | Form controlled inputs, validation khi rỗng, báo lỗi khi sai tài khoản. |
| **R03** | Auth State | Lưu trữ trạng thái đăng nhập, điều hướng vào layout quản trị, nút Logout xóa phiên làm việc. |
| **R04** | AI Logo Asset | Logo tạo bằng AI lưu tại `public/assets/logo.jpg` và `src/assets/logo.jpg`, hiển thị rõ ràng trên Header và Login. |
| **R05** | Master Layout | Khung Header, Sidebar, Vùng nội dung chính (Content Area) và Footer chuẩn mực. |
| **R06** | Menu Navigation | 5 khu vực: Dashboard, Category, News, Users, Settings. Chuyển đổi trạng thái SPA không reload trang. |
| **R07** | Category CRUD + Search | Xem danh sách, tìm kiếm theo tên, thêm mới/sửa qua popup dialog, xác nhận trước khi xóa, kiểm tra ràng buộc với tin tức. |
| **R08** | News CRUD + Search | Xem danh sách, tìm kiếm theo tiêu đề, liên kết danh mục, tác giả, thêm/sửa qua popup dialog, xác nhận xóa. |
| **R09** | User CRUD + Search | Quản lý tài khoản (Admin/Staff), tìm kiếm username, thêm/sửa qua dialog, bảo vệ tài khoản Admin mặc định không bị xóa. |
| **R10** | Popup Dialog | Modal Dialog dùng chung cơ chế cho cả Create (form rỗng) và Update (prefill dữ liệu). |
| **R11** | Delete Confirmation | Hộp thoại xác nhận thao tác xóa: Cancel giữ nguyên dữ liệu, Confirm mới thực hiện xóa. |
| **R12** | Data Layer & Persistence | Tách biệt tầng dữ liệu (`seed.js`, `newsService.js`), lưu trữ qua `localStorage` (`funews.assignment01.data.v1`), cập nhật bất biến (immutable state). |

---

## 5. Bảng Kiểm Thử Thực Tế (Test Matrix - Mục 5.1)

| # | Nhóm | Case | Thao tác | Kết quả mong đợi | Trạng thái |
|---|---|---|---|---|---|
| 1 | Login | Empty | Bấm Sign In khi để trống username/password | Không login; hiển thị lỗi "Username and password are required." | **PASS** |
| 2 | Login | Wrong credential | Nhập username hoặc password không đúng | Không vào admin; hiển thị "Incorrect username or password." | **PASS** |
| 3 | Login | Valid Admin | Nhập Admin / Admin | Đăng nhập thành công, vào Admin Dashboard | **PASS** |
| 4 | Auth | Logout | Bấm Logout tại Header | Trở về Login page; admin không thể truy cập khi chưa login lại | **PASS** |
| 5 | Layout | Menu completeness | Quan sát thanh Sidebar | Có đủ 5 mục: Dashboard, Category, News, Users, Settings | **PASS** |
| 6 | Navigation | All menus | Bấm lần lượt từng menu | Hiển thị đúng nội dung từng trang; không full reload | **PASS** |
| 7 | Category | Read | Mở Category Management | Danh sách hiển thị đúng ID, Tên, Trạng thái, Thao tác | **PASS** |
| 8 | Category | Create valid | Bấm Add New, nhập tên hợp lệ, chọn status, bấm Save | Record mới xuất hiện trên bảng, dialog đóng | **PASS** |
| 9 | Category | Create invalid | Để trống Category Name, bấm Save | Form không lưu, hiển thị lỗi "Category name is required." | **PASS** |
| 10 | Category | Update | Bấm Edit, đổi tên, bấm Save | Đúng record được cập nhật, dữ liệu mới hiển thị ngay | **PASS** |
| 11 | Category | Delete cancel | Bấm Delete -> bấm Cancel trên hộp thoại | Record vẫn còn nguyên vẹn | **PASS** |
| 12 | Category | Delete confirm | Bấm Delete -> bấm Confirm Delete | Record được xóa khỏi danh sách | **PASS** |
| 13 | Category | Search hit | Nhập từ khóa tồn tại (ví dụ: "University") | Danh sách lọc chính xác record | **PASS** |
| 14 | Category | Search miss | Nhập từ khóa không tồn tại (ví dụ: "Science") | Hiển thị thông báo không tìm thấy kết quả và nút Reset Filter | **PASS** |
| 15 | News | Read | Mở News Management | Danh sách hiển thị bài viết cùng danh mục và tác giả | **PASS** |
| 16 | News | Create valid | Bấm Add New Article, điền title, content, chọn category, Save | Bài viết mới hiển thị trên bảng | **PASS** |
| 17 | News | Create invalid | Để trống Title hoặc Content, bấm Save | Không thêm; hiển thị thông báo validation | **PASS** |
| 18 | News | Update | Bấm Edit, sửa nội dung, bấm Save | Đúng bài viết được cập nhật, giữ nguyên tác giả ban đầu | **PASS** |
| 19 | News | Delete cancel | Bấm Delete -> Cancel | Bài viết giữ nguyên | **PASS** |
| 20 | News | Delete confirm | Bấm Delete -> Confirm Delete | Bài viết bị xóa khỏi danh sách | **PASS** |
| 21 | News | Search hit | Nhập từ khóa tiêu đề có sẵn | Danh sách lọc đúng bài viết | **PASS** |
| 22 | News | Search miss | Nhập từ khóa tiêu đề không khớp | Hiển thị No-result state | **PASS** |
| 23 | Users | Read | Mở Users Management | Hiển thị danh sách các tài khoản với role Admin/Staff | **PASS** |
| 24 | Users | Create valid | Bấm Add New User, nhập username, password, chọn role, Save | Tài khoản mới xuất hiện | **PASS** |
| 25 | Users | Create invalid | Bỏ trống username hoặc trùng username có sẵn | Không thêm; hiển thị thông báo lỗi | **PASS** |
| 26 | Users | Update | Bấm Edit, đổi role/status, để trống password để giữ mật khẩu cũ | Đúng tài khoản được cập nhật | **PASS** |
| 27 | Users | Delete cancel | Bấm Delete -> Cancel | Tài khoản được giữ nguyên | **PASS** |
| 28 | Users | Delete confirm | Bấm Delete trên user phụ -> Confirm | Tài khoản bị xóa khỏi hệ thống | **PASS** |
| 29 | Users | Search hit | Nhập keyword username tồn tại | Danh sách lọc chính xác user | **PASS** |
| 30 | Users | Search miss | Nhập keyword không tồn tại | Hiển thị No-result state | **PASS** |
| 31 | News | Relation | Thêm/sửa news với dropdown Category | Chỉ cho phép chọn Category hợp lệ trong hệ thống | **PASS** |
| 32 | User | Role | Cột Role hiển thị nhãn thân thiện | Hiển thị rõ Admin / Staff thay vì số 1 / 2 | **PASS** |
| 33 | Status | Display | Cột Status hiển thị nhãn thân thiện | Hiển thị rõ Active / Inactive thay vì số 1 / 0 | **PASS** |
| 34 | Data | Empty state | Khi danh sách chưa có dữ liệu | Giao diện hiển thị thông báo rỗng rõ ràng, không bị crash | **PASS** |
| 35 | Persistence | Reload | Tải lại trang (F5) sau khi CRUD | Dữ liệu được khôi phục nguyên vẹn từ localStorage | **PASS** |

---

## 6. Nhật ký sử dụng AI (AI Usage Log - Mục 9.4)

| # | Prompt | Mục đích | Gợi ý AI | Kiểm chứng | Phần đã chỉnh sửa |
|---|---|---|---|---|---|
| 1 | Tạo logo AI cho FUNewsManagementSystem phong cách tối giản | Tạo asset logo theo yêu cầu R04 | Hình ảnh logo FU News với tone xanh - trắng | Kiểm tra độ phân giải, lưu vào `public/assets/logo.jpg` và `src/assets/logo.jpg` | Đưa vào Header và Login page |
| 2 | Thiết kế bố cục giao diện quản trị theo hướng dẫn chuẩn | Xây dựng Admin Layout và các Page CRUD | Mã nguồn CSS và Component React tách theo cấu trúc đề xuất | Chạy `npm run build` và kiểm thử toàn bộ qua trình duyệt | Tinh chỉnh giao diện không dùng icon, giữ phong cách tiêu chuẩn |

---

## 7. Các câu hỏi phỏng vấn chuẩn bị trước (Interview Readiness)
1. **Lệnh tạo & chạy:** `npm run dev` chạy Vite dev server, `npm test` chạy Node.js native test runner kiểm tra service logic.
2. **Quản lý trạng thái:** `currentUser` quản lý phiên đăng nhập ở component gốc `App`; dữ liệu `data` đồng bộ cùng `localStorage`.
3. **Tính bất biến (Immutable Updates):** Tất cả thao tác lưu/xóa trong `newsService.js` đều dùng cú pháp sao chép `[...array]`, `.map()`, `.filter()` để đảm bảo React phát hiện thay đổi và re-render đúng đắn mà không làm thay đổi trực tiếp mảng gốc.
4. **Bảo toàn dữ liệu nguồn khi tìm kiếm:** Dữ liệu nguồn `categories`, `news`, `users` luôn được giữ nguyên; danh sách hiển thị `displayedCategories` được tính toán thông qua hàm `searchRecords`.
