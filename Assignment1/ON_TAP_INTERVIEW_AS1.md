# Ôn phỏng vấn Assignment 01 từ code hiện tại

Bản ôn này dựa trên mục 7 (trang 14–15), phụ lục interview (trang 19–20) của tài liệu hướng dẫn và mã nguồn trong `F:\SBA301\labs\Assignment1\StudentName_ClassCode`. Đây là cách giải thích để luyện hiểu; câu hỏi thực tế do giảng viên quyết định. Các câu “em…” bên dưới là mẫu diễn đạt về hành vi code, không phải lời khẳng định ai đã viết hoặc kiểm thử nó.

**1. Trước tiên: bài này làm gì?**

Bạn đang làm giao diện quản trị tin tức FUNewsManagementSystem bằng React và Vite. Người quản trị đăng nhập rồi quản lý ba loại dữ liệu: Category (danh mục), News (bài viết), Users (tài khoản). CRUD nghĩa là Create, Read, Update, Delete: thêm, xem, sửa, xóa. Search là tìm kiếm.

Bạn có thể nói:

> Bài của em là frontend quản trị tin tức bằng React và Vite. Có đăng nhập Admin/Admin, các mục Dashboard, Category, News, Users, Settings. Ba phần Category, News, Users có CRUD và tìm kiếm. Thêm/sửa dùng popup, xóa có xác nhận. Dữ liệu được xử lý trong frontend và lưu bằng localStorage.

Bản code này chưa gọi Spring Boot/API/database, đăng nhập đang kiểm tra cố định Admin/Admin. Role Staff có trong dữ liệu nhưng chưa đăng nhập/phân quyền theo từng tài khoản. F5 mất phiên đăng nhập; dữ liệu CRUD đã lưu vẫn còn nếu localStorage chưa bị xóa và đọc được.

**2. Mục 7 yêu cầu nói như thế nào?**

Khung 5 ý của tài liệu có thể nhớ thành:

**Làm gì → nhận gì → khi nào chạy → xử lý gì → kết quả gì.**

Ví dụ giảng viên mở `CategoryDialog` và hỏi “giải thích component này”:

> Component này là form thêm hoặc sửa danh mục. Nó nhận isOpen, mode, initialData, onSave, onClose từ trang quản lý. Khi popup mở hoặc bản ghi được chọn thay đổi, useEffect đưa dữ liệu cũ vào form nếu sửa, hoặc xóa form nếu thêm. Khi submit, nó chặn thao tác submit mặc định, kiểm tra tên không được rỗng rồi gọi onSave. Nếu lưu thành công, luồng xử lý ở trang cha đóng popup; nếu lỗi, form hiển thị thông báo.

Đọc lại rồi tự mở code và chỉ từng ý. Không cần nhớ nguyên văn.

**3. Mỗi file có trách nhiệm gì?**

| File/thư mục | Giải thích đơn giản | Chỗ bắt đầu đọc |
|---|---|---|
| `src/main.jsx` | Đưa component App vào phần tử root của trang HTML | Dòng 6 |
| `src/App.jsx` | Giữ dữ liệu chung, phiên đăng nhập, menu đang chọn và hàm lưu/xóa | Dòng 13 |
| `src/pages/LoginPage.jsx` | Form đăng nhập | Dòng 4 |
| `src/pages/CategoryManagement.jsx` | Bảng Category, tìm kiếm, điều khiển popup | Dòng 6 |
| `src/pages/NewsManagement.jsx` | Bảng News, tìm kiếm và thao tác bài viết | Dòng 6 |
| `src/pages/UserManagement.jsx` | Bảng Users và thao tác tài khoản | Dòng 6 |
| `src/components/CategoryDialog.jsx` | Form thêm/sửa Category | Dòng 4 |
| `src/components/Modal.jsx` | Khung popup dùng lại được | Dòng 3 |
| `src/components/ConfirmDialog.jsx` | Nội dung và nút xác nhận xóa, đặt trong Modal | Dòng 4 |
| `src/components/Header.jsx`, `Sidebar.jsx` | Phần đầu trang và menu | Sidebar dòng 3 |
| `src/data/seed.js` | Dữ liệu mẫu ban đầu | Hàm createSeedData |
| `src/services/newsService.js` | Kiểm tra dữ liệu, thêm/sửa/xóa, tìm kiếm, đọc/ghi storage | Dòng 18 trở đi |

