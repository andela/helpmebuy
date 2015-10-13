package com.andela.helpmebuy.dal;


public interface DataCallback<T> {

    void onSuccess(T data);

    void onError(String errorMessage);
}
