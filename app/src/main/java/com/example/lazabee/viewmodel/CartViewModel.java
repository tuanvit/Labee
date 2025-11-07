package com.example.lazabee.viewmodel;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import com.example.lazabee.LazabeeApplication;
import com.example.lazabee.data.model.ApiResponse;
import com.example.lazabee.data.model.CartItem;
import com.example.lazabee.data.repository.CartRepository;

import java.util.List;

public class CartViewModel extends AndroidViewModel {
    private CartRepository cartRepository;

    public CartViewModel(@NonNull Application application) {
        super(application);
        this.cartRepository = ((LazabeeApplication) application).getCartRepository();
    }

    public LiveData<ApiResponse<List<CartItem>>> getCartItems() {
        return cartRepository.getCart();
    }

    public void getCart() {
        // Trigger cart loading (repository handles LiveData)
        cartRepository.getCart();
    }

    public LiveData<ApiResponse<CartItem>> addToCart(String productId, int quantity) {
        return cartRepository.addToCart(productId, quantity);
    }

    // Wrapper to expose add to cart result - INITIALIZED
    private MutableLiveData<ApiResponse<CartItem>> addToCartResultLiveData = new MutableLiveData<>();

    public LiveData<ApiResponse<CartItem>> getAddToCartResult() {
        return addToCartResultLiveData;
    }

    public void addProductToCart(String productId, int quantity) {
        LiveData<ApiResponse<CartItem>> result = cartRepository.addToCart(productId, quantity);
        result.observeForever(response -> {
            addToCartResultLiveData.postValue(response);
        });
    }

    public LiveData<ApiResponse<Void>> updateCartItem(Long cartItemId, int quantity) {
        return cartRepository.updateCartItem(cartItemId, quantity);
    }

    // Wrapper to expose update result - INITIALIZED
    private MutableLiveData<ApiResponse<Void>> updateCartResultLiveData = new MutableLiveData<>();

    public LiveData<ApiResponse<Void>> getUpdateCartResult() {
        return updateCartResultLiveData;
    }

    public void updateCart(Long cartItemId, int quantity) {
        LiveData<ApiResponse<Void>> result = cartRepository.updateCartItem(cartItemId, quantity);
        result.observeForever(response -> {
            updateCartResultLiveData.postValue(response);
        });
    }

    public LiveData<ApiResponse<Void>> deleteCartItem(Long cartItemId) {
        return cartRepository.deleteCartItem(cartItemId);
    }

    // Wrapper to expose delete result - INITIALIZED
    private MutableLiveData<ApiResponse<Void>> deleteCartItemResultLiveData = new MutableLiveData<>();

    public LiveData<ApiResponse<Void>> getDeleteCartItemResult() {
        return deleteCartItemResultLiveData;
    }

    public void deleteItem(Long cartItemId) {
        LiveData<ApiResponse<Void>> result = cartRepository.deleteCartItem(cartItemId);
        result.observeForever(response -> {
            deleteCartItemResultLiveData.postValue(response);
        });
    }

    public LiveData<ApiResponse<Void>> clearCart() {
        return cartRepository.clearCart();
    }
}
