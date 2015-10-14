package com.andela.helpmebuy.authentication;


import com.andela.helpmebuy.models.User;

public interface AuthCallback {

    void onSuccess(User user);

    void onCancel();

    void onError(String errorMessage);

    void onFailure(Exception e);
}
