# Hướng Dẫn Triển Khai Backend Spring Boot - Labee E-Commerce

## 📋 Tổng Quan

File này hướng dẫn chi tiết từ **Giai đoạn 2 đến Giai đoạn 4** để bạn (Hoàng) xây dựng backend cho dự án Labee theo best practices.

**✅ Giai đoạn 1 đã hoàn thành:**

- Dự án Spring Boot đã được tạo tại `labee-backend/`
- Kết nối database MySQL qua Docker đã thành công
- File `application.properties` đã cấu hình đúng

---

## 🎯 Giai đoạn 2: Xây dựng Domain Model (Entity Classes)

### 2.1. Cấu trúc thư mục chuẩn

Tạo cấu trúc package trong `labee-backend/src/main/java/com/labee/`:

```
com.labee/
├── config/           # Cấu hình Spring (Security, CORS, etc.)
├── model/            # Entity classes (JPA)
│   ├── entity/       # Các class entity chính
│   └── enums/        # Các enum (OrderStatus, Role, etc.)
├── repository/       # JPA Repository interfaces
├── service/          # Business Logic Layer
│   └── impl/         # Implementation của các service
├── controller/       # REST Controllers
├── dto/              # Data Transfer Objects
│   ├── request/      # Request DTOs
│   └── response/     # Response DTOs
├── security/         # JWT, UserDetails, etc.
├── exception/        # Custom Exceptions
└── util/             # Utility classes
```

### 2.2. Tạo các Entity Classes

#### 📌 **Bước 1: Tạo các Enum trước**

**File: `model/enums/OrderStatus.java`**

```java
package com.labee.model.enums;

public enum OrderStatus {
    PENDING,         // Chờ xác nhận
    CONFIRMED,       // Đã xác nhận
    PROCESSING,      // Đang xử lý
    SHIPPING,        // Đang giao
    DELIVERED,       // Đã giao
    CANCELLED,       // Đã hủy
    REFUNDED        // Đã hoàn tiền
}
```

**File: `model/enums/Role.java`**

```java
package com.labee.model.enums;

public enum Role {
    USER,
    ADMIN
}
```

**File: `model/enums/PaymentMethod.java`**

```java
package com.labee.model.enums;

public enum PaymentMethod {
    COD,              // Cash on Delivery
    BANK_TRANSFER,    // Chuyển khoản
    CREDIT_CARD,      // Thẻ tín dụng
    E_WALLET          // Ví điện tử
}
```

---

#### 📌 **Bước 2: Entity - User.java**

**File: `model/entity/User.java`**

```java
package com.labee.model.entity;

import com.labee.model.enums.Role;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "user_id")
    private String userId;

    @Column(nullable = false, unique = true, length = 100)
    private String email;

    @Column(nullable = false)
    private String password; // Đã mã hóa bằng BCrypt

    @Column(nullable = false, length = 100)
    private String fullName;

    @Column(length = 15)
    private String phoneNumber;

    @Column(length = 500)
    private String avatarUrl;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Builder.Default
    private Role role = Role.USER;

    @Column(nullable = false)
    @Builder.Default
    private Boolean isActive = true;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    // Relationships
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<Address> addresses = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<Order> orders = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<CartItem> cartItems = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<Favorite> favorites = new ArrayList<>();
}
```

---

#### 📌 **Bước 3: Entity - Category.java**

**File: `model/entity/Category.java`**

```java
package com.labee.model.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "categories")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "category_id")
    private String categoryId;

    @Column(nullable = false, unique = true, length = 100)
    private String name;

    @Column(length = 500)
    private String imageUrl;

    @Column(columnDefinition = "TEXT")
    private String description;

    // Relationships
    @OneToMany(mappedBy = "category", cascade = CascadeType.ALL)
    @Builder.Default
    private List<Product> products = new ArrayList<>();
}
```

---

#### 📌 **Bước 4: Entity - Product.java**

**File: `model/entity/Product.java`**

