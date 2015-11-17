package com.andela.helpmebuy.utilities;

import android.widget.Filter;

import com.andela.helpmebuy.models.Model;

import java.util.List;

public abstract class ModelFilter<T extends Model> extends Filter {
    protected List<T> initialValue;

    protected List<T> filteredResult;
}
