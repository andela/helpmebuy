package com.andela.helpmebuy.utilities;


public interface HttpCallback {

    void onSuccess(String result);

    void onError(String message);
}
