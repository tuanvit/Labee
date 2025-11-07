package com.labee.repository;

import com.labee.model.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product, String> {

    Page<Product> findByCategoryCategoryIdAndIsActiveTrue(String categoryId, Pageable pageable);

    Page<Product> findByIsActiveTrue(Pageable pageable);
}