package com.example.lazabee.database;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;
import androidx.room.Delete;

import com.example.lazabee.model.CartItem;
import com.example.lazabee.model.CartItemDetail;
import com.example.lazabee.model.Order;
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

    @Query("SELECT * FROM products WHERE name LIKE '%' || :keyword || '%'")
    List<Product> searchProducts(String keyword);

    @Insert
    void insertProduct(Product product);

    @Insert
    void insertProducts(List<Product> products);

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
    void insertOrder(Order order);

    @Query("SELECT * FROM orders WHERE userId = :userId")
    List<Order> getOrders(int userId);
}