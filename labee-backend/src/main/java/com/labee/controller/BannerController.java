package com.labee.controller;

import com.labee.dto.response.ApiResponse;
import com.labee.model.entity.Banner;
import com.labee.service.BannerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/banners")
@RequiredArgsConstructor
public class BannerController {
    
    private final BannerService bannerService;
    
    @GetMapping
    public ResponseEntity<ApiResponse<List<Banner>>> getAllBanners() {
        List<Banner> banners = bannerService.getAllActiveBanners();
        return ResponseEntity.ok(ApiResponse.success(banners));
    }
}
