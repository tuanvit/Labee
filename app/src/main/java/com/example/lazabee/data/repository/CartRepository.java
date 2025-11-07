package com.example.lazabee.data.repository;

import android.content.Context;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import com.example.lazabee.data.model.ApiResponse;
import com.example.lazabee.data.model.CartItem;
import com.example.lazabee.data.remote.ApiService;
import com.example.lazabee.data.remote.RetrofitClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.util.List;

public class CartRepository {
    private ApiService apiService;

    public CartRepository(Context context) {
        this.apiService = RetrofitClient.getInstance(context).getApiService();
    }

    public LiveData<ApiResponse<List<CartItem>>> getCart() {
        MutableLiveData<ApiResponse<List<CartItem>>> result = new MutableLiveData<>();

        apiService.getCart().enqueue(new Callback<ApiResponse<List<CartItem>>>() {
            @Override
            public void onResponse(Call<ApiResponse<List<CartItem>>> call,
                    Response<ApiResponse<List<CartItem>>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    result.setValue(response.body());
                } else {
                    ApiResponse<List<CartItem>> errorResponse = new ApiResponse<>();
                    errorResponse.setSuccess(false);
                    errorResponse.setMessage("Failed to load cart");
                    result.setValue(errorResponse);
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<List<CartItem>>> call, Throwable t) {
                ApiResponse<List<CartItem>> errorResponse = new ApiResponse<>();
                errorResponse.setSuccess(false);
                errorResponse.setMessage(t.getMessage());
                result.setValue(errorResponse);
            }
        });

        return result;
    }

    public LiveData<ApiResponse<CartItem>> addToCart(String productId, int quantity) {
        MutableLiveData<ApiResponse<CartItem>> result = new MutableLiveData<>();

        apiService.addToCart(productId, quantity).enqueue(new Callback<ApiResponse<CartItem>>() {
            @Override
            public void onResponse(Call<ApiResponse<CartItem>> call,
                    Response<ApiResponse<CartItem>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    result.setValue(response.body());
                } else {
                    ApiResponse<CartItem> errorResponse = new ApiResponse<>();
                    errorResponse.setSuccess(false);
                    errorResponse.setMessage("Failed to add to cart");
                    result.setValue(errorResponse);
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<CartItem>> call, Throwable t) {
                ApiResponse<CartItem> errorResponse = new ApiResponse<>();
                errorResponse.setSuccess(false);
                errorResponse.setMessage(t.getMessage());
                result.setValue(errorResponse);
            }
        });

        return result;
    }

    public LiveData<ApiResponse<Void>> updateCartItem(Long cartItemId, int quantity) {
        MutableLiveData<ApiResponse<Void>> result = new MutableLiveData<>();

        apiService.updateCartItem(cartItemId, quantity).enqueue(new Callback<ApiResponse<Void>>() {
            @Override
            public void onResponse(Call<ApiResponse<Void>> call,
                    Response<ApiResponse<Void>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    result.setValue(response.body());
                } else {
                    ApiResponse<Void> errorResponse = new ApiResponse<>();
                    errorResponse.setSuccess(false);
                    errorResponse.setMessage("Failed to update cart item");
                    result.setValue(errorResponse);
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<Void>> call, Throwable t) {
                ApiResponse<Void> errorResponse = new ApiResponse<>();
                errorResponse.setSuccess(false);
                errorResponse.setMessage(t.getMessage());
                result.setValue(errorResponse);
            }
        });

        return result;
    }

    public LiveData<ApiResponse<Void>> deleteCartItem(Long cartItemId) {
        MutableLiveData<ApiResponse<Void>> result = new MutableLiveData<>();

        apiService.deleteCartItem(cartItemId).enqueue(new Callback<ApiResponse<Void>>() {
            @Override
            public void onResponse(Call<ApiResponse<Void>> call,
                    Response<ApiResponse<Void>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    result.setValue(response.body());
                } else {
                    ApiResponse<Void> errorResponse = new ApiResponse<>();
                    errorResponse.setSuccess(false);
                    errorResponse.setMessage("Failed to delete cart item");
                    result.setValue(errorResponse);
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<Void>> call, Throwable t) {
                ApiResponse<Void> errorResponse = new ApiResponse<>();
                errorResponse.setSuccess(false);
                errorResponse.setMessage(t.getMessage());
                result.setValue(errorResponse);
            }
        });

        return result;
    }

    public LiveData<ApiResponse<Void>> clearCart() {
        MutableLiveData<ApiResponse<Void>> result = new MutableLiveData<>();

        apiService.clearCart().enqueue(new Callback<ApiResponse<Void>>() {
            @Override
            public void onResponse(Call<ApiResponse<Void>> call,
                    Response<ApiResponse<Void>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    result.setValue(response.body());
                } else {
                    ApiResponse<Void> errorResponse = new ApiResponse<>();
                    errorResponse.setSuccess(false);
                    errorResponse.setMessage("Failed to clear cart");
                    result.setValue(errorResponse);
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<Void>> call, Throwable t) {
                ApiResponse<Void> errorResponse = new ApiResponse<>();
                errorResponse.setSuccess(false);
                errorResponse.setMessage(t.getMessage());
                result.setValue(errorResponse);
            }
        });

        return result;
    }
}
