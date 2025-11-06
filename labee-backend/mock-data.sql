-- =====================================================
-- Labee E-Commerce - Mock Data SQL Script (English)
-- =====================================================
-- Run this script to insert test data into database
-- =====================================================

USE labee_db;

-- Clear old data (if exists)
SET FOREIGN_KEY_CHECKS = 0;
TRUNCATE TABLE order_items;
TRUNCATE TABLE orders;
TRUNCATE TABLE cart_items;
TRUNCATE TABLE favorites;
TRUNCATE TABLE addresses;
TRUNCATE TABLE products;
TRUNCATE TABLE categories;
TRUNCATE TABLE banners;
TRUNCATE TABLE users;
SET FOREIGN_KEY_CHECKS = 1;

-- =====================================================
-- 1. INSERT CATEGORIES
-- =====================================================
INSERT INTO categories (category_id, name, image_url, description) VALUES
('cat-001', 'Smartphones', 'https://images.unsplash.com/photo-1511707171634-5f897ff02aa9?w=400', 'Latest smartphones and mobile devices'),
('cat-002', 'Laptops', 'https://images.unsplash.com/photo-1496181133206-80ce9b88a853?w=400', 'Laptops and portable computers'),
('cat-003', 'Tablets', 'https://images.unsplash.com/photo-1561154464-82e9adf32764?w=400', 'Tablets, iPad and Samsung tablets'),
('cat-004', 'Accessories', 'https://images.unsplash.com/photo-1572635196237-14b3f281503f?w=400', 'Headphones, chargers, cases and more'),
('cat-005', 'Smartwatches', 'https://images.unsplash.com/photo-1523275335684-37898b6baf30?w=400', 'Smart watches and wearables'),
('cat-006', 'Gaming', 'https://images.unsplash.com/photo-1593305841991-05c297ba4575?w=400', 'Gaming devices and consoles');

-- =====================================================
-- 2. INSERT BANNERS
-- =====================================================
INSERT INTO banners (banner_id, image_url, target_url, display_order, is_active) VALUES
('banner-001', 'https://images.unsplash.com/photo-1607082348824-0a96f2a4b9da?w=1200', '/products?categoryId=cat-001', 1, true),
('banner-002', 'https://images.unsplash.com/photo-1593642632823-8f785ba67e45?w=1200', '/products?categoryId=cat-002', 2, true),
('banner-003', 'https://images.unsplash.com/photo-1550745165-9bc0b252726f?w=1200', '/products?categoryId=cat-004', 3, true),
('banner-004', 'https://images.unsplash.com/photo-1434494878577-86c23bcb06b9?w=1200', '/sale', 4, true);

-- =====================================================
-- 3. INSERT PRODUCTS - Smartphones
-- =====================================================
INSERT INTO products (product_id, name, description, price, original_price, stock_quantity, image_url, rating, review_count, category_id, is_active, created_at, updated_at) VALUES
('prod-001', 'iPhone 15 Pro Max', 'iPhone 15 Pro Max 256GB - Natural Titanium. Powerful A17 Pro chip, 48MP camera', 29990000, 34990000, 50, 'https://images.unsplash.com/photo-1678652197950-1e66d2f66b31?w=500', 4.8, 125, 'cat-001', true, NOW(), NOW()),
('prod-002', 'Samsung Galaxy S24 Ultra', 'Samsung Galaxy S24 Ultra 512GB. Snapdragon 8 Gen 3, 200MP camera, built-in S Pen', 27990000, 31990000, 35, 'https://images.unsplash.com/photo-1610945415295-d9bbf067e59c?w=500', 4.7, 89, 'cat-001', true, NOW(), NOW()),
('prod-003', 'Xiaomi 14 Ultra', 'Xiaomi 14 Ultra - Leica Camera, Snapdragon 8 Gen 3, 2K AMOLED display', 24990000, 27990000, 45, 'https://images.unsplash.com/photo-1598327105666-5b89351aff97?w=500', 4.6, 67, 'cat-001', true, NOW(), NOW()),
('prod-004', 'OPPO Find X7 Pro', 'OPPO Find X7 Pro - Hasselblad Camera, 100W fast charging, premium design', 21990000, 24990000, 30, 'https://images.unsplash.com/photo-1511707171634-5f897ff02aa9?w=500', 4.5, 45, 'cat-001', true, NOW(), NOW()),
('prod-005', 'Google Pixel 8 Pro', 'Google Pixel 8 Pro - Pure AI experience, best-in-class camera, Tensor G3', 23990000, 26990000, 25, 'https://images.unsplash.com/photo-1598327105666-5b89351aff97?w=500', 4.7, 78, 'cat-001', true, NOW(), NOW());

