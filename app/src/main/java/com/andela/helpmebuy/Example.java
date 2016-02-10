package com.andela.helpmebuy;


import com.andela.helpmebuy.authentication.AuthCallback;
import com.andela.helpmebuy.authentication.EmailPasswordAuth;

public class Example implements EmailPasswordAuth {
    public void sayHello() {
        System.out.println("Hello");
    }

    @Override
    public void signIn(String email, String password, AuthCallback callback) {

    }

    @Override
    public void signUp(String email, String password, AuthCallback callback) {

    }

    @Override
    public void signOut() {

    }
}
