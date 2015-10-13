package com.andela.helpmebuy.authentication;

import com.andela.helpmebuy.models.User;

public interface AuthCallback {
    public void onSuccess(User user);

    public void onError(String string);

    public void onFailure(Exception e);
}
