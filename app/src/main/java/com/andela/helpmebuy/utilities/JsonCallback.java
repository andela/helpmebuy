package com.andela.helpmebuy.utilities;


import com.andela.helpmebuy.authentication.AuthCallback;

public interface JsonCallback {
    void getJson(String url, HttpCallback httpCallback);
}
