package com.andela.helpmebuy.authentication;

public interface EmailPasswordAuth {

    void signIn(String email, String password, AuthCallback callback);

    void signUp(String email, String password, AuthCallback callback);

    void signOut();
}