```java
package com.labee.model.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "products")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "product_id")
    private String productId;

    @Column(nullable = false, length = 200)
    private String name;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal price;

    @Column(precision = 12, scale = 2)
    private BigDecimal originalPrice; // Giá gốc (trước khi giảm)

    @Column(nullable = false)
    @Builder.Default
    private Integer stockQuantity = 0;

    @Column(length = 500)
    private String imageUrl;

    @Column(precision = 3, scale = 2)
    @Builder.Default
    private BigDecimal rating = BigDecimal.ZERO;

    @Column
    @Builder.Default
    private Integer reviewCount = 0;

    @Column(nullable = false)
    @Builder.Default
    private Boolean isActive = true;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    // Relationships
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
    @Builder.Default
    private List<CartItem> cartItems = new ArrayList<>();

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
    @Builder.Default
    private List<Favorite> favorites = new ArrayList<>();

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
    @Builder.Default
    private List<OrderItem> orderItems = new ArrayList<>();
}
```

---

#### 📌 **Bước 5: Entity - Address.java**

**File: `model/entity/Address.java`**

```java
package com.labee.model.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "addresses")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Address {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "address_id")
    private String addressId;

    @Column(nullable = false, length = 100)
    private String recipientName;

    @Column(nullable = false, length = 15)
    private String phoneNumber;

    @Column(nullable = false, length = 300)
    private String addressLine;

    @Column(length = 100)
    private String ward; // Phường/Xã

    @Column(length = 100)
    private String district; // Quận/Huyện

    @Column(nullable = false, length = 100)
    private String city; // Thành phố/Tỉnh

    @Column(nullable = false)
    @Builder.Default
    private Boolean isDefault = false;

    // Relationships
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
}
```

---

#### 📌 **Bước 6: Entity - Order.java**

**File: `model/entity/Order.java`**

```java
package com.labee.model.entity;

import com.labee.model.enums.OrderStatus;
import com.labee.model.enums.PaymentMethod;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "orders")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "order_id")
    private String orderId;

    @Column(nullable = false, unique = true, length = 50)
    private String orderNumber; // Mã đơn hàng hiển thị (ORD-20231105-001)

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Builder.Default
    private OrderStatus status = OrderStatus.PENDING;

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal totalAmount;

    @Column(nullable = false, precision = 12, scale = 2)
    @Builder.Default
    private BigDecimal shippingFee = BigDecimal.ZERO;

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal finalAmount; // totalAmount + shippingFee

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PaymentMethod paymentMethod;

    @Column(nullable = false)
    @Builder.Default
    private Boolean isPaid = false;

    // Shipping Information (snapshot từ Address)
    @Column(nullable = false, length = 100)
    private String shippingName;

    @Column(nullable = false, length = 15)
    private String shippingPhone;

    @Column(nullable = false, length = 500)
    private String shippingAddress;

    @Column(columnDefinition = "TEXT")
    private String note;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    // Relationships
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<OrderItem> orderItems = new ArrayList<>();
}
```

---

#### 📌 **Bước 7: Entity - OrderItem.java**

**File: `model/entity/OrderItem.java`**

```java
package com.labee.model.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "order_items")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "order_item_id")
    private String orderItemId;

    @Column(nullable = false)
    private Integer quantity;

    // Snapshot giá tại thời điểm đặt hàng
    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal priceAtOrder;

    // Snapshot thông tin sản phẩm
    @Column(nullable = false, length = 200)
    private String productName;

    @Column(length = 500)
    private String productImageUrl;

    // Relationships
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;
}
```

---

#### 📌 **Bước 8: Entity - CartItem.java**

**File: `model/entity/CartItem.java`**

```java
package com.labee.model.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "cart_items")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CartItem {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "cart_item_id")
    private String cartItemId;

    @Column(nullable = false)
    private Integer quantity;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    // Relationships
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;
}
```

---

#### 📌 **Bước 9: Entity - Favorite.java**

**File: `model/entity/Favorite.java`**

```java
package com.labee.model.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "favorites")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Favorite {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "favorite_id")
    private String favoriteId;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    // Relationships
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;
}
```

---

#### 📌 **Bước 10: Entity - Banner.java**

**File: `model/entity/Banner.java`**

```java
package com.labee.model.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "banners")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Banner {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "banner_id")
    private String bannerId;

    @Column(nullable = false, length = 500)
    private String imageUrl;

    @Column(length = 500)
    private String targetUrl; // URL khi click vào banner

    @Column(nullable = false)
    @Builder.Default
    private Integer displayOrder = 0;

    @Column(nullable = false)
    @Builder.Default
    private Boolean isActive = true;
}
```

---

### 2.3. Tạo các Repository Interfaces

Tạo các interface trong package `repository/`:

