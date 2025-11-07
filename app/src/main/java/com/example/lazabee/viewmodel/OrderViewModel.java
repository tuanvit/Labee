package com.example.lazabee.viewmodel;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import com.example.lazabee.data.model.ApiResponse;
import com.example.lazabee.data.model.PageResponse;
import com.example.lazabee.data.model.order.OrderResponse;
import com.example.lazabee.data.repository.OrderRepository;

public class OrderViewModel extends AndroidViewModel {
    private OrderRepository orderRepository;

    public OrderViewModel(@NonNull Application application) {
        super(application);
        orderRepository = new OrderRepository(application);
    }

    public LiveData<ApiResponse<PageResponse<OrderResponse>>> getOrders(int page, int size) {
        return orderRepository.getOrders(page, size);
    }

    public LiveData<ApiResponse<OrderResponse>> getOrderById(String orderId) {
        return orderRepository.getOrderById(orderId);
    }

    public LiveData<ApiResponse<OrderResponse>> placeOrder(String addressId, String paymentMethod, String note) {
        return orderRepository.placeOrder(addressId, paymentMethod, note);
    }

    public LiveData<ApiResponse<OrderResponse>> cancelOrder(String orderId) {
        return orderRepository.cancelOrder(orderId);
    }
}