-- =====================================================
-- 4. INSERT PRODUCTS - Laptops
-- =====================================================
INSERT INTO products (product_id, name, description, price, original_price, stock_quantity, image_url, rating, review_count, category_id, is_active, created_at, updated_at) VALUES
('prod-006', 'MacBook Pro 16" M3 Max', 'MacBook Pro 16 inch with M3 Max chip, 36GB RAM, 1TB SSD. Liquid Retina XDR display', 89990000, 99990000, 20, 'https://images.unsplash.com/photo-1517336714731-489689fd1ca8?w=500', 4.9, 156, 'cat-002', true, NOW(), NOW()),
('prod-007', 'Dell XPS 15', 'Dell XPS 15 - Intel Core i9-13900H, RTX 4070, 32GB RAM, 4K OLED display', 54990000, 59990000, 15, 'https://images.unsplash.com/photo-1496181133206-80ce9b88a853?w=500', 4.6, 92, 'cat-002', true, NOW(), NOW()),
('prod-008', 'ASUS ROG Zephyrus G16', 'ASUS ROG Zephyrus G16 - Thin and light gaming laptop, RTX 4090, Intel Core i9', 69990000, 74990000, 12, 'https://images.unsplash.com/photo-1603302576837-37561b2e2302?w=500', 4.8, 134, 'cat-002', true, NOW(), NOW()),
('prod-009', 'Lenovo ThinkPad X1 Carbon', 'ThinkPad X1 Carbon Gen 11 - Business laptop, ultra-light 1.1kg, Intel Core i7', 42990000, 47990000, 18, 'https://images.unsplash.com/photo-1588872657578-7efd1f1555ed?w=500', 4.7, 76, 'cat-002', true, NOW(), NOW()),
('prod-010', 'HP Spectre x360', 'HP Spectre x360 - 2-in-1 laptop, touchscreen display, Intel Core i7-1355U', 38990000, 42990000, 22, 'https://images.unsplash.com/photo-1525547719571-a2d4ac8945e2?w=500', 4.5, 58, 'cat-002', true, NOW(), NOW());

-- =====================================================
-- 5. INSERT PRODUCTS - Tablets
-- =====================================================
INSERT INTO products (product_id, name, description, price, original_price, stock_quantity, image_url, rating, review_count, category_id, is_active, created_at, updated_at) VALUES
('prod-011', 'iPad Pro 12.9" M2', 'iPad Pro 12.9 inch with M2 chip, Liquid Retina XDR display, 5G connectivity', 32990000, 36990000, 28, 'https://images.unsplash.com/photo-1544244015-0df4b3ffc6b0?w=500', 4.8, 112, 'cat-003', true, NOW(), NOW()),
('prod-012', 'Samsung Galaxy Tab S9 Ultra', 'Galaxy Tab S9 Ultra - 14.6 inch AMOLED display, free S Pen included', 28990000, 31990000, 20, 'https://images.unsplash.com/photo-1561154464-82e9adf32764?w=500', 4.7, 87, 'cat-003', true, NOW(), NOW()),
('prod-013', 'iPad Air M2', 'iPad Air with M2 chip, 11 inch display, supports Apple Pencil Pro', 16990000, 18990000, 35, 'https://images.unsplash.com/photo-1585790050230-5dd28404f869?w=500', 4.6, 95, 'cat-003', true, NOW(), NOW()),
('prod-014', 'Xiaomi Pad 6 Pro', 'Xiaomi Pad 6 Pro - Snapdragon 8+ Gen 1, 144Hz display, great value', 11990000, 13990000, 40, 'https://images.unsplash.com/photo-1611532736597-de2d4265fba3?w=500', 4.5, 63, 'cat-003', true, NOW(), NOW());

-- =====================================================
-- 6. INSERT PRODUCTS - Accessories
-- =====================================================
INSERT INTO products (product_id, name, description, price, original_price, stock_quantity, image_url, rating, review_count, category_id, is_active, created_at, updated_at) VALUES
('prod-015', 'AirPods Pro 2', 'AirPods Pro 2nd generation with USB-C, Active Noise Cancellation, 3D audio', 5990000, 6990000, 100, 'https://images.unsplash.com/photo-1606841837239-c5a1a4a07af7?w=500', 4.9, 234, 'cat-004', true, NOW(), NOW()),
('prod-016', 'Sony WH-1000XM5', 'Sony WH-1000XM5 - Best noise cancellation headphones, 30h battery life', 7990000, 8990000, 75, 'https://images.unsplash.com/photo-1546435770-a3e426bf472b?w=500', 4.8, 189, 'cat-004', true, NOW(), NOW()),
('prod-017', 'Anker PowerBank 20000mAh', 'Anker 737 PowerBank - 20000mAh capacity, 140W fast charging, 3 USB ports', 2990000, 3490000, 150, 'https://images.unsplash.com/photo-1609091839311-d5365f9ff1c5?w=500', 4.7, 267, 'cat-004', true, NOW(), NOW()),
('prod-018', 'Apple Magic Keyboard', 'Magic Keyboard for iPad Pro - Premium keyboard with built-in trackpad', 8990000, 9990000, 45, 'https://images.unsplash.com/photo-1587829741301-dc798b83add3?w=500', 4.6, 98, 'cat-004', true, NOW(), NOW()),
('prod-019', 'Logitech MX Master 3S', 'Logitech MX Master 3S - Premium wireless mouse, 8000 DPI precision', 2490000, 2990000, 80, 'https://images.unsplash.com/photo-1527864550417-7fd91fc51a46?w=500', 4.8, 156, 'cat-004', true, NOW(), NOW());