Tên file phía trên đều thuộc thư mục project đã ghi đầu tài liệu. Khi luyện, dùng tìm kiếm tên hàm trong VS Code để đến đúng chỗ.

**4. Component, JSX, props, state là gì?**

- **Component:** một phần giao diện có thể ghép lại hoặc dùng lại. Ví dụ LoginPage, Header, CategoryDialog. Trong code này chúng được viết bằng hàm trả về JSX.
- **JSX:** cú pháp viết giao diện trong JavaScript, nhìn gần giống HTML. Ví dụ `<LoginPage onLoginSuccess={handleLoginSuccess} />`. Dấu `{}` cho phép đưa biểu thức JavaScript vào giao diện.
- **Props:** giá trị hoặc hàm cha truyền xuống con. App truyền `categories` và `onSaveRecord` xuống CategoryManagement. Component con đọc props và gọi callback khi cần cha xử lý.
- **State:** dữ liệu component ghi nhớ giữa các lần render và có thể cập nhật qua setter. Ví dụ `searchTerm`, `dialogState`, `currentUser`.

Ví dụ thật ở CategoryManagement dòng 6–8:

```jsx
export default function CategoryManagement({ categories = [], onSaveRecord, onDeleteRecord }) {
  const [searchTerm, setSearchTerm] = useState('');
  const [dialogState, setDialogState] = useState({ isOpen: false, mode: 'create', item: null });
```

Mẫu trả lời:

> categories là props do App truyền vào. searchTerm là state riêng của trang quản lý. Khi người dùng gõ, trang gọi setSearchTerm để cập nhật giá trị tìm kiếm.

**5. Re-render là gì, tại sao giao diện đổi?**

Render là React tính ra giao diện từ props và state hiện tại. Khi setter cập nhật state sang giá trị mới, React lên lịch render lại; component cũng có thể render lại khi cha render. React sau đó cập nhật những phần DOM cần thiết, không phải tải lại toàn bộ trang.

Ví dụ gõ vào ô tìm kiếm: `onChange` → `setSearchTerm` → render với từ khóa mới → tính lại displayedCategories → bảng hiển thị kết quả mới.

Đừng nói “chỉ cần ghi localStorage thì React tự cập nhật”: trong bản code này, cập nhật UI đi qua `setDataState`, còn localStorage giữ dữ liệu sau reload.

**6. Controlled input, sự kiện và preventDefault**

Ô nhập trong LoginPage dòng 49–50:

```jsx
value={username}
onChange={(e) => setUsername(e.target.value)}
```

> Đây là controlled input vì value lấy từ state username. Khi gõ, onChange đọc giá trị trong ô nhập và gọi setUsername. State là nơi giữ giá trị mà giao diện hiển thị.

Ba sự kiện cần nhớ:

| Sự kiện | Khi nào chạy | Ví dụ trong bài |
|---|---|---|
| onClick | Bấm nút | Mở popup, chọn menu, xác nhận xóa |
| onChange | Giá trị ô nhập thay đổi | Nhập username, tên Category, từ khóa |
| onSubmit | Gửi form, như bấm nút submit hoặc Enter | Login, lưu form Category |

`e.preventDefault()` chặn hành vi mặc định của submit form, thường là điều hướng/tải lại trang. Sau đó code tự kiểm tra dữ liệu và cập nhật state.

Validation là kiểm tra dữ liệu hợp lệ: không bỏ trống tên, News phải có danh mục tồn tại, username không trùng, role/status đúng giá trị. Form kiểm tra để báo lỗi sớm; service kiểm tra lại quy tắc dữ liệu.

**7. Luồng đăng nhập: câu gần như phải luyện đầu tiên**

Mở theo thứ tự:

1. `LoginPage.jsx:5`: username/password lưu bằng state.
2. `LoginPage.jsx:9`: submit chạy handleSubmit, có preventDefault.
3. `newsService.js:18`: validateLogin kiểm tra rỗng và chính xác Admin/Admin.
4. `LoginPage.jsx:18`: hợp lệ thì gọi onLoginSuccess với thông tin Admin.
5. `App.jsx:19`: handleLoginSuccess gọi setCurrentUser và chọn Dashboard.
6. `App.jsx:63`: khi currentUser có giá trị, App đi tiếp để render giao diện quản trị.

Mẫu nói:

