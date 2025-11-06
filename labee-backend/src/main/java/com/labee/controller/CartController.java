package com.labee.controller;

import com.labee.dto.request.AddToCartRequest;
import com.labee.dto.response.ApiResponse;
import com.labee.dto.response.CartItemResponse;
import com.labee.service.CartService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/cart")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;

    @PostMapping
    public ResponseEntity<ApiResponse<CartItemResponse>> addToCart(
            @Valid @RequestBody AddToCartRequest request,
            HttpServletRequest httpRequest) {
        String userId = (String) httpRequest.getAttribute("userId");
        CartItemResponse response = cartService.addToCart(userId, request);
        return ResponseEntity.ok(ApiResponse.success("Item added to cart successfully", response));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<CartItemResponse>>> getCart(HttpServletRequest httpRequest) {
        String userId = (String) httpRequest.getAttribute("userId");
        List<CartItemResponse> cart = cartService.getCart(userId);
        return ResponseEntity.ok(ApiResponse.success(cart));
    }

    @PutMapping("/{cartItemId}")
    public ResponseEntity<ApiResponse<CartItemResponse>> updateCartItem(
            @PathVariable String cartItemId,
            @RequestParam int quantity,
            HttpServletRequest httpRequest) {
        String userId = (String) httpRequest.getAttribute("userId");
        CartItemResponse response = cartService.updateCartItemQuantity(userId, cartItemId, quantity);
        return ResponseEntity.ok(ApiResponse.success("Cart item updated successfully", response));
    }

    @DeleteMapping("/{cartItemId}")
    public ResponseEntity<ApiResponse<Void>> removeCartItem(
            @PathVariable String cartItemId,
            HttpServletRequest httpRequest) {
        String userId = (String) httpRequest.getAttribute("userId");
        cartService.removeCartItem(userId, cartItemId);
        return ResponseEntity.ok(ApiResponse.success("Item removed from cart successfully", null));
    }

    @DeleteMapping
    public ResponseEntity<ApiResponse<Void>> clearCart(HttpServletRequest httpRequest) {
        String userId = (String) httpRequest.getAttribute("userId");
        cartService.clearCart(userId);
        return ResponseEntity.ok(ApiResponse.success("Cart cleared successfully", null));
    }
}
