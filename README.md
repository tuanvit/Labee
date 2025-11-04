# Lazabee - E-commerce Android App

Ứng dụng thương mại điện tử được xây dựng theo kiến trúc MVVM với PostgreSQL backend.

## Cấu trúc Project

### Android App (MVVM Architecture)
```
app/src/main/java/com/example/lazabee/
├── data/
│   ├── model/          # Data models (User, LoginRequest, etc.)
│   ├── local/          # Room database (UserDao, AppDatabase)
│   ├── remote/         # API services (ApiService, ApiClient)
│   └── repository/     # Repository pattern (AuthRepository)
├── view/               # Activities/Fragments
├── viewmodel/          # ViewModels
└── utils/              # Utilities (SharedPreferencesManager)
```

### Backend API (Node.js + PostgreSQL)
```
backend/
├── config/             # Database configuration
├── routes/             # API routes
├── server.js           # Main server file
├── package.json        # Dependencies
└── database.sql        # Database schema
```

## Setup Instructions

### 1. Database Setup (PostgreSQL)

1. Cài đặt PostgreSQL
2. Tạo database và chạy script:
```bash
psql -U postgres
\i backend/database.sql
```

### 2. Backend Setup

1. Di chuyển vào thư mục backend:
```bash
cd backend
```

2. Cài đặt dependencies:
```bash
npm install
```

3. Cấu hình file `.env`:
```env
DB_HOST=localhost
DB_PORT=5432
DB_NAME=lazabee_db
DB_USER=your_username
DB_PASSWORD=your_password
JWT_SECRET=your_jwt_secret_key
JWT_EXPIRES_IN=7d
PORT=8080
EMAIL_HOST=smtp.gmail.com
EMAIL_PORT=587
EMAIL_USER=your_email@gmail.com
EMAIL_PASSWORD=your_app_password
```

4. Chạy server:
```bash
npm run dev
```

### 3. Android App Setup

1. Mở project trong Android Studio
2. Cập nhật URL API trong `ApiClient.java`:
```java
private static final String BASE_URL = "http://your-server-ip:8080/api/";
```

3. Sync project và build

## Tính năng đã hoàn thành

### Authentication
- ✅ Đăng nhập
- ✅ Đăng ký
- ✅ Quên mật khẩu
- ✅ Xác nhận mã OTP
- ✅ Đặt lại mật khẩu
- ✅ Đăng xuất

### Architecture
- ✅ MVVM Pattern
- ✅ Repository Pattern
- ✅ Room Database (Local caching)
- ✅ Retrofit (API calls)
- ✅ RxJava (Reactive programming)
- ✅ LiveData & ViewModel
- ✅ Encrypted SharedPreferences

### Backend
- ✅ RESTful API
- ✅ PostgreSQL Database
- ✅ JWT Authentication
- ✅ Password Hashing
- ✅ Email Service (Password Reset)
- ✅ Input Validation

## API Endpoints

### Authentication
- `POST /api/auth/register` - Đăng ký
- `POST /api/auth/login` - Đăng nhập
- `POST /api/auth/forgot-password` - Quên mật khẩu
- `POST /api/auth/verify-code` - Xác nhận mã
- `POST /api/auth/reset-password` - Đặt lại mật khẩu
- `POST /api/auth/logout` - Đăng xuất

### Health Check
- `GET /api/health` - Kiểm tra trạng thái server

## Database Schema

### Users Table
```sql
CREATE TABLE users (
    id SERIAL PRIMARY KEY,
    username VARCHAR(50) UNIQUE NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    full_name VARCHAR(100) NOT NULL,
    phone VARCHAR(20),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
```

### Password Resets Table
```sql
CREATE TABLE password_resets (
    id SERIAL PRIMARY KEY,
    email VARCHAR(100) NOT NULL,
    code VARCHAR(6) NOT NULL,
    expires_at TIMESTAMP NOT NULL,
    used BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
```

## Công nghệ sử dụng

### Android
- Java
- MVVM Architecture
- Room Database
- Retrofit
- RxJava
- Glide (Image loading)
- Material Design

### Backend
- Node.js
- Express.js
- PostgreSQL
- JWT
- bcryptjs
- Nodemailer

## Lưu ý cho Team

1. **Code Style**: Tuân thủ Java naming conventions
2. **Git Workflow**: Sử dụng feature branches
3. **Testing**: Thêm unit tests cho ViewModels và Repositories
4. **Security**: Không commit file `.env` và API keys
5. **Documentation**: Cập nhật README khi thêm tính năng mới

## Tính năng sắp tới

- [ ] Quản lý sản phẩm
- [ ] Giỏ hàng
- [ ] Thanh toán
- [ ] Đánh giá sản phẩm
- [ ] Push notifications
- [ ] Social login (Google, Facebook)

## Liên hệ

Nếu có vấn đề gì, vui lòng tạo issue hoặc liên hệ team lead.