> Khi submit, form lấy username và password đang nằm trong state, chặn submit mặc định rồi gọi validateLogin. Nếu rỗng hoặc sai thì hiển thị lỗi và dừng. Nếu đúng Admin/Admin thì gọi callback về App. App cập nhật currentUser, nên lần render tiếp theo hiển thị layout quản trị.

Logout ở App dòng 25: đặt currentUser về null, reset menu về Dashboard và xóa lỗi chung; điều kiện render đưa người dùng về LoginPage. Logout không xóa dữ liệu CRUD khỏi localStorage.

Chú ý: kiểm tra credential phân biệt hoa/thường. `admin/admin` không giống `Admin/Admin`. Tạo User mới trong bảng Users cũng chưa làm tài khoản đó đăng nhập được vì validateLogin vẫn kiểm tra cố định.

**8. Chọn menu bằng gì?**

Sidebar dòng 20 gọi `onSelectTab(item.key)`. App dòng 72 nhận và cập nhật `activeTab`. Các điều kiện từ App dòng 87 quyết định trang nào xuất hiện.

> Bài này điều hướng bằng state activeTab và conditional rendering. Ví dụ activeTab bằng categories thì render CategoryManagement. Chuyển menu không tải lại trang và không thay URL route.

Conditional rendering nghĩa là hiển thị theo điều kiện: chưa login thì hiện LoginPage; có lỗi thì hiện thông báo; isOpen bằng false thì Modal trả về null.

**9. Popup thêm/sửa biết đang ở chế độ nào?**

Trong CategoryManagement dòng 8:

```jsx
{ isOpen: false, mode: 'create', item: null }
```

- `isOpen`: popup có đang mở không.
- `mode`: create hay update, phục vụ tiêu đề và điền dữ liệu form.
- `item`: bản ghi được chọn để sửa; thêm mới thì null.
- `name`, `status` ở CategoryDialog: dữ liệu người dùng đang nhập trong form.

Add chạy handleOpenCreate, đặt isOpen true, mode create, item null. Edit chạy handleOpenEdit(item), đặt mode update và giữ item được chọn.

CategoryDialog dòng 9 có useEffect phụ thuộc `[isOpen, mode, initialData]`. Khi mở form, effect điền dữ liệu cũ nếu update; nếu create thì reset tên rỗng và status mặc định 1. Điều này tránh form thêm mới giữ lại dữ liệu của lần sửa trước.

> Trang quản lý giữ trạng thái popup và bản ghi được chọn. Dialog nhận chúng qua props, còn dữ liệu ô nhập nằm trong state riêng của dialog.

**10. Luồng Create Category: hiểu kỹ một luồng này trước**

Ví dụ người dùng thêm danh mục “Sports”:

1. Bấm Add New Category → handleOpenCreate mở popup ở chế độ create.
2. CategoryDialog reset form. Người dùng gõ Sports → onChange cập nhật name.
3. Bấm Save → handleSubmit kiểm tra name.trim() không rỗng.
4. Dialog gọi onSave với name và status.
5. CategoryManagement.handleSave gọi onSaveRecord với entity categories và id null.
6. App.handleSaveRecord gọi saveRecord của service.
7. Service kiểm tra dữ liệu, tạo id bằng crypto.randomUUID(), tạo mảng mới bằng `[...data[entity], record]`.
8. App lưu dữ liệu mới vào localStorage, sau đó gọi setDataState.
9. Giao diện render theo dữ liệu mới; handleSave đóng popup. Nếu còn bộ lọc tìm kiếm, bảng chỉ hiện bản ghi khớp bộ lọc.

Mẫu nói ngắn:

> Dữ liệu đi từ input vào state của dialog. Submit hợp lệ thì dialog gọi callback lên trang quản lý rồi lên App. Service tạo bản ghi có id mới và danh sách mới. App lưu localStorage, cập nhật state, nên bảng render lại; luồng lưu thành công đóng popup.

Chỗ mở code: CategoryManagement dòng 17 và 31 → CategoryDialog dòng 22 → App dòng 31 → newsService dòng 30.

**11. Update khác Create ở đâu?**

> Edit giữ bản ghi được chọn và đưa dữ liệu cũ vào form. Khi lưu, trang truyền id của bản ghi đó về service. Service tìm đúng id và dùng map để thay đúng phần tử, giữ nguyên các phần tử khác. Create không có id nên tạo id mới và thêm vào mảng.

