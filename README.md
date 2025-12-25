<p align="center">
  <img src="Resources/shopping%20cart%20icon.jpg" alt="Labee Logo" width="120" height="120">
</p>

<h1 align="center">🐝 Labee - E-Commerce Android App</h1>

<p align="center">
  <strong>Ứng dụng thương mại điện tử Android với kiến trúc MVC và Room Database</strong>
</p>

<p align="center">
  <a href="#features">Features</a> •
  <a href="#screenshots">Screenshots</a> •
  <a href="#installation">Installation</a> •
  <a href="#architecture">Architecture</a> •
  <a href="#contributing">Contributing</a> •
  <a href="#license">License</a>
</p>

<p align="center">
  <img src="https://img.shields.io/badge/Platform-Android-3DDC84?style=for-the-badge&logo=android&logoColor=white" alt="Platform">
  <img src="https://img.shields.io/badge/Language-Java-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white" alt="Language">
  <img src="https://img.shields.io/badge/Min%20SDK-24-blue?style=for-the-badge" alt="Min SDK">
  <img src="https://img.shields.io/badge/Target%20SDK-36-blue?style=for-the-badge" alt="Target SDK">
</p>

<p align="center">
  <img src="https://img.shields.io/badge/Architecture-MVC-purple?style=flat-square" alt="Architecture">
  <img src="https://img.shields.io/badge/Database-Room%20(SQLite)-green?style=flat-square" alt="Database">
  <img src="https://img.shields.io/badge/License-MIT-yellow?style=flat-square" alt="License">
  <img src="https://img.shields.io/badge/Version-1.0.0-red?style=flat-square" alt="Version">
</p>

---

## 📋 Mục Lục

