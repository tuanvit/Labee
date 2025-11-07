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
    @POST("api/auth/register")
    Call<ApiResponse<AuthResponse>> register(@Body RegisterRequest request);

    @POST("api/auth/login")
    Call<ApiResponse<AuthResponse>> login(@Body LoginRequest request);

    @GET("api/users/profile")
    Call<ApiResponse<AuthResponse>> getProfile();

    @GET("api/products")
    Call<ApiResponse<PageResponse<Product>>> getAllProducts(@Query("page") int page, @Query("size") int size);

    @GET("api/products/{id}")
    Call<ApiResponse<Product>> getProductById(@Path("id") Long productId);

    @GET("api/products/search")
    Call<ApiResponse<PageResponse<Product>>> searchProducts(
            @Query("keyword") String keyword,
            @Query("page") int page,
            @Query("size") int size);

    @GET("api/cart")
    Call<ApiResponse<List<CartItem>>> getCart();

    @POST("api/cart")
    Call<ApiResponse<CartItem>> addToCart(@Query("productId") Long productId, @Query("quantity") int quantity);

    @PUT("api/cart/{cartItemId}")
    Call<ApiResponse<Void>> updateCartItem(@Path("cartItemId") Long cartItemId, @Query("quantity") int quantity);

    @DELETE("api/cart/{cartItemId}")
    Call<ApiResponse<Void>> deleteCartItem(@Path("cartItemId") Long cartItemId);

    @DELETE("api/cart/clear")
    Call<ApiResponse<Void>> clearCart();

    @POST("api/orders")
    Call<ApiResponse<OrderResponse>> placeOrder(@Body PlaceOrderRequest request);

    @GET("api/orders")
    Call<ApiResponse<PageResponse<OrderResponse>>> getOrders(@Query("page") int page, @Query("size") int size);

    @GET("api/orders/{orderId}")
    Call<ApiResponse<OrderResponse>> getOrderById(@Path("orderId") Long orderId);

    @PUT("api/orders/{orderId}/cancel")
    Call<ApiResponse<Void>> cancelOrder(@Path("orderId") Long orderId);

    @GET("api/addresses")
    Call<ApiResponse<List<AddressResponse>>> getAddresses();

    @GET("api/addresses/{addressId}")
    Call<ApiResponse<AddressResponse>> getAddressById(@Path("addressId") Long addressId);

    @POST("api/addresses")
    Call<ApiResponse<AddressResponse>> createAddress(@Body AddressRequest request);

    @PUT("api/addresses/{addressId}")
    Call<ApiResponse<AddressResponse>> updateAddress(@Path("addressId") Long addressId, @Body AddressRequest request);

    @DELETE("api/addresses/{addressId}")
    Call<ApiResponse<Void>> deleteAddress(@Path("addressId") Long addressId);

    @PUT("api/addresses/{addressId}/default")
    Call<ApiResponse<AddressResponse>> setDefaultAddress(@Path("addressId") Long addressId);
}
