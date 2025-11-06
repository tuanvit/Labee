package com.labee.repository;

import com.labee.model.entity.Order;
import com.labee.model.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, String> {

    List<Order> findByUserUserIdOrderByCreatedAtDesc(String userId);
    
    Page<Order> findByUser(User user, Pageable pageable);
}