package com.example.lazabee.viewmodel;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.lazabee.data.model.ApiResponse;
import com.example.lazabee.data.model.auth.AuthResponse;
import com.example.lazabee.data.repository.AuthRepository;

public class AuthViewModel extends AndroidViewModel {
    
    private AuthRepository authRepository;
    
    public AuthViewModel(@NonNull Application application) {
        super(application);
        authRepository = new AuthRepository(application);
    }
    
    public LiveData<ApiResponse<AuthResponse>> login(String email, String password) {
        return authRepository.login(email, password);
    }
    
    public LiveData<ApiResponse<AuthResponse>> register(String username, String email, String password, String fullName, String phoneNumber) {
        return authRepository.register(username, email, password, fullName, phoneNumber);
    }
    
    public LiveData<ApiResponse<AuthResponse>> getProfile() {
        return authRepository.getProfile();
    }
    
    public boolean isLoggedIn() {
        return authRepository.isLoggedIn();
    }
    
    public void logout() {
        authRepository.logout();
    }
}
