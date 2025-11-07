package com.labee.service;

import com.labee.dto.response.FavoriteResponse;
import com.labee.exception.BadRequestException;
import com.labee.exception.ResourceNotFoundException;
import com.labee.model.entity.Favorite;
import com.labee.model.entity.Product;
import com.labee.model.entity.User;
import com.labee.repository.FavoriteRepository;
import com.labee.repository.ProductRepository;
import com.labee.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FavoriteService {

    private final FavoriteRepository favoriteRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;

    @Transactional
    public FavoriteResponse addToFavorites(String userId, String productId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found"));

        // Check if already in favorites
        if (favoriteRepository.findByUserAndProduct(user, product).isPresent()) {
            throw new BadRequestException("Product already in favorites");
        }

        // Create favorite (favoriteId will be auto-generated)
        Favorite favorite = Favorite.builder()
                .user(user)
                .product(product)
                .build();

        favorite = favoriteRepository.save(favorite);

        return mapToFavoriteResponse(favorite);
    }

    @Transactional(readOnly = true)
    public List<FavoriteResponse> getFavorites(String userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        List<Favorite> favorites = favoriteRepository.findByUser(user);

        return favorites.stream()
                .map(this::mapToFavoriteResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public void removeFromFavorites(String userId, String productId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found"));

        Favorite favorite = favoriteRepository.findByUserAndProduct(user, product)
                .orElseThrow(() -> new ResourceNotFoundException("Favorite not found"));

        favoriteRepository.delete(favorite);
    }

    @Transactional(readOnly = true)
    public boolean isFavorite(String userId, String productId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found"));

        return favoriteRepository.findByUserAndProduct(user, product).isPresent();
    }

    private FavoriteResponse mapToFavoriteResponse(Favorite favorite) {
        Product product = favorite.getProduct();

        return FavoriteResponse.builder()
                .favoriteId(favorite.getFavoriteId())
                .productId(product.getProductId())
                .productName(product.getName())
                .productImage(product.getImageUrl())
                .price(product.getPrice())
                .originalPrice(product.getOriginalPrice())
                .rating(product.getRating().doubleValue())
                .isActive(product.getIsActive())
                .build();
    }
}