- [Giới Thiệu](#-giới-thiệu)
- [Tính Năng](#-tính-năng)
- [Công Nghệ Sử Dụng](#-công-nghệ-sử-dụng)
- [Cấu Trúc Dự Án](#-cấu-trúc-dự-án)
- [Cơ Sở Dữ Liệu](#-cơ-sở-dữ-liệu)
- [Cài Đặt](#-cài-đặt)
- [Hướng Dẫn Sử Dụng](#-hướng-dẫn-sử-dụng)
- [API Documentation](#-api-documentation)
- [Contributing](#-contributing)
- [Code of Conduct](#-code-of-conduct)
- [License](#-license)
- [Tác Giả](#-tác-giả)

---

## 🎯 Giới Thiệu

**Labee** là ứng dụng thương mại điện tử Android được phát triển bằng Java, cung cấp trải nghiệm mua sắm trực tuyến hoàn chỉnh cho người dùng và công cụ quản lý hiệu quả cho quản trị viên.

### Đối tượng sử dụng:
- **👤 Khách hàng (Customer)**: Tìm kiếm, mua sắm sản phẩm, quản lý đơn hàng
- **👨‍💼 Quản trị viên (Admin)**: Quản lý sản phẩm, đơn hàng, thống kê doanh thu

---

## ✨ Tính Năng

### 🔐 Xác Thực (Authentication)

| Tính năng | Mô tả | Activity |
|-----------|-------|----------|
| Đăng nhập | Email/Password với phân quyền Admin/Customer | `LoginActivity` |
| Đăng ký | Tạo tài khoản mới | `RegisterActivity` |
| Quên mật khẩu | Gửi yêu cầu reset password | `ForgotPasswordActivity` |
| Xác thực OTP | Xác nhận mã OTP qua email | `VerifyCodeActivity` |
| Đặt lại mật khẩu | Tạo mật khẩu mới | `CreateNewPasswordActivity` |

### 🛒 Người Dùng (Customer)

<details>
<summary><b>🏠 Trang Chủ & Sản Phẩm</b></summary>

| Tính năng | Mô tả |
|-----------|-------|
| Banner quảng cáo | Auto-scroll banner (3s/lần) |
| Danh mục sản phẩm | Electronics, Fashion, Home, Sports, Beauty |
| Tìm kiếm | Tìm theo tên sản phẩm |
| Chi tiết sản phẩm | Hình ảnh, giá, mô tả, tồn kho |
| Xem tất cả | Grid view toàn bộ sản phẩm |

</details>

<details>
<summary><b>🛒 Giỏ Hàng & Thanh Toán</b></summary>

| Tính năng | Mô tả |
|-----------|-------|
| Thêm vào giỏ | Thêm sản phẩm với số lượng |
| Quản lý giỏ hàng | Tăng/giảm/xóa sản phẩm |
| Checkout | Chọn địa chỉ, phương thức thanh toán |
| Đặt hàng | Xác nhận và tạo đơn hàng |

</details>

<details>
<summary><b>📦 Quản Lý Đơn Hàng</b></summary>

| Tính năng | Mô tả |
|-----------|-------|
| Lịch sử đơn hàng | Xem danh sách đơn đã đặt |
| Chi tiết đơn hàng | Thông tin sản phẩm, địa chỉ, trạng thái |
| Hủy đơn hàng | Hủy đơn ở trạng thái Pending |

</details>

<details>
<summary><b>📍 Quản Lý Địa Chỉ</b></summary>

| Tính năng | Mô tả |
|-----------|-------|
| Danh sách địa chỉ | Xem các địa chỉ đã lưu |
| CRUD địa chỉ | Thêm/Sửa/Xóa địa chỉ |
| Địa chỉ mặc định | Đặt địa chỉ mặc định |

</details>

<details>
<summary><b>🎫 Voucher & Khuyến Mãi</b></summary>

| Tính năng | Mô tả |
|-----------|-------|
| Danh sách voucher | Xem mã giảm giá có sẵn |
| Thu thập voucher | Lưu mã vào ví voucher |

</details>

### 👨‍💼 Quản Trị Viên (Admin)

| Tính năng | Mô tả | Activity |
|-----------|-------|----------|
| Dashboard | Trang chủ quản trị | `AdminHomeActivity` |
| Quản lý sản phẩm | CRUD sản phẩm | `AdminProductListActivity`, `AdminProductEditActivity` |
| Quản lý đơn hàng | Xem và cập nhật trạng thái | `AdminOrderListActivity`, `AdminOrderDetailActivity` |
| Thống kê doanh thu | Báo cáo doanh thu, số đơn | `AdminRevenueActivity` |

---

## 🛠 Công Nghệ Sử Dụng

### Core
| Công nghệ | Phiên bản | Mô tả |
|-----------|-----------|-------|
| **Java** | 11 | Ngôn ngữ lập trình chính |
| **Android SDK** | 36 | Target SDK |
| **Gradle** | 8.12.3 | Build system |

### Android Jetpack & Libraries
| Library | Phiên bản | Mục đích |
|---------|-----------|----------|
| **Room Database** | 2.6.1 | Local database (SQLite) |
| **Material Design** | 1.13.0 | UI Components |
| **RecyclerView** | 1.3.0 | Hiển thị danh sách |
| **ViewPager2** | 1.1.0 | Banner slider |
| **CardView** | 1.0.0 | Card UI |
| **Glide** | 4.15.1 | Image loading |
| **Gson** | 2.10.1 | JSON processing |
| **Google Play Services Auth** | 20.7.0 | Google Sign-In |

---

## 📁 Cấu Trúc Dự Án

```
app/src/main/java/com/example/lazabee/
│
├── 📂 adapter/                     # RecyclerView Adapters
│   ├── AddressAdapter.java         # Adapter địa chỉ
│   ├── CartItemAdapter.java        # Adapter giỏ hàng
│   ├── CheckoutItemAdapter.java    # Adapter checkout
│   ├── OrderAdapter.java           # Adapter đơn hàng
│   ├── OrderItemAdapter.java       # Adapter chi tiết đơn
│   ├── ProductAdapter.java         # Adapter sản phẩm
│   └── VoucherAdapter.java         # Adapter voucher
│
├── 📂 database/                    # Room Database
│   ├── AppDatabase.java            # Database configuration
│   └── LabeeDao.java               # Data Access Object
│
├── 📂 model/                       # Data Models (Entities)
│   ├── User.java                   # Entity người dùng
│   ├── Product.java                # Entity sản phẩm
│   ├── Order.java                  # Entity đơn hàng
│   ├── OrderItem.java              # Entity chi tiết đơn
│   ├── OrderItemDetail.java        # POJO chi tiết đơn + tên SP
│   ├── CartItem.java               # Entity giỏ hàng
│   ├── CartItemDetail.java         # POJO giỏ hàng + thông tin SP
│   ├── Address.java                # Entity địa chỉ
│   └── Voucher.java                # Entity voucher
│
├── 📂 utils/                       # Utilities
│   ├── Constants.java              # Hằng số
│   ├── NetworkUtils.java           # Network utilities
│   └── SharedPreferencesManager.java # Quản lý session
│
├── 📂 view/                        # Activities (UI)
│   │
│   │── 🔐 Authentication
│   ├── LoginActivity.java
│   ├── RegisterActivity.java
│   ├── ForgotPasswordActivity.java
│   ├── VerifyCodeActivity.java
│   └── CreateNewPasswordActivity.java
│   │
│   │── 🏠 Customer
│   ├── HomeActivity.java
│   ├── SearchActivity.java
│   ├── ProductDetailActivity.java
│   ├── ProductFullActivity.java
│   ├── CartActivity.java
│   ├── CheckoutActivity.java
│   ├── OrderSuccessActivity.java
│   ├── OrderHistoryActivity.java
│   ├── OrderDetailActivity.java
│   ├── AddressManagementActivity.java
│   ├── AddAddressActivity.java
│   ├── VoucherListActivity.java
│   └── UserProfileActivity.java
│   │
│   │── 👨‍💼 Admin
│   ├── AdminHomeActivity.java
│   ├── AdminProductListActivity.java
│   ├── AdminProductEditActivity.java
│   ├── AdminOrderListActivity.java
│   ├── AdminOrderDetailActivity.java
│   └── AdminRevenueActivity.java
│
├── BannerAdapter.java              # Banner ViewPager adapter
├── CategoryItemView.java           # Custom category view
├── LazabeeApplication.java         # Application class
├── MainActivity.java               # Entry point
└── ProductFullActivity.java        # All products screen
```

### 📂 Resources Structure

```
app/src/main/res/
├── 📂 layout/                      # XML Layouts (35 files)
│   ├── activity_*.xml              # Activity layouts
│   └── item_*.xml                  # RecyclerView item layouts
│
├── 📂 drawable/                    # Images & Drawables
│   ├── ic_*.xml                    # Vector icons
│   ├── img_*.png                   # Product images
│   ├── banner_*.png                # Banner images
│   └── *_background.xml            # Shape drawables
│
└── 📂 values/                      # Resources values
    ├── colors.xml
    ├── strings.xml
    └── themes.xml
```

---

## 🗄 Cơ Sở Dữ Liệu

### ERD (Entity Relationship Diagram)

```
┌─────────────┐       ┌─────────────┐       ┌─────────────┐
│   USERS     │       │  PRODUCTS   │       │  VOUCHERS   │
├─────────────┤       ├─────────────┤       ├─────────────┤
│ PK: id      │       │ PK: id      │       │ id          │
│ email       │       │ name        │       │ code        │
│ password    │       │ price       │       │ title       │
│ fullName    │       │ description │       │ discountAmt │
│ phone       │       │ imageResName│       │ quantity    │
│ address     │       │ category    │       │ expiryDate  │
│ role        │       │ stock       │       │ isCollected │
└──────┬──────┘       └──────┬──────┘       └─────────────┘
       │                     │
       │ 1:N                 │ 1:N
       ▼                     ▼
┌─────────────┐       ┌─────────────┐
│  ADDRESSES  │       │ CART_ITEMS  │
├─────────────┤       ├─────────────┤
│ PK: id      │       │ PK: id      │
│ FK: userId  │       │ FK: userId  │
│ name        │       │ FK: productId│
│ phone       │       │ quantity    │
│ address     │       └─────────────┘
│ isDefault   │
└─────────────┘
       │
       │ 1:N
       ▼
┌─────────────┐       ┌─────────────┐
│   ORDERS    │──1:N──│ ORDER_ITEMS │
├─────────────┤       ├─────────────┤
│ PK: id      │       │ PK: id      │
│ FK: userId  │       │ FK: orderId │
│ totalPrice  │       │ FK: productId│
│ date        │       │ quantity    │
│ status      │       │ price       │
│ address     │       └─────────────┘
│ phoneNumber │
│ paymentMethod│
│ note        │
└─────────────┘
```

### Bảng Dữ Liệu

<details>
<summary><b>📊 Chi tiết các bảng</b></summary>

#### Users
| Column | Type | Description |
|--------|------|-------------|
| id | INTEGER | Primary Key, Auto Increment |
| email | TEXT | Unique, Email đăng nhập |
| password | TEXT | Mật khẩu |
| fullName | TEXT | Họ và tên |
| phone | TEXT | Số điện thoại |
| address | TEXT | Địa chỉ |
| role | TEXT | "admin" / "customer" |

#### Products
| Column | Type | Description |
|--------|------|-------------|
| id | INTEGER | Primary Key, Auto Increment |
| name | TEXT | Tên sản phẩm |
| price | INTEGER | Giá (VNĐ) |
| description | TEXT | Mô tả |
| imageResName | TEXT | Tên file hình trong drawable |
| category | TEXT | Danh mục |
| stock | INTEGER | Số lượng tồn kho |

#### Orders
| Column | Type | Description |
|--------|------|-------------|
| id | INTEGER | Primary Key, Auto Increment |
| userId | INTEGER | FK → users.id |
| totalPrice | INTEGER | Tổng tiền |
| date | TEXT | Ngày đặt |
| status | TEXT | Pending/Shipping/Completed/Cancelled |
| address | TEXT | Địa chỉ giao |
| phoneNumber | TEXT | SĐT nhận hàng |
| paymentMethod | TEXT | COD/Bank/Card/EWallet |
| note | TEXT | Ghi chú |

</details>

---

## 🚀 Cài Đặt

### Yêu Cầu Hệ Thống

- **Android Studio**: Arctic Fox (2020.3.1) trở lên
- **JDK**: 11 hoặc cao hơn
- **Android SDK**: API 24+ (Android 7.0 Nougat)
- **Gradle**: 8.0+

### Các Bước Cài Đặt

1. **Clone repository**
   ```bash
   git clone https://github.com/your-username/labee.git
   cd labee
   ```

2. **Mở project trong Android Studio**
   ```
   File → Open → Chọn thư mục project
   ```

3. **Sync Gradle**
   ```
   File → Sync Project with Gradle Files
   ```

4. **Build & Run**
   ```
   Run → Run 'app' hoặc nhấn Shift + F10
   ```

### Tài Khoản Test

| Role | Email | Password |
|------|-------|----------|
| Admin | admin@labee.com | admin123 |
| Customer | user@labee.com | user123 |

---

## 📖 Hướng Dẫn Sử Dụng

### Khách Hàng

1. **Đăng ký/Đăng nhập** → Tạo tài khoản hoặc đăng nhập
2. **Trang chủ** → Duyệt sản phẩm theo danh mục hoặc tìm kiếm
3. **Chi tiết sản phẩm** → Xem thông tin và thêm vào giỏ
4. **Giỏ hàng** → Kiểm tra và điều chỉnh đơn hàng
5. **Thanh toán** → Chọn địa chỉ, phương thức thanh toán
6. **Theo dõi đơn hàng** → Xem trạng thái trong lịch sử đơn

### Quản Trị Viên

1. **Đăng nhập** với tài khoản Admin
2. **Dashboard** → Truy cập các chức năng quản lý
3. **Quản lý sản phẩm** → Thêm/Sửa/Xóa sản phẩm
4. **Quản lý đơn hàng** → Cập nhật trạng thái đơn
5. **Thống kê** → Xem báo cáo doanh thu

---

## 📚 API Documentation

Chi tiết API documentation có thể xem tại: [docs/API-DOCUMENTATION.md](docs/API-DOCUMENTATION.md)

### Quick Reference

| Endpoint | Method | Description |
|----------|--------|-------------|
| `/api/v1/auth/login` | POST | Đăng nhập |
| `/api/v1/auth/register` | POST | Đăng ký |
| `/api/v1/products` | GET | Danh sách sản phẩm |
| `/api/v1/cart` | GET/POST | Giỏ hàng |
| `/api/v1/orders` | GET/POST | Đơn hàng |

---

## 🤝 Contributing

Chúng tôi hoan nghênh mọi đóng góp! Vui lòng đọc [CONTRIBUTING.md](CONTRIBUTING.md) để biết thêm chi tiết.

### Quy Trình Đóng Góp

1. **Fork** repository
2. **Clone** fork về máy
   ```bash
   git clone https://github.com/your-username/labee.git
   ```
3. **Tạo branch** cho feature/fix
   ```bash
   git checkout -b feature/amazing-feature
   ```
4. **Commit** changes
   ```bash
   git commit -m "feat: add amazing feature"
   ```
5. **Push** to branch
   ```bash
   git push origin feature/amazing-feature
   ```
6. Tạo **Pull Request**

### Commit Convention

```
feat:     Tính năng mới
fix:      Sửa lỗi
docs:     Thay đổi documentation
style:    Format code (không ảnh hưởng logic)
refactor: Refactor code
test:     Thêm tests
chore:    Maintenance tasks
```

---

## 📜 Code of Conduct

### Cam Kết Của Chúng Tôi

Chúng tôi cam kết tạo ra một môi trường thân thiện, an toàn và chào đón tất cả mọi người, bất kể:
- Kinh nghiệm
- Giới tính, bản dạng giới và biểu hiện giới
- Xu hướng tính dục
- Khuyết tật
- Ngoại hình
- Kích thước cơ thể
- Chủng tộc, dân tộc
- Tuổi tác
- Tôn giáo
- Quốc tịch

### Tiêu Chuẩn Hành Vi

**✅ Hành vi được khuyến khích:**
- Sử dụng ngôn ngữ thân thiện và hòa nhập
- Tôn trọng các quan điểm và trải nghiệm khác nhau
- Chấp nhận phê bình mang tính xây dựng
- Tập trung vào những gì tốt nhất cho cộng đồng
- Thể hiện sự đồng cảm với các thành viên khác

**❌ Hành vi không được chấp nhận:**
- Ngôn ngữ hoặc hình ảnh mang tính tình dục
- Trolling, bình luận xúc phạm hoặc tấn công cá nhân
- Quấy rối công khai hoặc riêng tư
- Công bố thông tin cá nhân của người khác
- Các hành vi không phù hợp trong môi trường chuyên nghiệp

### Thực Thi

Các trường hợp vi phạm có thể được báo cáo qua email của team. Tất cả các báo cáo sẽ được xem xét và điều tra.

---

## 📄 License

```
MIT License

Copyright (c) 2024 Labee Team

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
```

---

## 👥 Tác Giả

<table>
  <tr>
    <td align="center">
      <a href="https://github.com/your-username">
        <img src="https://via.placeholder.com/100" width="100px;" alt=""/>
        <br />
        <sub><b>Team Labee</b></sub>
      </a>
    </td>
  </tr>
</table>

---

## 🙏 Acknowledgments

- [Android Developers](https://developer.android.com/) - Documentation & Guides
- [Material Design](https://material.io/) - Design Guidelines
- [Glide](https://github.com/bumptech/glide) - Image Loading Library
- [Room Database](https://developer.android.com/training/data-storage/room) - Local Database

---

## 📞 Liên Hệ

- **Email**: labee.team@example.com
- **Issues**: [GitHub Issues](https://github.com/your-username/labee/issues)

---

<p align="center">
  Made with ❤️ by Labee Team
</p>

<p align="center">
  <a href="#-labee---e-commerce-android-app">⬆ Back to Top</a>
</p>
