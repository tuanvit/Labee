package com.example.lazabee.data.repository;

import android.content.Context;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import com.example.lazabee.data.local.TokenManager;
import com.example.lazabee.data.model.ApiResponse;
import com.example.lazabee.data.model.auth.AuthResponse;
import com.example.lazabee.data.model.auth.LoginRequest;
import com.example.lazabee.data.model.auth.RegisterRequest;
import com.example.lazabee.data.remote.ApiService;
import com.example.lazabee.data.remote.RetrofitClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AuthRepository {

    private ApiService apiService;
    private TokenManager tokenManager;

    public AuthRepository(Context context) {
        this.apiService = RetrofitClient.getInstance(context).getApiService();
        this.tokenManager = TokenManager.getInstance(context);
    }

    public LiveData<ApiResponse<AuthResponse>> login(String email, String password) {
        MutableLiveData<ApiResponse<AuthResponse>> result = new MutableLiveData<>();

        LoginRequest request = new LoginRequest(email, password);
        apiService.login(request).enqueue(new Callback<ApiResponse<AuthResponse>>() {
            @Override
            public void onResponse(Call<ApiResponse<AuthResponse>> call, Response<ApiResponse<AuthResponse>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    ApiResponse<AuthResponse> apiResponse = response.body();
                    if (apiResponse.isSuccess() && apiResponse.getData() != null) {
                        AuthResponse authData = apiResponse.getData();
                        tokenManager.saveToken(authData.getAccessToken());
                        tokenManager.saveUserId(Long.parseLong(authData.getUserId()));
                        tokenManager.saveEmail(authData.getEmail());
                    }
                    result.postValue(apiResponse);
                } else {
                    ApiResponse<AuthResponse> errorResponse = new ApiResponse<>(false, "Login failed", null);
                    result.postValue(errorResponse);
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<AuthResponse>> call, Throwable t) {
                ApiResponse<AuthResponse> errorResponse = new ApiResponse<>(false, t.getMessage(), null);
                result.postValue(errorResponse);
            }
        });

        return result;
    }

    public LiveData<ApiResponse<AuthResponse>> register(String username, String email, String password, String fullName,
            String phoneNumber) {
        MutableLiveData<ApiResponse<AuthResponse>> result = new MutableLiveData<>();

        RegisterRequest request = new RegisterRequest(username, email, password, fullName, phoneNumber);
        apiService.register(request).enqueue(new Callback<ApiResponse<AuthResponse>>() {
            @Override
            public void onResponse(Call<ApiResponse<AuthResponse>> call, Response<ApiResponse<AuthResponse>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    ApiResponse<AuthResponse> apiResponse = response.body();
                    if (apiResponse.isSuccess() && apiResponse.getData() != null) {
                        AuthResponse authData = apiResponse.getData();
                        tokenManager.saveToken(authData.getAccessToken());
                        tokenManager.saveUserId(Long.parseLong(authData.getUserId()));
                        tokenManager.saveEmail(authData.getEmail());
                    }
                    result.postValue(apiResponse);
                } else {
                    ApiResponse<AuthResponse> errorResponse = new ApiResponse<>(false, "Registration failed", null);
                    result.postValue(errorResponse);
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<AuthResponse>> call, Throwable t) {
                ApiResponse<AuthResponse> errorResponse = new ApiResponse<>(false, t.getMessage(), null);
                result.postValue(errorResponse);
            }
        });

        return result;
    }

    public boolean isLoggedIn() {
        return tokenManager.isLoggedIn();
    }

    public void logout() {
        tokenManager.logout();
    }

}
