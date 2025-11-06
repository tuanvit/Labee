### \#\# Kế hoạch Tổng thể (Hiện thực Chương 3 với Spring Boot)

- **Bạn (Hoàng - Backend):** Nhiệm vụ của bạn là xây dựng một **RESTful API** bằng Spring Boot. API này chính là "nhà bếp" cung cấp dữ liệu.
- **Bạn của bạn (Việt - Frontend):** Nhiệm vụ của bạn là xây dựng ứng dụng Android (theo MVVM) và dùng thư viện **Retrofit** để "gọi món" (call API) từ backend của Hoàng.
- **"Hợp đồng" (Giao ước):** Hai bạn phải thống nhất với nhau về các **API Endpoint** (Bước 2 dưới đây). Đây là menu chung, là thứ kết nối hai bạn với nhau.

---

### \#\# Giai đoạn 1: Hoàng (Backend) - Xây dựng "Xương sống" Spring Boot

**Mục tiêu:** Tạo dự án và kết nối thành công với cơ sở dữ liệu (CSDL).

1.  **Tạo Dự án:**
    - Truy cập `start.spring.io`.
    - Chọn `Maven` (hoặc Gradle), ngôn ngữ `Java`.
    - Trong "Dependencies", thêm các gói sau:
      - **Spring Web:** Để tạo API.
      - **Spring Data JPA:** Để làm việc với CSDL.
      - **MySQL Driver** (hoặc **PostgreSQL Driver**): Driver cho CSDL bạn chọn.
      - **Spring Security:** Để bảo mật (cực kỳ quan trọng, sẽ làm ở Giai đoạn 4).
      - **Lombok:** (Tùy chọn) Giúp code ngắn gọn hơn.
2.  **Cài đặt CSDL:**
    - Cài đặt một CSDL (như MySQL hoặc PostgreSQL) trên máy của bạn.
    - Tạo một CSDL (schema) mới, ví dụ: `labee_db`.
3.  **Kết nối CSDL:**

    - Mở file `src/main/resources/application.properties`.
    - Thêm các dòng cấu hình để Spring Boot biết CSDL của bạn ở đâu:

      ```properties
      # Database Connection
      spring.datasource.url=jdbc:mysql://localhost:3306/labee_db
      spring.datasource.username=root
      spring.datasource.password=mật_khẩu_của_bạn

      # Spring Data JPA
      spring.jpa.hibernate.ddl-auto=update
      spring.jpa.show-sql=true
      ```

4.  **Chạy thử:** Chạy dự án. Nếu không có lỗi, bạn đã thành công kết nối CSDL.

---

### \#\# Giai đoạn 2: Hoàng (Backend) - "Số hóa" Sơ đồ ERD

**Mục tiêu:** Biến sơ đồ ERD (Hình 1.5) của bạn thành các `class` Java.

1.  **Tạo các Lớp `@Entity`:**
    - Trong code Spring, bạn tạo các file Java tương ứng với **mỗi thực thể** trong sơ đồ ERD v2.
    - Sử dụng các annotation (chú thích) của JPA để định nghĩa chúng:
      - `@Entity`
      - `@Table(name="products")`
      - `@Id` (cho các trường `PK` như `productId`)
      - `@Column` (để định nghĩa các trường)
    - **Quan trọng nhất - Định nghĩa Mối quan hệ:**
      - **Trong `Product.java`:**
        `@ManyToOne`
        `private Category category;`
      - **Trong `User.java`:**
        `@OneToMany(mappedBy="user")`
        `private List<Order> orders;`
        `@OneToMany(mappedBy="user")`
        `private List<Address> addresses;`
      - **Trong `Order.java`:**
        `@ManyToOne`
        `private User user;`
        `@OneToMany(mappedBy="order")`
        `private List<OrderItem> orderItems;`
2.  **Tạo các `Repository`:**
    - Tạo các `interface` extends `JpaRepository` (ví dụ: `ProductRepository`, `UserRepository`, `OrderRepository`...).
    - Spring Data JPA sẽ tự động cung cấp các hàm `save()`, `findById()`, `findAll()`... cho bạn.

> **Kết thúc Giai đoạn 2:** Bạn đã có một lớp CSDL hoàn chỉnh, sẵn sàng để lưu và truy xuất dữ liệu.

---

### \#\# Giai đoạn 3: Hoàng & Việt (Chung) - Thống nhất "Hợp đồng API"

**Mục tiêu:** Hai bạn phải thống nhất chính xác về các API. Dưới đây là danh sách API "best practice" cho Labee:

