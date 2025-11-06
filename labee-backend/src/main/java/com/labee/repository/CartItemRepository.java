package com.labee.repository;

import com.labee.model.entity.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, String> {

    List<CartItem> findByUserUserId(String userId);

    Optional<CartItem> findByUserUserIdAndProductProductId(String userId, String productId);

    void deleteByUserUserId(String userId);
}