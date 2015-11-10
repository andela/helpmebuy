package com.andela.helpmebuy.views;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;

import com.andela.helpmebuy.R;
import com.andela.helpmebuy.adapters.LocationAdapter;

import java.util.Arrays;
import java.util.List;

public class LocationView extends FrameLayout {

    RecyclerView recyclerView;

    LinearLayoutManager layoutManager;

    LocationAdapter adapter;

    public LocationView(Context context,AttributeSet attributeSet) {
        super(context, attributeSet);

        View view = LayoutInflater.from(getContext()).inflate(R.layout.location_view,this,false);
        addView(view);

        recyclerView = (RecyclerView)findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);

        List<String> locations = Arrays.asList("Nigeria", "Togo", "Japan", "USA");

        adapter = new LocationAdapter(getContext(), locations);

        recyclerView.setAdapter(adapter);

    }

    public void locationFilter() {

        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        };
    }
}
