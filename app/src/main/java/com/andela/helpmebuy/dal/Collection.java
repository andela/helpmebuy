package com.andela.helpmebuy.dal;


import java.util.List;

public interface Collection<T> {

    void save(T data, DataCallback<T> callback);

    void get(String id, DataCallback<T> callback);

    void getAll(DataCallback<List<T>> callback);

    void query(String[] selection, String[] selectionArgs, DataCallback<List<T>> callback);
}