Code quyết định thêm/sửa thực tế trong service dựa vào bản ghi tìm được bằng id, không chỉ dựa vào chữ mode của popup:

```js
[entity]: existing
  ? data[entity].map((item) => item.id === id ? record : item)
  : [...data[entity], record]
```

**12. Xóa có confirmation như thế nào?**

CategoryManagement dòng 42–60:

> Bấm Delete mới chỉ lưu bản ghi vào deleteTarget và mở xác nhận. Bấm Cancel chỉ đặt deleteTarget về null, không gọi hàm xóa. Bấm Confirm mới truyền id về App rồi service. Service dùng filter tạo mảng không còn id đó; App lưu và cập nhật state.

Service chặn xóa Category đang được News sử dụng. Vì vậy nếu demo xóa, hãy tạo một Category mới chưa có News để thử. Cũng có ràng buộc bảo vệ Admin mặc định và User đang là tác giả bài viết.

**13. Immutable update: vì sao không dùng push/splice trực tiếp lên state?**

> State nên được xem như dữ liệu chỉ đọc. Em tạo mảng/object mới thay vì sửa trực tiếp dữ liệu đang nằm trong state. React so sánh giá trị state và tham chiếu; nếu sửa cùng object rồi set lại cùng tham chiếu, có thể không cập nhật như mong đợi. Cách bất biến cũng giữ được dữ liệu cũ để việc xử lý dễ theo dõi.

Nhớ ba cách tương ứng:

| Thao tác | Cách trong service |
|---|---|
| Thêm | Spread: `[...oldList, newRecord]` |
| Sửa | map: tạo mảng mới, thay phần tử đúng id |
| Xóa | filter: tạo mảng mới, bỏ phần tử đúng id |

Chỗ mở: newsService dòng 61–66 và 80. Khi sửa phần tử, bản code tạo record mới; dùng map đơn thuần rồi sửa trực tiếp object cũ vẫn có thể là mutation.

**14. Search có làm mất danh sách gốc không?**

CategoryManagement dòng 13–15 có danh sách hiển thị:

```js
const displayedCategories = searchTerm.trim()
  ? searchRecords(categories, 'name', searchTerm)
  : categories;
```

Service dòng 24:

```js
const query = keyword.trim().toLowerCase();
return records.filter((record) => String(record[field] ?? '').toLowerCase().includes(query));
```

> categories là danh sách nguồn. displayedCategories là kết quả phục vụ hiển thị. Khi gõ từ khóa, code bỏ khoảng trắng đầu/cuối, chuyển về chữ thường và filter theo name. Code không ghi đè danh sách nguồn. Xóa từ khóa thì hiển thị lại toàn bộ.

Category tìm theo name; News theo title; Users theo username. Không nhầm với tìm toàn bộ các field.

**15. map và key; empty state và no-result state**

CategoryManagement dòng 125 dùng `displayedCategories.map(...)` để tạo một hàng JSX cho mỗi bản ghi. `<tr key={item.id}>` cho React một định danh ổn định của mỗi hàng giữa những lần thêm/sửa/xóa. Key cần duy nhất giữa các phần tử cùng cấp; id phù hợp hơn vị trí index cho danh sách thay đổi.

> Empty state là danh sách nguồn chưa có dữ liệu. No-result state là nguồn vẫn có dữ liệu nhưng không bản ghi nào khớp tìm kiếm. Trang kiểm tra hai trường hợp riêng để đưa ra thông báo phù hợp.

Chỗ mở: CategoryManagement dòng 102–113 và 125–126.

**16. Dữ liệu nằm ở đâu, F5 còn gì?**

> Dữ liệu categories, news, users được giữ chung trong state data của App. Khi lưu hoặc xóa thành công, App gọi persistData, dùng JSON.stringify ghi vào localStorage. Khi App khởi tạo, loadData đọc chuỗi bằng getItem, JSON.parse thành object và kiểm tra cấu trúc. Chưa có dữ liệu hoặc dữ liệu không hợp lệ thì dùng dữ liệu mẫu; trường hợp đọc lỗi có cảnh báo.

Chỗ mở: App dòng 16, 31–47; newsService dòng 99–113.

| Sau F5 | Hành vi theo mã hiện tại |
|---|---|
| Đăng nhập | Mất vì currentUser khởi tạo null và không lưu storage |
| Dữ liệu đã lưu qua CRUD | Đọc lại được từ localStorage nếu hợp lệ và còn tồn tại |
| Từ khóa, popup, form chưa lưu | Mất/reset vì chỉ nằm trong state |
| Menu đang chọn | Reset vì activeTab khởi tạo dashboard |

