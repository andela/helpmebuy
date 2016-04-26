package com.andela.helpmebuy.utilities;


import com.andela.helpmebuy.models.Model;

import java.util.List;

public interface ListCallback<T extends Model> {
    void onGetList(List<T> items);
}
