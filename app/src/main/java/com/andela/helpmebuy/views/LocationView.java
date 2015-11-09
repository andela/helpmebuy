package com.andela.helpmebuy.views;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;

import com.andela.helpmebuy.R;

public class LocationView extends FrameLayout {

    RecyclerView locationRecyclerView;

    public LocationView(Context context,AttributeSet attributeSet) {
        super(context, attributeSet);
        View view = LayoutInflater.from(getContext()).inflate(R.layout.location_view,this,false);
        addView(view);
        locationRecyclerView = (RecyclerView)findViewById(R.id.locationRecycler);
    }



}
