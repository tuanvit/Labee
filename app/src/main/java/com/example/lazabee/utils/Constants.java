package com.example.lazabee.utils;

public class Constants {
    // Development - Android Emulator
    public static final String BASE_URL = "http://10.0.2.2:8080/";

    // For physical device, use your computer's IP address
    // public static final String BASE_URL = "http://192.168.1.x:8080/";

    // SharedPreferences
    public static final String PREF_NAME = "lazabee_prefs";
    public static final String PREF_TOKEN_KEY = "jwt_token";
    public static final String PREF_USER_ID = "user_id";

    // Payment Methods - MUST match backend enum
    public static final String PAYMENT_COD = "COD";
    public static final String PAYMENT_BANK_TRANSFER = "BANK_TRANSFER";
    public static final String PAYMENT_CREDIT_CARD = "CREDIT_CARD";
    public static final String PAYMENT_E_WALLET = "E_WALLET";

    // Private constructor to prevent instantiation
    private Constants() {
        throw new AssertionError("Cannot instantiate Constants class");
    }
}
