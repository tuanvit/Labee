package com.labee.controller;

import com.labee.dto.request.AddressRequest;
import com.labee.dto.response.AddressResponse;
import com.labee.dto.response.ApiResponse;
import com.labee.service.AddressService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/addresses")
@RequiredArgsConstructor
public class AddressController {

    private final AddressService addressService;

    @PostMapping
    public ResponseEntity<ApiResponse<AddressResponse>> createAddress(
            @Valid @RequestBody AddressRequest request,
            HttpServletRequest httpRequest) {
        String userId = (String) httpRequest.getAttribute("userId");
        AddressResponse response = addressService.createAddress(userId, request);
        return ResponseEntity.ok(ApiResponse.success("Address created successfully", response));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<AddressResponse>>> getAddresses(HttpServletRequest httpRequest) {
        String userId = (String) httpRequest.getAttribute("userId");
        List<AddressResponse> addresses = addressService.getAddresses(userId);
        return ResponseEntity.ok(ApiResponse.success(addresses));
    }

    @PutMapping("/{addressId}")
    public ResponseEntity<ApiResponse<AddressResponse>> updateAddress(
            @PathVariable String addressId,
            @Valid @RequestBody AddressRequest request,
            HttpServletRequest httpRequest) {
        String userId = (String) httpRequest.getAttribute("userId");
        AddressResponse response = addressService.updateAddress(userId, addressId, request);
        return ResponseEntity.ok(ApiResponse.success(response, "Address updated successfully"));
    }

    @DeleteMapping("/{addressId}")
    public ResponseEntity<ApiResponse<Void>> deleteAddress(
            @PathVariable String addressId,
            HttpServletRequest httpRequest) {
        String userId = (String) httpRequest.getAttribute("userId");
        addressService.deleteAddress(userId, addressId);
        return ResponseEntity.ok(ApiResponse.success(null, "Address deleted successfully"));
    }

    @PutMapping("/{addressId}/set-default")
    public ResponseEntity<ApiResponse<AddressResponse>> setDefaultAddress(
            @PathVariable String addressId,
            HttpServletRequest httpRequest) {
        String userId = (String) httpRequest.getAttribute("userId");
        AddressResponse response = addressService.setDefaultAddress(userId, addressId);
        return ResponseEntity.ok(ApiResponse.success(response, "Default address set successfully"));
    }
}