localStorage ở trình duyệt, không phải database/server. Bản code này ghi storage trực tiếp trong hàm lưu/xóa của App; không dùng useEffect trong App để đồng bộ storage.

**17. Quan hệ News–Category–User; role/status**

> Mỗi News giữ categoryId để trỏ đến một Category, và createdBy để trỏ đến tác giả trong Users. Khi hiển thị, trang News tìm bản ghi liên quan để lấy tên. Service kiểm tra quan hệ tồn tại trước khi lưu, đồng thời chặn xóa dữ liệu đang được bài viết tham chiếu.

Role: 1 là Admin, 2 là Staff. Status: 1 là Active, 0 là Inactive. Nhãn hiển thị nằm trong ROLE_LABELS và STATUS_LABELS ở newsService dòng 2–3.

Khi cập nhật News, service giữ createdBy cũ thay vì tự đổi thành người đang sửa.

**18. Vì sao thiết kế như vậy?**

| Giảng viên hỏi | Câu trả lời có thể dùng |
|---|---|
| Vì sao tách component? | Để mỗi phần có trách nhiệm rõ. Header/Sidebar/Modal dùng lại được; trang quản lý tập trung xử lý nghiệp vụ của trang. |
| Vì sao data nằm ở App? | Dashboard và các trang quản lý đều cần dữ liệu; News còn cần cả categories và users. Đặt ở cha chung giúp chia sẻ nhất quán. |
| Vì sao form/search nằm ở component con? | Vì chỉ form hoặc trang đó cần sử dụng; không cần đưa mọi thao tác nhập lên App. |
| Vì sao dùng chung form thêm/sửa? | Hai thao tác có field và kiểm tra gần giống nhau; dùng mode và initialData để thay đổi trạng thái khởi tạo. |
| Vì sao dùng mock/localStorage? | Phạm vi Assignment 1 cho phép frontend độc lập. Dễ demo và giữ dữ liệu cục bộ; chưa có lưu trữ tập trung hoặc xác thực phía server. |
| Nếu kết nối Spring Boot? | Thay tầng service bằng gọi API; điều chỉnh handler thành async, thêm loading/error, nhận dữ liệu trả về rồi cập nhật state. Phần auth cũng cần đổi theo API. |
| Nếu thêm Staff đăng nhập? | Sửa validateLogin để tra tài khoản, kiểm tra trạng thái và role; sửa điều kiện menu và quyền vào trang. Khi có backend, quyền còn phải được kiểm tra phía server. |
| Nếu tìm kiếm/phân trang phía server? | Gửi keyword/page lên API, giữ state trang và loading/error; render items/total từ response thay vì filter toàn bộ dữ liệu cục bộ. |

**19. Nếu được hỏi debug, trả lời theo một trình tự**

Nói rõ: **hiện tượng → giả thuyết → chỗ kiểm tra → bằng chứng → sửa → thử lại**.

| Tình huống giả định | Cách bắt đầu |
|---|---|
| Popup không mở | Kiểm tra onClick có gọi handler không; dialogState.isOpen có thành true không; props isOpen truyền xuống đúng chưa; Modal có trả null không. |
| Sửa nhầm bản ghi | Kiểm tra item lúc bấm Edit, id được truyền vào handleSave và điều kiện item.id === id trong map. Không dùng index thay id. |
| Xóa không cập nhật UI | Kiểm tra Cancel/Confirm, id, lỗi ràng buộc của service, dữ liệu mới trả về và setter. Nếu giả định có mutation thì kiểm tra tham chiếu mảng/object. |
| Login đúng nhưng vẫn ở Login | Kiểm tra validateLogin trả gì, callback onLoginSuccess có được gọi không và currentUser có cập nhật không. |
| Search không đúng | Kiểm tra searchTerm, field name/title/username, normalize và có vô tình thay nguồn bằng kết quả lọc không. |
| Reload mất dữ liệu | Phân biệt state nào theo thiết kế phải reset; với CRUD thì kiểm tra key, JSON đã lưu, lỗi storage và loadData. |

Đây là cách phân tích các lỗi giả định, không phải khẳng định project hiện đang có các lỗi đó.

