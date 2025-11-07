package com.example.lazabee.viewmodel;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
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

    public LiveData<ApiResponse<CartItem>> addToCart(Long productId, int quantity) {
        return cartRepository.addToCart(productId, quantity);
    }

    // Wrapper to expose add to cart result
    private LiveData<ApiResponse<CartItem>> addToCartResultLiveData;

    public LiveData<ApiResponse<CartItem>> getAddToCartResult() {
        return addToCartResultLiveData;
    }

    public void addProductToCart(Long productId, int quantity) {
        addToCartResultLiveData = cartRepository.addToCart(productId, quantity);
    }

    public LiveData<ApiResponse<Void>> updateCartItem(Long cartItemId, int quantity) {
        return cartRepository.updateCartItem(cartItemId, quantity);
    }

    // Wrapper to expose update result
    private LiveData<ApiResponse<Void>> updateCartResultLiveData;

    public LiveData<ApiResponse<Void>> getUpdateCartResult() {
        return updateCartResultLiveData;
    }

    public void updateCart(Long cartItemId, int quantity) {
        updateCartResultLiveData = cartRepository.updateCartItem(cartItemId, quantity);
    }

    public LiveData<ApiResponse<Void>> deleteCartItem(Long cartItemId) {
        return cartRepository.deleteCartItem(cartItemId);
    }

    // Wrapper to expose delete result
    private LiveData<ApiResponse<Void>> deleteCartItemResultLiveData;

    public LiveData<ApiResponse<Void>> getDeleteCartItemResult() {
        return deleteCartItemResultLiveData;
    }

    public void deleteItem(Long cartItemId) {
        deleteCartItemResultLiveData = cartRepository.deleteCartItem(cartItemId);
    }

    public LiveData<ApiResponse<Void>> clearCart() {
        return cartRepository.clearCart();
    }
}
