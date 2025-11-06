package com.labee.controller;

import com.labee.dto.response.ApiResponse;
import com.labee.dto.response.FavoriteResponse;
import com.labee.service.FavoriteService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/favorites")
@RequiredArgsConstructor
public class FavoriteController {

    private final FavoriteService favoriteService;

    @PostMapping("/{productId}")
    public ResponseEntity<ApiResponse<FavoriteResponse>> addToFavorites(
            @PathVariable String productId,
            HttpServletRequest httpRequest) {
        String userId = (String) httpRequest.getAttribute("userId");
        FavoriteResponse response = favoriteService.addToFavorites(userId, productId);
        return ResponseEntity.ok(ApiResponse.success(response, "Added to favorites successfully"));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<FavoriteResponse>>> getFavorites(HttpServletRequest httpRequest) {
        String userId = (String) httpRequest.getAttribute("userId");
        List<FavoriteResponse> favorites = favoriteService.getFavorites(userId);
        return ResponseEntity.ok(ApiResponse.success(favorites));
    }

    @DeleteMapping("/{productId}")
    public ResponseEntity<ApiResponse<Void>> removeFromFavorites(
            @PathVariable String productId,
            HttpServletRequest httpRequest) {
        String userId = (String) httpRequest.getAttribute("userId");
        favoriteService.removeFromFavorites(userId, productId);
        return ResponseEntity.ok(ApiResponse.success(null, "Removed from favorites successfully"));
    }

    @GetMapping("/{productId}/check")
    public ResponseEntity<ApiResponse<Boolean>> checkFavorite(
            @PathVariable String productId,
            HttpServletRequest httpRequest) {
        String userId = (String) httpRequest.getAttribute("userId");
        boolean isFavorite = favoriteService.isFavorite(userId, productId);
        return ResponseEntity.ok(ApiResponse.success(isFavorite));
    }
}
