package com.example.lazabee.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.lazabee.model.User;
import com.google.gson.Gson;

public class SharedPreferencesManager {

    private static final String PREF_NAME = "lazabee_prefs";
    private static final String KEY_TOKEN = "auth_token";
    private static final String KEY_USER = "user_data";
    private static final String KEY_IS_LOGGED_IN = "is_logged_in";

    private SharedPreferences sharedPreferences;
    private Gson gson;

    public SharedPreferencesManager(Context context) {
        this.gson = new Gson();

        // Use regular SharedPreferences for compatibility
        sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);

        // TODO: Implement encryption later if needed for production
    }

    public void saveToken(String token) {
        sharedPreferences.edit()
                .putString(KEY_TOKEN, token)
                .putBoolean(KEY_IS_LOGGED_IN, true)
                .apply();
    }

    public String getToken() {
        return sharedPreferences.getString(KEY_TOKEN, null);
    }

    public void saveUser(User user) {
        String userJson = gson.toJson(user);
        sharedPreferences.edit()
                .putString(KEY_USER, userJson)
                .apply();
    }

    public User getUser() {
        String userJson = sharedPreferences.getString(KEY_USER, null);
        if (userJson != null) {
            return gson.fromJson(userJson, User.class);
        }
        return null;
    }

    public boolean isLoggedIn() {
        return sharedPreferences.getBoolean(KEY_IS_LOGGED_IN, false);
    }

    public void clearAll() {
        sharedPreferences.edit().clear().apply();
    }
}