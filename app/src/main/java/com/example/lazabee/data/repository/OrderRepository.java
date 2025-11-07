package com.example.lazabee.data.repository;

import android.content.Context;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import com.example.lazabee.data.model.ApiResponse;
import com.example.lazabee.data.model.PageResponse;
import com.example.lazabee.data.model.order.*;
import com.example.lazabee.data.remote.ApiService;
import com.example.lazabee.data.remote.RetrofitClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrderRepository {
    private ApiService apiService;

    public OrderRepository(Context context) {
        this.apiService = RetrofitClient.getInstance(context).getApiService();
    }

    public LiveData<ApiResponse<PageResponse<OrderResponse>>> getOrders(int page, int size) {
        MutableLiveData<ApiResponse<PageResponse<OrderResponse>>> result = new MutableLiveData<>();

        apiService.getOrders(page, size).enqueue(new Callback<ApiResponse<PageResponse<OrderResponse>>>() {
            @Override
            public void onResponse(Call<ApiResponse<PageResponse<OrderResponse>>> call,
                    Response<ApiResponse<PageResponse<OrderResponse>>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    result.postValue(response.body());
                } else {
                    ApiResponse<PageResponse<OrderResponse>> errorResponse = new ApiResponse<>(false,
                            "Failed to load orders", null);
                    result.postValue(errorResponse);
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<PageResponse<OrderResponse>>> call, Throwable t) {
                ApiResponse<PageResponse<OrderResponse>> errorResponse = new ApiResponse<>(false, t.getMessage(), null);
                result.postValue(errorResponse);
            }
        });

        return result;
    }

    public LiveData<ApiResponse<OrderResponse>> getOrderById(String orderId) {
        MutableLiveData<ApiResponse<OrderResponse>> result = new MutableLiveData<>();

        apiService.getOrderById(orderId).enqueue(new Callback<ApiResponse<OrderResponse>>() {
            @Override
            public void onResponse(Call<ApiResponse<OrderResponse>> call,
                    Response<ApiResponse<OrderResponse>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    result.postValue(response.body());
                } else {
                    ApiResponse<OrderResponse> errorResponse = new ApiResponse<>(false, "Order not found", null);
                    result.postValue(errorResponse);
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<OrderResponse>> call, Throwable t) {
                ApiResponse<OrderResponse> errorResponse = new ApiResponse<>(false, t.getMessage(), null);
                result.postValue(errorResponse);
            }
        });

        return result;
    }

    public LiveData<ApiResponse<OrderResponse>> placeOrder(String addressId, String paymentMethod, String note) {
        MutableLiveData<ApiResponse<OrderResponse>> result = new MutableLiveData<>();

        PlaceOrderRequest request = new PlaceOrderRequest(addressId, paymentMethod, note);
        apiService.placeOrder(request).enqueue(new Callback<ApiResponse<OrderResponse>>() {
            @Override
            public void onResponse(Call<ApiResponse<OrderResponse>> call,
                    Response<ApiResponse<OrderResponse>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    result.postValue(response.body());
                } else {
                    ApiResponse<OrderResponse> errorResponse = new ApiResponse<>(false, "Failed to place order", null);
                    result.postValue(errorResponse);
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<OrderResponse>> call, Throwable t) {
                ApiResponse<OrderResponse> errorResponse = new ApiResponse<>(false, t.getMessage(), null);
                result.postValue(errorResponse);
            }
        });

        return result;
    }

    public LiveData<ApiResponse<OrderResponse>> cancelOrder(String orderId) {
        MutableLiveData<ApiResponse<OrderResponse>> result = new MutableLiveData<>();

        apiService.cancelOrder(orderId).enqueue(new Callback<ApiResponse<OrderResponse>>() {
            @Override
            public void onResponse(Call<ApiResponse<OrderResponse>> call,
                    Response<ApiResponse<OrderResponse>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    result.postValue(response.body());
                } else {
                    ApiResponse<OrderResponse> errorResponse = new ApiResponse<>(false, "Failed to cancel order", null);
                    result.postValue(errorResponse);
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<OrderResponse>> call, Throwable t) {
                ApiResponse<OrderResponse> errorResponse = new ApiResponse<>(false, t.getMessage(), null);
                result.postValue(errorResponse);
            }
        });

        return result;
    }
}
