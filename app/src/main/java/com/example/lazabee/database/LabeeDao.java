package com.example.lazabee.database;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;
import androidx.room.Delete;

import com.example.lazabee.model.Address;
import com.example.lazabee.model.CartItem;
import com.example.lazabee.model.CartItemDetail;
import com.example.lazabee.model.Order;
import com.example.lazabee.model.OrderItem;
import com.example.lazabee.model.OrderItemDetail;
import com.example.lazabee.model.Product;
import com.example.lazabee.model.User;

import java.util.List;

@Dao
public interface LabeeDao {
        // User
        @Query("SELECT * FROM users WHERE id = :id")
        User getUserById(int id);

        @Query("SELECT * FROM users WHERE email = :email AND password = :password")
        User login(String email, String password);

        @Insert
        void register(User user);

        @Query("SELECT * FROM users WHERE email = :email")
        User checkUserExist(String email);

        // Product
        @Query("SELECT * FROM products")
        List<Product> getAllProducts();

        @Query("SELECT * FROM products LIMIT :limit OFFSET :offset")
        List<Product> getProducts(int limit, int offset);

        @Query("SELECT * FROM products WHERE name LIKE '%' || :keyword || '%'")
        List<Product> searchProducts(String keyword);

        @Query("SELECT * FROM products WHERE id = :id")
        Product getProductById(int id);

        @Insert
        void insertProduct(Product product);

        @Insert
        void insertProducts(List<Product> products);

        @Query("DELETE FROM products")
        void deleteAllProducts();

        // Cart
        @Query("SELECT * FROM cart_items WHERE userId = :userId")
        List<CartItem> getCartItems(int userId);

        @Query("SELECT c.id, c.quantity, c.productId, p.name as productName, p.price, p.imageResName " +
                        "FROM cart_items c INNER JOIN products p ON c.productId = p.id " +
                        "WHERE c.userId = :userId")
        List<CartItemDetail> getCartItemDetails(int userId);

        @Query("SELECT * FROM cart_items WHERE userId = :userId AND productId = :productId")
        CartItem getCartItem(int userId, int productId);

        @Query("SELECT * FROM cart_items WHERE id = :id")
        CartItem getCartItemById(int id);

        @Insert
        void addToCart(CartItem item);

        @Update
        void updateCartItem(CartItem item);

        @Delete
        void deleteCartItem(CartItem item);

        @Query("DELETE FROM cart_items WHERE userId = :userId")
        void clearCart(int userId);

        // Order
        @Insert
        long insertOrder(Order order);

        @Insert
        void insertOrderItems(List<OrderItem> items);

        @Query("SELECT * FROM orders WHERE userId = :userId ORDER BY id DESC")
        List<Order> getOrders(int userId);

        @Query("SELECT * FROM orders WHERE id = :orderId")
        Order getOrderById(int orderId);

        @Query("SELECT oi.id, oi.orderId, oi.productId, oi.quantity, oi.price, p.name as productName, p.imageResName as productImage "
                        +
                        "FROM order_items oi INNER JOIN products p ON oi.productId = p.id " +
                        "WHERE oi.orderId = :orderId")
        List<OrderItemDetail> getOrderItems(int orderId);

        @Query("UPDATE orders SET status = 'Cancelled' WHERE id = :orderId")
        void cancelOrder(int orderId);

        @Query("UPDATE orders SET status = :status WHERE id = :orderId")
        void updateOrderStatus(int orderId, String status);

        // Address
        @Query("SELECT * FROM addresses WHERE userId = :userId")
        List<Address> getAddresses(int userId);

        @Insert
        void insertAddress(Address address);

        @Update
        void updateAddress(Address address);

        @Delete
        void deleteAddress(Address address);

        @Query("SELECT * FROM addresses WHERE id = :id")
        Address getAddressById(int id);

        @Query("UPDATE addresses SET isDefault = 0 WHERE userId = :userId")
        void clearDefaultAddress(int userId);

        @Query("UPDATE addresses SET isDefault = 1 WHERE id = :id")
        void setDefaultAddress(int id);

        // Admin
        @Query("SELECT * FROM orders ORDER BY id DESC")
        List<Order> getAllOrders();

        @Update
        void updateProduct(Product product);

        @Delete
        void deleteProduct(Product product);

        @Query("UPDATE products SET stock = stock - :quantity WHERE id = :productId")
        void decreaseStock(int productId, int quantity);

        // Statistics
        @Query("SELECT SUM(totalPrice) FROM orders WHERE status = 'Completed'")
        Long getTotalRevenue();

        @Query("SELECT COUNT(*) FROM orders")
        int getTotalOrders();

        @Query("SELECT COUNT(*) FROM orders WHERE status = :status")
        int getOrderCountByStatus(String status);
}