package com.andela.helpmebuy.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;

import com.andela.helpmebuy.R;

public class LocationView extends FrameLayout {

    public LocationView(Context context,AttributeSet attributeSet) {
        super(context,attributeSet);
        View view = LayoutInflater.from(getContext()).inflate(R.layout.location_view,this,false);
        addView(view);

    }

}
