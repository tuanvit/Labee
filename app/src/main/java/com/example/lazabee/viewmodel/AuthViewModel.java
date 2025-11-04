package com.example.lazabee.viewmodel;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.lazabee.data.model.LoginResponse;
import com.example.lazabee.data.model.User;
import com.example.lazabee.data.repository.AuthRepository;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class AuthViewModel extends AndroidViewModel {
    
    private AuthRepository authRepository;
    private CompositeDisposable compositeDisposable;
    
    // LiveData for UI observation
    private MutableLiveData<LoginResponse> loginResult = new MutableLiveData<>();
    private MutableLiveData<LoginResponse> registerResult = new MutableLiveData<>();
    private MutableLiveData<LoginResponse> forgotPasswordResult = new MutableLiveData<>();
    private MutableLiveData<LoginResponse> verifyCodeResult = new MutableLiveData<>();
    private MutableLiveData<LoginResponse> resetPasswordResult = new MutableLiveData<>();
    private MutableLiveData<Boolean> logoutResult = new MutableLiveData<>();
    private MutableLiveData<Boolean> isLoading = new MutableLiveData<>();
    private MutableLiveData<String> errorMessage = new MutableLiveData<>();
    
    public AuthViewModel(@NonNull Application application) {
        super(application);
        authRepository = new AuthRepository(application);
        compositeDisposable = new CompositeDisposable();
    }
    
    public void login(String email, String password) {
        isLoading.setValue(true);
        
        compositeDisposable.add(
            authRepository.login(email, password)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    response -> {
                        isLoading.setValue(false);
                        loginResult.setValue(response);
                    },
                    throwable -> {
                        isLoading.setValue(false);
                        errorMessage.setValue(throwable.getMessage());
                    }
                )
        );
    }
    
    public void register(String username, String email, String password, String fullName, String phone) {
        isLoading.setValue(true);
        
        User user = new User(username, email, password, fullName, phone);
        
        compositeDisposable.add(
            authRepository.register(user)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    response -> {
                        isLoading.setValue(false);
                        registerResult.setValue(response);
                    },
                    throwable -> {
                        isLoading.setValue(false);
                        errorMessage.setValue(throwable.getMessage());
                    }
                )
        );
    }
    
    public void forgotPassword(String email) {
        isLoading.setValue(true);
        
        compositeDisposable.add(
            authRepository.forgotPassword(email)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    response -> {
                        isLoading.setValue(false);
                        forgotPasswordResult.setValue(response);
                    },
                    throwable -> {
                        isLoading.setValue(false);
                        errorMessage.setValue(throwable.getMessage());
                    }
                )
        );
    }
    
    public void verifyCode(String code) {
        isLoading.setValue(true);
        
        compositeDisposable.add(
            authRepository.verifyCode(code)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    response -> {
                        isLoading.setValue(false);
                        verifyCodeResult.setValue(response);
                    },
                    throwable -> {
                        isLoading.setValue(false);
                        errorMessage.setValue(throwable.getMessage());
                    }
                )
        );
    }
    
    public void resetPassword(String newPassword) {
        isLoading.setValue(true);
        
        compositeDisposable.add(
            authRepository.resetPassword(newPassword)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    response -> {
                        isLoading.setValue(false);
                        resetPasswordResult.setValue(response);
                    },
                    throwable -> {
                        isLoading.setValue(false);
                        errorMessage.setValue(throwable.getMessage());
                    }
                )
        );
    }
    
    public void logout() {
        isLoading.setValue(true);
        
        compositeDisposable.add(
            authRepository.logout()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    () -> {
                        isLoading.setValue(false);
                        logoutResult.setValue(true);
                    },
                    throwable -> {
                        isLoading.setValue(false);
                        errorMessage.setValue(throwable.getMessage());
                    }
                )
        );
    }
    
    public boolean isLoggedIn() {
        return authRepository.isLoggedIn();
    }
    
    public User getCurrentUser() {
        return authRepository.getCurrentUser();
    }
    
    // Getters for LiveData
    public MutableLiveData<LoginResponse> getLoginResult() { return loginResult; }
    public MutableLiveData<LoginResponse> getRegisterResult() { return registerResult; }
    public MutableLiveData<LoginResponse> getForgotPasswordResult() { return forgotPasswordResult; }
    public MutableLiveData<LoginResponse> getVerifyCodeResult() { return verifyCodeResult; }
    public MutableLiveData<LoginResponse> getResetPasswordResult() { return resetPasswordResult; }
    public MutableLiveData<Boolean> getLogoutResult() { return logoutResult; }
    public MutableLiveData<Boolean> getIsLoading() { return isLoading; }
    public MutableLiveData<String> getErrorMessage() { return errorMessage; }
    
    @Override
    protected void onCleared() {
        super.onCleared();
        compositeDisposable.clear();
    }
}