#### **File: `repository/UserRepository.java`**

```java
package com.labee.repository;

import com.labee.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, String> {

    Optional<User> findByEmail(String email);

    boolean existsByEmail(String email);
}
```

#### **File: `repository/CategoryRepository.java`**

```java
package com.labee.repository;

import com.labee.model.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends JpaRepository<Category, String> {
}
```

#### **File: `repository/ProductRepository.java`**

```java
package com.labee.repository;

import com.labee.model.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product, String> {

    Page<Product> findByCategoryIdAndIsActiveTrue(String categoryId, Pageable pageable);

    Page<Product> findByIsActiveTrue(Pageable pageable);
}
```

#### **File: `repository/AddressRepository.java`**

```java
package com.labee.repository;

import com.labee.model.entity.Address;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AddressRepository extends JpaRepository<Address, String> {

    List<Address> findByUserId(String userId);

    Optional<Address> findByUserIdAndIsDefaultTrue(String userId);
}
```

#### **File: `repository/OrderRepository.java`**

```java
package com.labee.repository;

import com.labee.model.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, String> {

    List<Order> findByUserIdOrderByCreatedAtDesc(String userId);
}
```

#### **File: `repository/CartItemRepository.java`**

```java
package com.labee.repository;

import com.labee.model.entity.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, String> {

    List<CartItem> findByUserId(String userId);

    Optional<CartItem> findByUserIdAndProductId(String userId, String productId);

    void deleteByUserId(String userId);
}
```

#### **File: `repository/BannerRepository.java`**

```java
package com.labee.repository;

import com.labee.model.entity.Banner;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BannerRepository extends JpaRepository<Banner, String> {

    List<Banner> findByIsActiveTrueOrderByDisplayOrderAsc();
}
```

---

### ✅ Checkpoint Giai đoạn 2

**Sau khi hoàn thành, hãy:**

1. **Chạy ứng dụng:** `mvn spring-boot:run` hoặc chạy từ IDE
2. **Kiểm tra database:** Mở DBeaver và xem các bảng đã được tự động tạo
3. **Kiểm tra cấu trúc:** Đảm bảo các bảng có đúng cột và foreign key

---

## 🎯 Giai đoạn 3: Định nghĩa API Contract (DTOs)

### 3.1. Tạo các Request DTOs

#### **File: `dto/request/RegisterRequest.java`**

```java
package com.labee.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class RegisterRequest {

    @NotBlank(message = "Email không được để trống")
    @Email(message = "Email không hợp lệ")
    private String email;

    @NotBlank(message = "Mật khẩu không được để trống")
    @Size(min = 6, message = "Mật khẩu phải có ít nhất 6 ký tự")
    private String password;

    @NotBlank(message = "Họ tên không được để trống")
    private String fullName;

    private String phoneNumber;
}
```

#### **File: `dto/request/LoginRequest.java`**

```java
package com.labee.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class LoginRequest {

    @NotBlank(message = "Email không được để trống")
    @Email(message = "Email không hợp lệ")
    private String email;

    @NotBlank(message = "Mật khẩu không được để trống")
    private String password;
}
```

#### **File: `dto/request/AddToCartRequest.java`**

```java
package com.labee.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class AddToCartRequest {

    @NotBlank(message = "Product ID không được để trống")
    private String productId;

    @Min(value = 1, message = "Số lượng phải lớn hơn 0")
    private Integer quantity;
}
```

#### **File: `dto/request/PlaceOrderRequest.java`**

```java
package com.labee.dto.request;

import com.labee.model.enums.PaymentMethod;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class PlaceOrderRequest {

    @NotBlank(message = "Address ID không được để trống")
    private String addressId;

    @NotNull(message = "Phương thức thanh toán không được để trống")
    private PaymentMethod paymentMethod;

    private String note;
}
```

---

### 3.2. Tạo các Response DTOs

#### **File: `dto/response/AuthResponse.java`**

```java
package com.labee.dto.response;

import com.labee.model.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuthResponse {
    private String token;
    private String userId;
    private String email;
    private String fullName;
    private Role role;
}
```

#### **File: `dto/response/ProductResponse.java`**

```java
package com.labee.dto.response;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class ProductResponse {
    private String productId;
    private String name;
    private String description;
    private BigDecimal price;
    private BigDecimal originalPrice;
    private Integer stockQuantity;
    private String imageUrl;
    private BigDecimal rating;
    private Integer reviewCount;
    private String categoryId;
    private String categoryName;
}
```

