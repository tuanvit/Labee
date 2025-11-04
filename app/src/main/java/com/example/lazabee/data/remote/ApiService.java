package com.example.lazabee.data.remote;

import com.example.lazabee.data.model.LoginRequest;
import com.example.lazabee.data.model.LoginResponse;
import com.example.lazabee.data.model.User;

import io.reactivex.rxjava3.core.Single;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface ApiService {
    
    @POST("auth/login")
    Single<LoginResponse> login(@Body LoginRequest loginRequest);
    
    @POST("auth/register")
    Single<LoginResponse> register(@Body User user);
    
    @POST("auth/forgot-password")
    Single<LoginResponse> forgotPassword(@Body String email);
    
    @POST("auth/verify-code")
    Single<LoginResponse> verifyCode(@Body String code);
    
    @POST("auth/reset-password")
    Single<LoginResponse> resetPassword(@Body String newPassword);
    
    @GET("auth/profile")
    Single<User> getProfile(@Header("Authorization") String token);
    
    @POST("auth/logout")
    Single<LoginResponse> logout(@Header("Authorization") String token);
}