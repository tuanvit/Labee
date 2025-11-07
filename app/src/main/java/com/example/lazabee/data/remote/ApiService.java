package com.example.lazabee.data.remote;

import com.example.lazabee.data.model.ApiResponse;
import com.example.lazabee.data.model.CartItem;
import com.example.lazabee.data.model.PageResponse;
import com.example.lazabee.data.model.Product;
import com.example.lazabee.data.model.auth.AuthResponse;
import com.example.lazabee.data.model.auth.LoginRequest;
import com.example.lazabee.data.model.auth.RegisterRequest;
import com.example.lazabee.data.model.order.OrderResponse;
import com.example.lazabee.data.model.order.PlaceOrderRequest;
import com.example.lazabee.data.model.address.AddressRequest;
import com.example.lazabee.data.model.address.AddressResponse;
import retrofit2.Call;
import retrofit2.http.*;
import java.util.List;

public interface ApiService {
    @POST("api/v1/auth/register")
    Call<ApiResponse<AuthResponse>> register(@Body RegisterRequest request);

    @POST("api/v1/auth/login")
    Call<ApiResponse<AuthResponse>> login(@Body LoginRequest request);

    @GET("api/v1/auth/profile")
    Call<ApiResponse<AuthResponse>> getProfile();

    @GET("api/v1/products")
    Call<ApiResponse<PageResponse<Product>>> getAllProducts(@Query("page") int page, @Query("size") int size);

    @GET("api/v1/products/{id}")
    Call<ApiResponse<Product>> getProductById(@Path("id") String productId);

    @GET("api/v1/products/search")
    Call<ApiResponse<PageResponse<Product>>> searchProducts(
            @Query("keyword") String keyword,
            @Query("page") int page,
            @Query("size") int size);

    @GET("api/v1/cart")
    Call<ApiResponse<List<CartItem>>> getCart();

    @POST("api/v1/cart")
    Call<ApiResponse<CartItem>> addToCart(@Query("productId") String productId, @Query("quantity") int quantity);

    @PUT("api/v1/cart/{cartItemId}")
    Call<ApiResponse<Void>> updateCartItem(@Path("cartItemId") Long cartItemId, @Query("quantity") int quantity);

    @DELETE("api/v1/cart/{cartItemId}")
    Call<ApiResponse<Void>> deleteCartItem(@Path("cartItemId") Long cartItemId);

    @DELETE("api/v1/cart/clear")
    Call<ApiResponse<Void>> clearCart();

    @POST("api/v1/orders")
    Call<ApiResponse<OrderResponse>> placeOrder(@Body PlaceOrderRequest request);

    @GET("api/v1/orders")
    Call<ApiResponse<PageResponse<OrderResponse>>> getOrders(@Query("page") int page, @Query("size") int size);

    @GET("api/v1/orders/{orderId}")
    Call<ApiResponse<OrderResponse>> getOrderById(@Path("orderId") Long orderId);

    @PUT("api/v1/orders/{orderId}/cancel")
    Call<ApiResponse<Void>> cancelOrder(@Path("orderId") Long orderId);

    @GET("api/v1/addresses")
    Call<ApiResponse<List<AddressResponse>>> getAddresses();

    @GET("api/v1/addresses/{addressId}")
    Call<ApiResponse<AddressResponse>> getAddressById(@Path("addressId") Long addressId);

    @POST("api/v1/addresses")
    Call<ApiResponse<AddressResponse>> createAddress(@Body AddressRequest request);

    @PUT("api/v1/addresses/{addressId}")
    Call<ApiResponse<AddressResponse>> updateAddress(@Path("addressId") Long addressId, @Body AddressRequest request);

    @DELETE("api/v1/addresses/{addressId}")
    Call<ApiResponse<Void>> deleteAddress(@Path("addressId") Long addressId);

    @PUT("api/v1/addresses/{addressId}/default")
    Call<ApiResponse<AddressResponse>> setDefaultAddress(@Path("addressId") Long addressId);
}
