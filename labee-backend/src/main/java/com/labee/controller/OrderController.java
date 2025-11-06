package com.labee.controller;

import com.labee.dto.request.PlaceOrderRequest;
import com.labee.dto.response.ApiResponse;
import com.labee.dto.response.OrderResponse;
import com.labee.service.OrderService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    public ResponseEntity<ApiResponse<OrderResponse>> placeOrder(
            @Valid @RequestBody PlaceOrderRequest request,
            HttpServletRequest httpRequest) {
        String userId = (String) httpRequest.getAttribute("userId");
        OrderResponse response = orderService.placeOrder(userId, request);
        return ResponseEntity.ok(ApiResponse.success(response, "Order placed successfully"));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<Page<OrderResponse>>> getOrders(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            HttpServletRequest httpRequest) {
        String userId = (String) httpRequest.getAttribute("userId");
        Page<OrderResponse> orders = orderService.getOrders(userId, page, size);
        return ResponseEntity.ok(ApiResponse.success(orders));
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<ApiResponse<OrderResponse>> getOrderDetail(
            @PathVariable String orderId,
            HttpServletRequest httpRequest) {
        String userId = (String) httpRequest.getAttribute("userId");
        OrderResponse response = orderService.getOrderDetail(userId, orderId);
        return ResponseEntity.ok(ApiResponse.success(response));
    }
}
