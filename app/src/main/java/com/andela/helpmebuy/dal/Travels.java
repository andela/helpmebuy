package com.andela.helpmebuy.dal;


import com.andela.helpmebuy.models.Travel;

import java.util.List;

public interface Travels {

    void save(Travel travel, DataCallback<Travel> callback);

    void get(String id, DataCallback<Travel> callback);

    void getAll(DataCallback<List<Travel>> callback);

    void query(String[] selection, String[] selectionArgs, DataCallback<List<Travel>> callback);
}
