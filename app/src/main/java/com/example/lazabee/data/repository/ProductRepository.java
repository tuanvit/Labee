package com.example.lazabee.data.repository;

import android.content.Context;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import com.example.lazabee.data.model.ApiResponse;
import com.example.lazabee.data.model.PageResponse;
import com.example.lazabee.data.model.Product;
import com.example.lazabee.data.remote.ApiService;
import com.example.lazabee.data.remote.RetrofitClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductRepository {
    private ApiService apiService;

    public ProductRepository(Context context) {
        this.apiService = RetrofitClient.getInstance(context).getApiService();
    }

    public LiveData<ApiResponse<PageResponse<Product>>> getAllProducts(int page, int size) {
        MutableLiveData<ApiResponse<PageResponse<Product>>> result = new MutableLiveData<>();

        apiService.getAllProducts(page, size).enqueue(
                new Callback<ApiResponse<PageResponse<Product>>>() {
                    @Override
                    public void onResponse(Call<ApiResponse<PageResponse<Product>>> call,
                            Response<ApiResponse<PageResponse<Product>>> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            result.setValue(response.body());
                        } else {
                            ApiResponse<PageResponse<Product>> errorResponse = new ApiResponse<>();
                            errorResponse.setSuccess(false);
                            errorResponse.setMessage("Failed to load products");
                            result.setValue(errorResponse);
                        }
                    }

                    @Override
                    public void onFailure(Call<ApiResponse<PageResponse<Product>>> call, Throwable t) {
                        ApiResponse<PageResponse<Product>> errorResponse = new ApiResponse<>();
                        errorResponse.setSuccess(false);
                        errorResponse.setMessage(t.getMessage());
                        result.setValue(errorResponse);
                    }
                });

        return result;
    }

    public LiveData<ApiResponse<Product>> getProductById(Long productId) {
        MutableLiveData<ApiResponse<Product>> result = new MutableLiveData<>();

        apiService.getProductById(productId).enqueue(new Callback<ApiResponse<Product>>() {
            @Override
            public void onResponse(Call<ApiResponse<Product>> call,
                    Response<ApiResponse<Product>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    result.setValue(response.body());
                } else {
                    ApiResponse<Product> errorResponse = new ApiResponse<>();
                    errorResponse.setSuccess(false);
                    errorResponse.setMessage("Product not found");
                    result.setValue(errorResponse);
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<Product>> call, Throwable t) {
                ApiResponse<Product> errorResponse = new ApiResponse<>();
                errorResponse.setSuccess(false);
                errorResponse.setMessage(t.getMessage());
                result.setValue(errorResponse);
            }
        });

        return result;
    }
}