#### **File: `dto/response/CartItemResponse.java`**

```java
package com.labee.dto.response;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class CartItemResponse {
    private String cartItemId;
    private String productId;
    private String productName;
    private String productImageUrl;
    private BigDecimal price;
    private Integer quantity;
    private BigDecimal subtotal; // price * quantity
}
```

#### **File: `dto/response/ApiResponse.java`**

```java
package com.labee.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ApiResponse<T> {
    private boolean success;
    private String message;
    private T data;

    public static <T> ApiResponse<T> success(T data) {
        return ApiResponse.<T>builder()
                .success(true)
                .data(data)
                .build();
    }

    public static <T> ApiResponse<T> success(String message, T data) {
        return ApiResponse.<T>builder()
                .success(true)
                .message(message)
                .data(data)
                .build();
    }

    public static <T> ApiResponse<T> error(String message) {
        return ApiResponse.<T>builder()
                .success(false)
                .message(message)
                .build();
    }
}
```

---

## 🎯 Giai đoạn 4: Hiện thực Business Logic & REST API

### 4.1. Thứ tự triển khai (từ dễ đến khó)

1. ✅ **Banner API** (Đơn giản nhất - không cần auth)
2. ✅ **Category API** (Đơn giản - không cần auth)
3. ✅ **Product API** (Trung bình - hỗ trợ pagination)
4. ✅ **Authentication API** (Khó - JWT + Spring Security)
5. ✅ **Cart API** (Cần auth)
6. ✅ **Order API** (Khó nhất - Transaction + Snapshot)

---

### 4.2. Triển khai Banner & Category (APIs đơn giản)

#### **File: `service/BannerService.java`**

```java
package com.labee.service;

import com.labee.model.entity.Banner;
import com.labee.repository.BannerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BannerService {

    private final BannerRepository bannerRepository;

    @Transactional(readOnly = true)
    public List<Banner> getAllActiveBanners() {
        return bannerRepository.findByIsActiveTrueOrderByDisplayOrderAsc();
    }
}
```

#### **File: `controller/BannerController.java`**

```java
package com.labee.controller;

import com.labee.dto.response.ApiResponse;
import com.labee.model.entity.Banner;
import com.labee.service.BannerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/banners")
@RequiredArgsConstructor
public class BannerController {

    private final BannerService bannerService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<Banner>>> getAllBanners() {
        List<Banner> banners = bannerService.getAllActiveBanners();
        return ResponseEntity.ok(ApiResponse.success(banners));
    }
}
```

**Tương tự cho Category** - Tạo `CategoryService` và `CategoryController`.

---

### 4.3. Triển khai Product API (Với Pagination)

#### **File: `service/ProductService.java`**

```java
package com.labee.service;

import com.labee.dto.response.ProductResponse;
import com.labee.exception.ResourceNotFoundException;
import com.labee.model.entity.Product;
import com.labee.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    @Transactional(readOnly = true)
    public Page<ProductResponse> getProducts(String categoryId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());

        Page<Product> products;
        if (categoryId != null && !categoryId.isEmpty()) {
            products = productRepository.findByCategoryIdAndIsActiveTrue(categoryId, pageable);
        } else {
            products = productRepository.findByIsActiveTrue(pageable);
        }

        return products.map(this::mapToResponse);
    }

    @Transactional(readOnly = true)
    public ProductResponse getProductById(String productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy sản phẩm"));

        return mapToResponse(product);
    }

    private ProductResponse mapToResponse(Product product) {
        return ProductResponse.builder()
                .productId(product.getProductId())
                .name(product.getName())
                .description(product.getDescription())
                .price(product.getPrice())
                .originalPrice(product.getOriginalPrice())
                .stockQuantity(product.getStockQuantity())
                .imageUrl(product.getImageUrl())
                .rating(product.getRating())
                .reviewCount(product.getReviewCount())
                .categoryId(product.getCategory().getCategoryId())
                .categoryName(product.getCategory().getName())
                .build();
    }
}
```

#### **File: `controller/ProductController.java`**

