package com.andela.helpmebuy.authentication;

/**
 * Created by GRACE on 1/18/2016.
 */
public interface ChangeEmailAuth {
    void changeEmail(String oldEmail, String newEmail, String password, ChangeEmailCallback callback);
}
