package com.andela.helpmebuy.dal;


import com.andela.helpmebuy.models.User;

import java.util.List;

public interface Users {

    void save(User user, DataCallback<User> callback);

    void get(String id, DataCallback<User> callback);

    void getAll(DataCallback<List<User>> callback);

    void query(String[] selection, String[] selectionArgs, DataCallback<List<User>> callback);
}
