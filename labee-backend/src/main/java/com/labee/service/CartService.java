package com.labee.service;

import com.labee.dto.request.AddToCartRequest;
import com.labee.dto.response.CartItemResponse;
import com.labee.exception.BadRequestException;
import com.labee.exception.ResourceNotFoundException;
import com.labee.model.entity.CartItem;
import com.labee.model.entity.Product;
import com.labee.model.entity.User;
import com.labee.repository.CartItemRepository;
import com.labee.repository.ProductRepository;
import com.labee.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CartService {

    private final CartItemRepository cartItemRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;

    @Transactional
    public CartItemResponse addToCart(String userId, AddToCartRequest request) {
        // Verify user exists
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        // Verify product exists and is active
        Product product = productRepository.findById(request.getProductId())
                .orElseThrow(() -> new ResourceNotFoundException("Product not found"));

        if (!product.getIsActive()) {
            throw new BadRequestException("Product is not available");
        }

        // Check stock availability
        if (product.getStockQuantity() < request.getQuantity()) {
            throw new BadRequestException("Insufficient stock. Available: " + product.getStockQuantity());
        }

        // Check if item already exists in cart
        CartItem cartItem = cartItemRepository.findByUserAndProduct(user, product)
                .map(existingItem -> {
                    int newQuantity = existingItem.getQuantity() + request.getQuantity();
                    if (product.getStockQuantity() < newQuantity) {
                        throw new BadRequestException("Insufficient stock. Available: " + product.getStockQuantity());
                    }
                    existingItem.setQuantity(newQuantity);
                    return existingItem;
                })
                .orElseGet(() -> CartItem.builder()
                        .cartItemId(UUID.randomUUID().toString())
                        .user(user)
                        .product(product)
                        .quantity(request.getQuantity())
                        .build());

        cartItemRepository.save(cartItem);

        return mapToCartItemResponse(cartItem);
    }

    @Transactional(readOnly = true)
    public List<CartItemResponse> getCart(String userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        List<CartItem> cartItems = cartItemRepository.findByUser(user);

        return cartItems.stream()
                .map(this::mapToCartItemResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public CartItemResponse updateCartItemQuantity(String userId, String cartItemId, int quantity) {
        if (quantity <= 0) {
            throw new BadRequestException("Quantity must be greater than 0");
        }

        CartItem cartItem = cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new ResourceNotFoundException("Cart item not found"));

        // Verify ownership
        if (!cartItem.getUser().getUserId().equals(userId)) {
            throw new BadRequestException("Unauthorized access to cart item");
        }

        // Check stock availability
        if (cartItem.getProduct().getStockQuantity() < quantity) {
            throw new BadRequestException("Insufficient stock. Available: " + cartItem.getProduct().getStockQuantity());
        }

        cartItem.setQuantity(quantity);
        cartItemRepository.save(cartItem);

        return mapToCartItemResponse(cartItem);
    }

    @Transactional
    public void removeCartItem(String userId, String cartItemId) {
        CartItem cartItem = cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new ResourceNotFoundException("Cart item not found"));

        // Verify ownership
        if (!cartItem.getUser().getUserId().equals(userId)) {
            throw new BadRequestException("Unauthorized access to cart item");
        }

        cartItemRepository.delete(cartItem);
    }

    @Transactional
    public void clearCart(String userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        cartItemRepository.deleteByUser(user);
    }

    private CartItemResponse mapToCartItemResponse(CartItem cartItem) {
        Product product = cartItem.getProduct();
        BigDecimal itemTotal = product.getPrice().multiply(BigDecimal.valueOf(cartItem.getQuantity()));

        return CartItemResponse.builder()
                .cartItemId(cartItem.getCartItemId())
                .productId(product.getProductId())
                .productName(product.getName())
                .productImageUrl(product.getImageUrl())
                .price(product.getPrice())
                .quantity(cartItem.getQuantity())
                .subtotal(itemTotal)
                .build();
    }
}
