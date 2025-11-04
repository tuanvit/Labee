package com.example.lazabee.data.repository;

import android.content.Context;

import com.example.lazabee.data.local.AppDatabase;
import com.example.lazabee.data.local.UserDao;
import com.example.lazabee.data.model.LoginRequest;
import com.example.lazabee.data.model.LoginResponse;
import com.example.lazabee.data.model.User;
import com.example.lazabee.data.remote.ApiClient;
import com.example.lazabee.data.remote.ApiService;
import com.example.lazabee.utils.SharedPreferencesManager;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Single;

public class AuthRepository {
    
    private ApiService apiService;
    private UserDao userDao;
    private SharedPreferencesManager prefsManager;
    
    public AuthRepository(Context context) {
        this.apiService = ApiClient.getApiService();
        this.userDao = AppDatabase.getInstance(context).userDao();
        this.prefsManager = new SharedPreferencesManager(context);
    }
    
    public Single<LoginResponse> login(String email, String password) {
        LoginRequest request = new LoginRequest(email, password);
        return apiService.login(request)
                .doOnSuccess(response -> {
                    if (response.isSuccess()) {
                        // Lưu token và user info
                        prefsManager.saveToken(response.getToken());
                        prefsManager.saveUser(response.getUser());
                        
                        // Cache user vào local database
                        userDao.insertUser(response.getUser()).subscribe();
                    }
                });
    }
    
    public Single<LoginResponse> register(User user) {
        return apiService.register(user)
                .doOnSuccess(response -> {
                    if (response.isSuccess()) {
                        prefsManager.saveToken(response.getToken());
                        prefsManager.saveUser(response.getUser());
                        userDao.insertUser(response.getUser()).subscribe();
                    }
                });
    }
    
    public Single<LoginResponse> forgotPassword(String email) {
        return apiService.forgotPassword(email);
    }
    
    public Single<LoginResponse> verifyCode(String code) {
        return apiService.verifyCode(code);
    }
    
    public Single<LoginResponse> resetPassword(String newPassword) {
        return apiService.resetPassword(newPassword);
    }
    
    public Completable logout() {
        String token = prefsManager.getToken();
        if (token != null) {
            return apiService.logout("Bearer " + token)
                    .doOnSuccess(response -> clearUserData())
                    .ignoreElement();
        } else {
            clearUserData();
            return Completable.complete();
        }
    }
    
    public boolean isLoggedIn() {
        return prefsManager.getToken() != null;
    }
    
    public User getCurrentUser() {
        return prefsManager.getUser();
    }
    
    private void clearUserData() {
        prefsManager.clearAll();
        userDao.deleteAllUsers().subscribe();
    }
}