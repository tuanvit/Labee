package com.example.lazabee.viewmodel;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import com.example.lazabee.LazabeeApplication;
import com.example.lazabee.data.model.ApiResponse;
import com.example.lazabee.data.model.PageResponse;
import com.example.lazabee.data.model.Product;
import com.example.lazabee.data.repository.ProductRepository;

public class ProductViewModel extends AndroidViewModel {
    private ProductRepository productRepository;

    public ProductViewModel(@NonNull Application application) {
        super(application);
        this.productRepository = ((LazabeeApplication) application).getProductRepository();
    }

    public LiveData<ApiResponse<PageResponse<Product>>> getProducts(int page, int size) {
        return productRepository.getAllProducts(page, size);
    }

    public LiveData<ApiResponse<Product>> getProductById(Long productId) {
        return productRepository.getProductById(productId);
    }
    
    // Wrapper to expose product detail as separate LiveData
    private LiveData<ApiResponse<Product>> productDetailLiveData;
    
    public LiveData<ApiResponse<Product>> getProductDetail() {
        return productDetailLiveData;
    }
    
    public void loadProductDetail(Long productId) {
        productDetailLiveData = productRepository.getProductById(productId);
    }
}
