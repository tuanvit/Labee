package com.labee.repository;

import com.labee.model.entity.CartItem;
import com.labee.model.entity.Product;
import com.labee.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, String> {

    List<CartItem> findByUserUserId(String userId);
    
    List<CartItem> findByUser(User user);

    Optional<CartItem> findByUserUserIdAndProductProductId(String userId, String productId);
    
    Optional<CartItem> findByUserAndProduct(User user, Product product);

    void deleteByUserUserId(String userId);
    
    void deleteByUser(User user);
}