**20. Nếu bị yêu cầu sửa nhỏ tại chỗ**

Ví dụ “thêm nút Reset vào CategoryDialog”:

> Thay đổi ảnh hưởng giao diện dialog và state name/status/error. Em cần làm rõ Reset là về giá trị mặc định hay về dữ liệu lúc mở form. Nếu reset về mặc định thì đặt name rỗng, status 1, error rỗng. Nút dùng type button để không submit form. Sau đó thử nhập, bấm Reset, kiểm tra giá trị và xác nhận không tạo bản ghi mới.

Ví dụ “thêm lọc Active/Inactive”:

> Em thêm state của bộ lọc và ô chọn trạng thái ở trang quản lý. Danh sách hiển thị sẽ áp dụng từ khóa và điều kiện status, giữ nguyên danh sách nguồn. Em thử All, Active, Inactive, kết hợp tìm kiếm và trường hợp không có kết quả.

Đây là bài tập luyện, chưa được thêm vào mã nguồn trong lần hỗ trợ này.

**21. Lệnh và môi trường**

Chạy project từ thư mục gốc project, không phải đứng ở thư mục labs:

```powershell
cd F:\SBA301\labs\Assignment1\StudentName_ClassCode
npm install
npm run dev
```

- `npm install`: cài dependency của project vào node_modules, dùng manifest và lockfile để giải quyết phiên bản.
- `npm run dev`: chạy script dev trong package.json; bản này là `vite --host 127.0.0.1`.
- `npm run build`: đóng gói bản production.
- `npm test`: bản này chạy `node --test tests/*.test.js`, kiểm tra logic service.
- `package.json`: tên project, scripts và thư viện dùng trong dự án.
- `node_modules`: thư viện đã cài, thường không commit vì có thể cài lại.
- Dừng dev server bằng Ctrl+C.

Khi hỏi tạo project: hãy trình bày đúng thao tác bạn đã dùng. Với bản hiện tại, có thể giải thích rằng đây là project Vite template React và chỉ script trong package.json. Không cần tạo lại project đang có để luyện interview.

Các lệnh trên được đọc từ source; lần hỗ trợ này không chạy app hoặc xác nhận lại các mục PASS trong README.

**22. Nếu hỏi việc dùng AI**

Trình bày đúng phần bạn đã sử dụng và thực sự kiểm chứng. Có thể điền khung:

> Em dùng AI để hỗ trợ [phần thực tế]. Gợi ý là [nội dung]. Em đã đọc và thay đổi [phần thực tế], rồi kiểm tra bằng [thao tác/test thực sự đã chạy]. Phần em còn cần học thêm là [nội dung].

Đừng nhận rằng đã tự viết hoặc đã kiểm thử một phần nếu chưa làm. Bạn có thể dùng chính bản ôn này để đọc code và luyện giải thích lại bằng lời của mình.

**23. Luyện theo thứ tự nếu đang bắt đầu từ số 0**

1. **10 phút:** đăng nhập và bấm từng menu, tự nói ứng dụng làm gì; xác định ba loại dữ liệu.
2. **15 phút:** mở LoginPage → validateLogin → App, kể lại luồng đăng nhập. Phân biệt props và state bằng ví dụ thật.
3. **20 phút:** chỉ luyện Category: Add → nhập → Save → App → service → storage/state → bảng. Sau đó kể lại Edit và Delete.
4. **10 phút:** luyện Search, F5, map/key, conditional rendering.
5. **15 phút:** tự giải thích một lỗi giả định và tự thử một thay đổi nhỏ. Chạy lại thao tác cũ sau thay đổi.

Đây là lịch ôn đề xuất, không phải thời lượng hay bảo đảm kết quả của buổi chấm.

Tự kiểm tra bằng 6 câu, mỗi câu vừa nói vừa mở code:

1. Ai quyết định hiển thị Login hay Admin?
2. Tên Category đang nhập được giữ ở đâu?
3. Add và Edit khác nhau ở state và id thế nào?
4. Vì sao Cancel không xóa bản ghi?
5. Vì sao xóa từ khóa lại thấy toàn bộ dữ liệu?
6. Vì sao F5 mất đăng nhập nhưng dữ liệu đã lưu còn?

Nếu trả lời được sáu câu và chỉ được vị trí code tương ứng, bạn đã nắm được phần luồng cốt lõi để tiếp tục luyện các chủ đề còn lại trong mục 7.
