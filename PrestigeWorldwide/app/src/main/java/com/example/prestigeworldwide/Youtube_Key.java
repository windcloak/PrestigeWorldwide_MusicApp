package com.example.prestigeworldwide;

public class Youtube_Key {
    public Youtube_Key() {
    }

    private final static String API_Key = BuildConfig.ApiKey;

    public static String getAPI_Key() {
        return API_Key;
    }
}