| Mục đích             | Phương thức | Endpoint (Đường dẫn)         | Bảo mật?       |
| :------------------- | :---------- | :--------------------------- | :------------- |
| Đăng ký              | `POST`      | `/api/v1/auth/register`      | Không          |
| Đăng nhập            | `POST`      | `/api/v1/auth/login`         | Không          |
| Lấy Banner           | `GET`       | `/api/v1/banners`            | Không          |
| Lấy Danh mục         | `GET`       | `/api/v1/categories`         | Không          |
| Lấy SP (theo DM)     | `GET`       | `/api/v1/products`           | Không          |
| Lấy chi tiết SP      | `GET`       | `/api/v1/products/{id}`      | Không          |
| Lấy giỏ hàng         | `GET`       | `/api/v1/cart`               | **Có**         |
| Thêm vào giỏ         | `POST`      | `/api/v1/cart`               | **Có**         |
| Đặt hàng             | `POST`      | `/api/v1/orders/place-order` | **Có**         |
| Lấy lịch sử đơn hàng | `GET`       | `/api/v1/orders`             | **Có**         |
| Admin: Thêm SP       | `POST`      | `/api/v1/admin/products`     | **Có (Admin)** |
| Admin: Sửa đơn       | `PUT`       | `/api/v1/admin/orders/{id}`  | **Có (Admin)** |

---

### \#\# Giai đoạn 4: Hoàng (Backend) - Hiện thực API (Logic nghiệp vụ)

**Mục tiêu:** Viết code cho các API đã thống nhất.

1.  **Tạo các `@Service`:**
    - Tạo các lớp `Service` (ví dụ: `ProductService`, `OrderService`) để chứa logic nghiệp vụ.
    - **Quan trọng nhất:** Trong `OrderService`, bạn sẽ viết hàm `placeOrder()`. Hàm này phải được đánh dấu **`@Transactional`**. Đây chính là thứ thay thế cho "Firestore Transaction", đảm bảo việc "trừ kho", "tạo snapshot" và "lưu đơn hàng" diễn ra một cách an toàn.
2.  **Tạo các `@RestController`:**
    - Tạo các lớp `Controller` (ví dụ: `ProductController`, `AuthController`).
    - Dùng `@GetMapping`, `@PostMapping`... để ánh xạ các endpoint ở Giai đoạn 3.
    - Các hàm Controller sẽ gọi các hàm Service (ví dụ: `ProductController` gọi `ProductService`).
3.  **Triển khai Bảo mật (Spring Security + JWT):**
    - Đây là phần khó nhất bạn phải tự học (thay cho `Firebase Authentication`).
    - Khi người dùng gọi `POST /api/v1/auth/login`, `AuthService` của bạn phải kiểm tra mật khẩu. Nếu đúng, nó sẽ tạo ra một **JWT Token** (một chuỗi mã hóa) và trả về cho Frontend.
    - Bạn phải cấu hình Spring Security để nó _chặn_ tất cả các API có đánh dấu "Có" ở trên. Nó sẽ chỉ cho phép truy cập nếu Frontend gửi kèm JWT Token hợp lệ trong `Authorization` header.
4.  **Kiểm tra API:**
    - Dùng công cụ **Postman** để gọi thử các API của bạn và xem kết quả JSON trả về. **Đừng đưa cho Việt khi bạn chưa test bằng Postman.**

---

### \#\# Giai đoạn 5: Việt (Frontend) - Hiện thực Giao diện

**Mục tiêu:** Xây dựng app Android (MVVM) và gọi API của Hoàng.

1.  **Bỏ Firebase:**
    - Xóa file `google-services.json`.
    - Xóa tất cả thư viện `com.google.firebase` khỏi file `build.gradle`.
2.  **Thêm Retrofit:**
    - Thêm thư viện `Retrofit` và `Gson` (hoặc `Moshi`) vào `build.gradle`. Đây là công cụ gọi API mạnh nhất cho Android.
3.  **Cập nhật Kiến trúc MVVM (Hình 1.4):**
    - **View (Activity/Fragment):** Giữ nguyên như thiết kế.
    - **ViewModel:** Giữ nguyên.
    - **Model (Repository):** Đây là lớp thay đổi nhiều nhất\!
      - **Trước (Firebase):** `Repository` gọi `FirebaseFirestore.getInstance()...`
      - **Bây giờ (Spring):** `Repository` sẽ gọi `ApiService.getProducts()`.
4.  **Tạo `ApiService`:**

    - Tạo một `interface` `ApiService.java` bằng Retrofit, định nghĩa chính xác các API ở Giai đoạn 3.

      ```java
      public interface ApiService {
          @GET("/api/v1/products")
          Call<List<Product>> getProducts(@Query("categoryId") String categoryId);

          @POST("/api/v1/auth/login")
          Call<LoginResponse> login(@Body LoginRequest loginRequest);

          @POST("/api/v1/orders/place-order")
          Call<Order> placeOrder(@Header("Authorization") String token, @Body PlaceOrderRequest request);
      }
      ```

5.  **Quản lý Token (JWT):**
    - Sau khi gọi API "login" thành công, Việt phải lưu cái **JWT Token** mà Hoàng trả về (lưu vào `EncryptedSharedPreferences`).
    - Với mọi API yêu cầu bảo mật (như "Đặt hàng"), Việt phải đọc token này và thêm vào Header của Retrofit (như ví dụ trên).

Bằng cách này, bạn sẽ học được toàn bộ quy trình xây dựng một hệ thống backend chuyên nghiệp, và bạn của bạn cũng học được cách tích hợp với một RESTful API chuẩn công nghiệp. Chúc may mắn\!