```java
package com.labee.controller;

import com.labee.dto.response.ApiResponse;
import com.labee.dto.response.ProductResponse;
import com.labee.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @GetMapping
    public ResponseEntity<ApiResponse<Page<ProductResponse>>> getProducts(
            @RequestParam(required = false) String categoryId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size
    ) {
        Page<ProductResponse> products = productService.getProducts(categoryId, page, size);
        return ResponseEntity.ok(ApiResponse.success(products));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ProductResponse>> getProductById(@PathVariable String id) {
        ProductResponse product = productService.getProductById(id);
        return ResponseEntity.ok(ApiResponse.success(product));
    }
}
```

---

### 4.4. Custom Exception Handler

#### **File: `exception/ResourceNotFoundException.java`**

```java
package com.labee.exception;

public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String message) {
        super(message);
    }
}
```

#### **File: `exception/GlobalExceptionHandler.java`**

```java
package com.labee.exception;

import com.labee.dto.response.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiResponse<Void>> handleResourceNotFound(ResourceNotFoundException ex) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(ApiResponse.error(ex.getMessage()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Map<String, String>>> handleValidationExceptions(
            MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse.error("Dữ liệu không hợp lệ"));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Void>> handleGeneralException(Exception ex) {
        ex.printStackTrace();
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse.error("Có lỗi xảy ra: " + ex.getMessage()));
    }
}
```

---

### 4.5. CORS Configuration

#### **File: `config/WebConfig.java`**

```java
package com.labee.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/api/**")
                .allowedOrigins("*")
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowedHeaders("*");
    }
}
```

---

## 🧪 Test APIs với Postman

### Test Banner API

```
GET http://localhost:8080/api/v1/banners
```

### Test Category API

```
GET http://localhost:8080/api/v1/categories
```

### Test Product API

```
GET http://localhost:8080/api/v1/products?page=0&size=20
GET http://localhost:8080/api/v1/products?categoryId=xxx&page=0&size=20
GET http://localhost:8080/api/v1/products/{id}
```

---

## 📝 Tiếp theo: Authentication & Security (JWT)

Đây là phần khó nhất và quan trọng nhất. Bạn sẽ cần:

1. **Thêm dependencies vào `pom.xml`:**

   ```xml
   <dependency>
       <groupId>io.jsonwebtoken</groupId>
       <artifactId>jjwt-api</artifactId>
       <version>0.12.3</version>
   </dependency>
   <dependency>
       <groupId>io.jsonwebtoken</groupId>
       <artifactId>jjwt-impl</artifactId>
       <version>0.12.3</version>
       <scope>runtime</scope>
   </dependency>
   <dependency>
       <groupId>io.jsonwebtoken</groupId>
       <artifactId>jjwt-jackson</artifactId>
       <version>0.12.3</version>
       <scope>runtime</scope>
   </dependency>
   ```

2. **Tạo JWT Utility**
3. **Cấu hình Spring Security**
4. **Triển khai Authentication Service**

---

## 📚 Resources & Best Practices

### Best Practices đã áp dụng:

1. ✅ **Sử dụng UUID cho Primary Key** (an toàn hơn auto-increment)
2. ✅ **Lombok** để giảm boilerplate code
3. ✅ **@Transactional** cho data consistency
4. ✅ **DTO Pattern** để tách biệt Entity và API response
5. ✅ **Exception Handling** tập trung
6. ✅ **Pagination** cho danh sách lớn
7. ✅ **Builder Pattern** cho Entity creation
8. ✅ **@CreationTimestamp/@UpdateTimestamp** tự động
9. ✅ **Fetch Type LAZY** để tối ưu performance
10. ✅ **API Versioning** (/api/v1/...)

---

## 🎯 Checklist

- [ ] Tạo tất cả Entity classes
- [ ] Tạo tất cả Repository interfaces
- [ ] Chạy app và kiểm tra database schema
- [ ] Tạo các DTO classes
- [ ] Implement Banner/Category APIs
- [ ] Implement Product API
- [ ] Test APIs với Postman
- [ ] Implement JWT Authentication
- [ ] Implement Cart APIs
- [ ] Implement Order APIs (Transaction)

---

**Lưu ý quan trọng:**

- Làm từng bước một, test kỹ trước khi sang bước tiếp theo
- Luôn kiểm tra database sau mỗi thay đổi
- Sử dụng Postman để test API trước khi đưa cho frontend
- Đọc error logs cẩn thận khi gặp lỗi

Chúc bạn thành công! 🚀
