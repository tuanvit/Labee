package com.example.lazabee.data.repository;

import android.content.Context;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import com.example.lazabee.data.model.ApiResponse;
import com.example.lazabee.data.model.address.*;
import com.example.lazabee.data.remote.ApiService;
import com.example.lazabee.data.remote.RetrofitClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import java.util.List;

public class AddressRepository {
    private ApiService apiService;

    public AddressRepository(Context context) {
        this.apiService = RetrofitClient.getInstance(context).getApiService();
    }

    public LiveData<ApiResponse<List<AddressResponse>>> getAddresses() {
        MutableLiveData<ApiResponse<List<AddressResponse>>> result = new MutableLiveData<>();

        apiService.getAddresses().enqueue(new Callback<ApiResponse<List<AddressResponse>>>() {
            @Override
            public void onResponse(Call<ApiResponse<List<AddressResponse>>> call,
                    Response<ApiResponse<List<AddressResponse>>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    result.postValue(response.body());
                } else {
                    ApiResponse<List<AddressResponse>> errorResponse = new ApiResponse<>(false,
                            "Failed to load addresses", null);
                    result.postValue(errorResponse);
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<List<AddressResponse>>> call, Throwable t) {
                ApiResponse<List<AddressResponse>> errorResponse = new ApiResponse<>(false, t.getMessage(), null);
                result.postValue(errorResponse);
            }
        });

        return result;
    }

    public LiveData<ApiResponse<AddressResponse>> getAddressById(String addressId) {
        MutableLiveData<ApiResponse<AddressResponse>> result = new MutableLiveData<>();

        apiService.getAddressById(Long.parseLong(addressId)).enqueue(new Callback<ApiResponse<AddressResponse>>() {
            @Override
            public void onResponse(Call<ApiResponse<AddressResponse>> call,
                    Response<ApiResponse<AddressResponse>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    result.postValue(response.body());
                } else {
                    ApiResponse<AddressResponse> errorResponse = new ApiResponse<>(false, "Address not found", null);
                    result.postValue(errorResponse);
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<AddressResponse>> call, Throwable t) {
                ApiResponse<AddressResponse> errorResponse = new ApiResponse<>(false, t.getMessage(), null);
                result.postValue(errorResponse);
            }
        });

        return result;
    }

    public LiveData<ApiResponse<AddressResponse>> createAddress(AddressRequest request) {
        MutableLiveData<ApiResponse<AddressResponse>> result = new MutableLiveData<>();

        apiService.createAddress(request).enqueue(new Callback<ApiResponse<AddressResponse>>() {
            @Override
            public void onResponse(Call<ApiResponse<AddressResponse>> call,
                    Response<ApiResponse<AddressResponse>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    result.postValue(response.body());
                } else {
                    ApiResponse<AddressResponse> errorResponse = new ApiResponse<>(false, "Failed to create address",
                            null);
                    result.postValue(errorResponse);
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<AddressResponse>> call, Throwable t) {
                ApiResponse<AddressResponse> errorResponse = new ApiResponse<>(false, t.getMessage(), null);
                result.postValue(errorResponse);
            }
        });

        return result;
    }

    public LiveData<ApiResponse<AddressResponse>> updateAddress(String addressId, AddressRequest request) {
        MutableLiveData<ApiResponse<AddressResponse>> result = new MutableLiveData<>();

        apiService.updateAddress(Long.parseLong(addressId), request)
                .enqueue(new Callback<ApiResponse<AddressResponse>>() {
                    @Override
                    public void onResponse(Call<ApiResponse<AddressResponse>> call,
                            Response<ApiResponse<AddressResponse>> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            result.postValue(response.body());
                        } else {
                            ApiResponse<AddressResponse> errorResponse = new ApiResponse<>(false,
                                    "Failed to update address",
                                    null);
                            result.postValue(errorResponse);
                        }
                    }

                    @Override
                    public void onFailure(Call<ApiResponse<AddressResponse>> call, Throwable t) {
                        ApiResponse<AddressResponse> errorResponse = new ApiResponse<>(false, t.getMessage(), null);
                        result.postValue(errorResponse);
                    }
                });

        return result;
    }

    public LiveData<ApiResponse<Void>> deleteAddress(String addressId) {
        MutableLiveData<ApiResponse<Void>> result = new MutableLiveData<>();

        apiService.deleteAddress(Long.parseLong(addressId)).enqueue(new Callback<ApiResponse<Void>>() {
            @Override
            public void onResponse(Call<ApiResponse<Void>> call, Response<ApiResponse<Void>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    result.postValue(response.body());
                } else {
                    ApiResponse<Void> errorResponse = new ApiResponse<>(false, "Failed to delete address", null);
                    result.postValue(errorResponse);
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<Void>> call, Throwable t) {
                ApiResponse<Void> errorResponse = new ApiResponse<>(false, t.getMessage(), null);
                result.postValue(errorResponse);
            }
        });

        return result;
    }

    public LiveData<ApiResponse<AddressResponse>> setDefaultAddress(String addressId) {
        MutableLiveData<ApiResponse<AddressResponse>> result = new MutableLiveData<>();

        apiService.setDefaultAddress(Long.parseLong(addressId)).enqueue(new Callback<ApiResponse<AddressResponse>>() {
            @Override
            public void onResponse(Call<ApiResponse<AddressResponse>> call,
                    Response<ApiResponse<AddressResponse>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    result.postValue(response.body());
                } else {
                    ApiResponse<AddressResponse> errorResponse = new ApiResponse<>(false,
                            "Failed to set default address", null);
                    result.postValue(errorResponse);
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<AddressResponse>> call, Throwable t) {
                ApiResponse<AddressResponse> errorResponse = new ApiResponse<>(false, t.getMessage(), null);
                result.postValue(errorResponse);
            }
        });

        return result;
    }
}