-- =====================================================
-- 7. INSERT PRODUCTS - Smartwatches
-- =====================================================
INSERT INTO products (product_id, name, description, price, original_price, stock_quantity, image_url, rating, review_count, category_id, is_active, created_at, updated_at) VALUES
('prod-020', 'Apple Watch Series 9', 'Apple Watch Series 9 - GPS + Cellular, Always-On Retina display', 11990000, 13990000, 55, 'https://images.unsplash.com/photo-1579586337278-3befd40fd17a?w=500', 4.8, 178, 'cat-005', true, NOW(), NOW()),
('prod-021', 'Samsung Galaxy Watch 6', 'Galaxy Watch 6 Classic - Rotating bezel, comprehensive health tracking', 7990000, 8990000, 60, 'https://images.unsplash.com/photo-1523275335684-37898b6baf30?w=500', 4.7, 142, 'cat-005', true, NOW(), NOW()),
('prod-022', 'Garmin Fenix 7X', 'Garmin Fenix 7X Sapphire Solar - Premium sports watch, 28-day battery life', 24990000, 27990000, 25, 'https://images.unsplash.com/photo-1508685096489-7aacd43bd3b1?w=500', 4.9, 89, 'cat-005', true, NOW(), NOW());

-- =====================================================
-- 8. INSERT PRODUCTS - Gaming
-- =====================================================
INSERT INTO products (product_id, name, description, price, original_price, stock_quantity, image_url, rating, review_count, category_id, is_active, created_at, updated_at) VALUES
('prod-023', 'PlayStation 5 Slim', 'PS5 Slim Digital Edition - 1TB SSD, new DualSense controller', 13990000, 15990000, 30, 'https://images.unsplash.com/photo-1606813907291-d86efa9b94db?w=500', 4.9, 456, 'cat-006', true, NOW(), NOW()),
('prod-024', 'Xbox Series X', 'Xbox Series X - 1TB storage, 4K gaming, Game Pass included', 13990000, 15990000, 28, 'https://images.unsplash.com/photo-1621259182978-fbf93132d53d?w=500', 4.8, 389, 'cat-006', true, NOW(), NOW()),
('prod-025', 'Nintendo Switch OLED', 'Nintendo Switch OLED - 7 inch OLED screen, 64GB internal storage', 8990000, 9990000, 45, 'https://images.unsplash.com/photo-1578303512597-81e6cc155b3e?w=500', 4.7, 278, 'cat-006', true, NOW(), NOW()),
('prod-026', 'Steam Deck', 'Steam Deck 512GB - Handheld PC gaming device, play all Steam games', 15990000, 17990000, 20, 'https://images.unsplash.com/photo-1593305841991-05c297ba4575?w=500', 4.6, 167, 'cat-006', true, NOW(), NOW());

-- =====================================================
-- 9. INSERT USERS (Test users)
-- =====================================================
-- Password: "password123" encrypted with BCrypt
-- You will need to replace with real passwords when implementing authentication
INSERT INTO users (user_id, email, password, full_name, phone_number, role, is_active, created_at, updated_at) VALUES
('user-001', 'admin@labee.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'Admin User', '0901234567', 'ADMIN', true, NOW(), NOW()),
('user-002', 'user@labee.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'John Doe', '0912345678', 'USER', true, NOW(), NOW()),
('user-003', 'test@labee.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'Jane Smith', '0923456789', 'USER', true, NOW(), NOW());

-- =====================================================
-- 10. INSERT ADDRESSES (Test addresses for user-002)
-- =====================================================
INSERT INTO addresses (address_id, recipient_name, phone_number, address_line, ward, district, city, is_default, user_id) VALUES
('addr-001', 'John Doe', '0912345678', '123 Nguyen Hue St', 'Ben Nghe Ward', 'District 1', 'Ho Chi Minh City', true, 'user-002'),
('addr-002', 'John Doe', '0912345678', '456 Le Loi St', 'Ben Thanh Ward', 'District 1', 'Ho Chi Minh City', false, 'user-002');

-- =====================================================
-- SCRIPT COMPLETED
-- =====================================================
-- Total records inserted:
-- - 6 Categories
-- - 4 Banners
-- - 26 Products (all categories covered)
-- - 3 Users (1 admin, 2 regular users)
-- - 2 Addresses

SELECT 'Mock data inserted successfully!' as Status;
SELECT COUNT(*) as 'Total Categories' FROM categories;
SELECT COUNT(*) as 'Total Products' FROM products;
SELECT COUNT(*) as 'Total Banners' FROM banners;
SELECT COUNT(*) as 'Total Users' FROM users;
