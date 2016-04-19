package com.andela.helpmebuy.views;


import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;

import com.andela.helpmebuy.R;

public class CustomRecyclerView extends RecyclerView {

    private float dividerPadding;

    public float getDividerPadding() {
        return dividerPadding;
    }

    public CustomRecyclerView(Context context) {
        super(context);
    }

    public CustomRecyclerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setDividerPadding(context);
    }

    public CustomRecyclerView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setDividerPadding(context);
    }

    private void setDividerPadding(Context context) {
        dividerPadding = dimenToInt(context);
    }

    private int dimenToInt(Context context) {
        Resources res = context.getResources();
        return res.getDimensionPixelSize(R.dimen.profile_picture_width)
                + res.getDimensionPixelSize(R.dimen.activity_horizontal_margin);
    }
}
