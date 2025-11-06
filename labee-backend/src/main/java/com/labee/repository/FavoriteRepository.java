package com.labee.repository;

import com.labee.model.entity.Favorite;
import com.labee.model.entity.Product;
import com.labee.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FavoriteRepository extends JpaRepository<Favorite, String> {
    List<Favorite> findByUser(User user);
    Optional<Favorite> findByUserAndProduct(User user, Product product);
}
