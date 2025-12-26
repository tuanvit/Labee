<p align="center">
  <img src="Resources/shopping%20cart%20icon.jpg" alt="Labee Logo" width="120" height="120">
</p>

<h1 align="center">🐝 Labee - E-Commerce Android App</h1>

<p align="center">
  <strong>A full-featured E-commerce Android application built with MVC architecture and Room Database</strong>
</p>

<p align="center">
  <a href="#-features">Features</a> •
  <a href="#-screenshots">Screenshots</a> •
  <a href="#-installation">Installation</a> •
  <a href="#-architecture">Architecture</a> •
  <a href="#-contributing">Contributing</a> •
  <a href="#-license">License</a>
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
  <img src="https://img.shields.io/badge/Build-Passing-brightgreen?style=flat-square" alt="Build">
  <img src="https://img.shields.io/badge/PRs-Welcome-brightgreen?style=flat-square" alt="PRs Welcome">
</p>

---

## 📋 Table of Contents

- [Overview](#-overview)
- [Features](#-features)
- [Tech Stack](#-tech-stack)
- [Project Structure](#-project-structure)
- [Database Schema](#-database-schema)
- [Installation](#-installation)
- [Usage Guide](#-usage-guide)
- [API Documentation](#-api-documentation)
- [Contributing](#-contributing)
- [Code of Conduct](#-code-of-conduct)
- [License](#-license)
- [Contributors](#-contributors)

---

## 🎯 Overview

**Labee** is a comprehensive e-commerce Android application developed in Java, providing a complete online shopping experience for customers and efficient management tools for administrators.

### Target Users:
- **👤 Customers**: Browse, search, purchase products and manage orders
- **👨‍💼 Administrators**: Manage products, orders, and view revenue statistics

---

## ✨ Features

### 🔐 Authentication

| Feature | Description | Activity |
|---------|-------------|----------|
| Login | Email/Password with Admin/Customer role separation | `LoginActivity` |
| Register | Create new user account | `RegisterActivity` |
| Forgot Password | Request password reset | `ForgotPasswordActivity` |
| OTP Verification | Verify OTP code via email | `VerifyCodeActivity` |
| Reset Password | Create new password | `CreateNewPasswordActivity` |

### 🛒 Customer Features

<details>
<summary><b>🏠 Home & Products</b></summary>

| Feature | Description |
|---------|-------------|
| Promotional Banner | Auto-scroll banner (3s interval) |
| Product Categories | Electronics, Fashion, Home, Sports, Beauty |
| Search | Search products by name |
| Product Details | View images, price, description, stock |
| View All | Grid view of all products |

</details>

<details>
<summary><b>🛒 Cart & Checkout</b></summary>

| Feature | Description |
|---------|-------------|
| Add to Cart | Add products with quantity |
| Cart Management | Increase/decrease/remove items |
| Checkout | Select address, payment method |
| Place Order | Confirm and create order |

</details>

<details>
<summary><b>📦 Order Management</b></summary>

| Feature | Description |
|---------|-------------|
| Order History | View list of placed orders |
| Order Details | Product info, address, status |
| Cancel Order | Cancel orders with Pending status |

</details>

<details>
<summary><b>📍 Address Management</b></summary>

| Feature | Description |
|---------|-------------|
| Address List | View saved addresses |
| CRUD Operations | Add/Edit/Delete addresses |
| Default Address | Set default shipping address |

</details>

<details>
<summary><b>🎫 Vouchers & Promotions</b></summary>

| Feature | Description |
|---------|-------------|
| Voucher List | View available discount codes |
| Collect Voucher | Save codes to voucher wallet |

</details>

### 👨‍💼 Admin Features

| Feature | Description | Activity |
|---------|-------------|----------|
| Dashboard | Admin home with navigation | `AdminHomeActivity` |
| Product Management | CRUD operations for products | `AdminProductListActivity`, `AdminProductEditActivity` |
| Order Management | View and update order status | `AdminOrderListActivity`, `AdminOrderDetailActivity` |
| Revenue Statistics | Revenue reports and order counts | `AdminRevenueActivity` |

---

## 🛠 Tech Stack

### Core Technologies
| Technology | Version | Description |
|------------|---------|-------------|
| **Java** | 11 | Primary programming language |
| **Android SDK** | 36 | Target SDK version |
| **Gradle** | 8.12.3 | Build automation system |

### Libraries & Frameworks
| Library | Version | Purpose |
|---------|---------|---------|
| **Room Database** | 2.6.1 | Local SQLite database |
| **Material Design** | 1.13.0 | UI Components |
| **RecyclerView** | 1.3.0 | List display |
| **ViewPager2** | 1.1.0 | Banner slider |
| **CardView** | 1.0.0 | Card UI elements |
| **Glide** | 4.15.1 | Image loading & caching |
| **Gson** | 2.10.1 | JSON serialization |
| **Google Play Services Auth** | 20.7.0 | Google Sign-In |

---



---

## 🗄 Database Schema

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

### Table Definitions

<details>
<summary><b>📊 Detailed Table Schemas</b></summary>

#### Users Table
| Column | Type | Description |
|--------|------|-------------|
| id | INTEGER | Primary Key, Auto Increment |
| email | TEXT | Unique, Login email |
| password | TEXT | User password |
| fullName | TEXT | Full name |
| phone | TEXT | Phone number |
| address | TEXT | Address |
| role | TEXT | "admin" / "customer" |

#### Products Table
| Column | Type | Description |
|--------|------|-------------|
| id | INTEGER | Primary Key, Auto Increment |
| name | TEXT | Product name |
| price | INTEGER | Price (VND) |
| description | TEXT | Product description |
| imageResName | TEXT | Drawable resource name |
| category | TEXT | Product category |
| stock | INTEGER | Stock quantity |

#### Orders Table
| Column | Type | Description |
|--------|------|-------------|
| id | INTEGER | Primary Key, Auto Increment |
| userId | INTEGER | FK → users.id |
| totalPrice | INTEGER | Total amount |
| date | TEXT | Order date |
| status | TEXT | Pending/Shipping/Completed/Cancelled |
| address | TEXT | Shipping address |
| phoneNumber | TEXT | Contact phone |
| paymentMethod | TEXT | COD/Bank/Card/EWallet |
| note | TEXT | Order notes |

#### Order Items Table
| Column | Type | Description |
|--------|------|-------------|
| id | INTEGER | Primary Key, Auto Increment |
| orderId | INTEGER | FK → orders.id |
| productId | INTEGER | FK → products.id |
| quantity | INTEGER | Item quantity |
| price | INTEGER | Price at purchase time |

#### Cart Items Table
| Column | Type | Description |
|--------|------|-------------|
| id | INTEGER | Primary Key, Auto Increment |
| userId | INTEGER | FK → users.id |
| productId | INTEGER | FK → products.id |
| quantity | INTEGER | Item quantity |

#### Addresses Table
| Column | Type | Description |
|--------|------|-------------|
| id | INTEGER | Primary Key, Auto Increment |
| userId | INTEGER | FK → users.id |
| name | TEXT | Recipient name |
| phone | TEXT | Recipient phone |
| address | TEXT | Full address |
| isDefault | BOOLEAN | Default address flag |

</details>

---

## 🚀 Installation

### Prerequisites

- **Android Studio**: Arctic Fox (2020.3.1) or later
- **JDK**: 11 or higher
- **Android SDK**: API 24+ (Android 7.0 Nougat)
- **Gradle**: 8.0+

### Setup Instructions

1. **Clone the repository**
   ```bash
   git clone https://github.com/your-username/labee.git
   cd labee
   ```

2. **Open in Android Studio**
   ```
   File → Open → Select project folder
   ```

3. **Sync Gradle**
   ```
   File → Sync Project with Gradle Files
   ```

4. **Build & Run**
   ```
   Run → Run 'app' or press Shift + F10
   ```

### Test Accounts

| Role | Email | Password |
|------|-------|----------|
| Admin | admin@labee.com | admin123 |
| Customer | user@labee.com | user123 |

---

## 📖 Usage Guide

### For Customers

1. **Register/Login** → Create account or sign in
2. **Home Screen** → Browse products by category or search
3. **Product Details** → View info and add to cart
4. **Cart** → Review and adjust order
5. **Checkout** → Select address, payment method
6. **Track Orders** → View status in order history

### For Administrators

1. **Login** with Admin account
2. **Dashboard** → Access management features
3. **Product Management** → Add/Edit/Delete products
4. **Order Management** → Update order status
5. **Statistics** → View revenue reports

---

## 📚 API Documentation

For detailed API documentation, see: [docs/API-DOCUMENTATION.md](docs/API-DOCUMENTATION.md)

### Quick Reference

| Endpoint | Method | Description |
|----------|--------|-------------|
| `/api/v1/auth/login` | POST | User login |
| `/api/v1/auth/register` | POST | User registration |
| `/api/v1/products` | GET | Get product list |
| `/api/v1/cart` | GET/POST | Cart operations |
| `/api/v1/orders` | GET/POST | Order operations |

---

## 🤝 Contributing

We welcome contributions! Please see our [Contributing Guidelines](CONTRIBUTING.md) for details on how to submit pull requests, report issues, and request features.

---

## 📜 Code of Conduct

Please note that this project is released with a [Code of Conduct](Code of conduct.md). By participating in this project you agree to abide by its terms.

---

## 📄 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE.md) file for details.

---

## 👥 Contributors

<table>
  <tr>
    <td align="center">
      <a href="https://github.com/your-username">
        <img src="https://via.placeholder.com/100" width="100px;" alt="Team Avatar"/>
        <br />
        <sub><b>Labee Team</b></sub>
      </a>
      <br />
      <sub>Project Lead</sub>
    </td>
  </tr>
</table>

---

## 🙏 Acknowledgments

- [Android Developers](https://developer.android.com/) - Official documentation & guides
- [Material Design](https://material.io/) - Design system & components
- [Glide](https://github.com/bumptech/glide) - Image loading library
- [Room Database](https://developer.android.com/training/data-storage/room) - SQLite abstraction

---

## 📞 Contact

- **Email**: labee.team@example.com
- **Issues**: [GitHub Issues](https://github.com/your-username/labee/issues)
- **Discussions**: [GitHub Discussions](https://github.com/your-username/labee/discussions)

---

<p align="center">
  Made with ❤️ by Labee Team
</p>

<p align="center">
  <a href="#-labee---e-commerce-android-app">⬆ Back to Top</a>
</p>
