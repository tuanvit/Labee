package com.example.lazabee;

import android.app.Application;
import com.example.lazabee.data.local.TokenManager;
import com.example.lazabee.data.remote.RetrofitClient;
import com.example.lazabee.data.repository.*;

public class LazabeeApplication extends Application {
    private static LazabeeApplication instance;

    private TokenManager tokenManager;
    private RetrofitClient retrofitClient;

    private AuthRepository authRepository;
    private ProductRepository productRepository;
    private CartRepository cartRepository;
    private OrderRepository orderRepository;
    private AddressRepository addressRepository;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;

        // Initialize TokenManager
        tokenManager = TokenManager.getInstance(this);

        // Initialize RetrofitClient
        retrofitClient = RetrofitClient.getInstance(this);

        // Initialize Repositories
        authRepository = new AuthRepository(this);
        productRepository = new ProductRepository(this);
        cartRepository = new CartRepository(this);
        orderRepository = new OrderRepository(this);
        addressRepository = new AddressRepository(this);
    }

    public static LazabeeApplication getInstance() {
        return instance;
    }

    public TokenManager getTokenManager() {
        return tokenManager;
    }

    public RetrofitClient getRetrofitClient() {
        return retrofitClient;
    }

    public AuthRepository getAuthRepository() {
        return authRepository;
    }

    public ProductRepository getProductRepository() {
        return productRepository;
    }

    public CartRepository getCartRepository() {
        return cartRepository;
    }

    public OrderRepository getOrderRepository() {
        return orderRepository;
    }

    public AddressRepository getAddressRepository() {
        return addressRepository;
    }
}