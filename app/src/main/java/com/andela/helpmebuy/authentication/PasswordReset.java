package com.andela.helpmebuy.authentication;

/**
 * Created by Daniel James on 10/14/2015.
 */
public interface PasswordReset {

    public void changePassword(String email, String oldPassword, String newPassword, AuthCallback authCallback);

    public void sendTemporaryPassword(String email, AuthCallback authCallback);
}
