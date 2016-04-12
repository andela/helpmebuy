package com.andela.helpmebuy.views;


import android.content.Context;
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
        setDividerPadding(context, attrs);
    }

    public CustomRecyclerView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setDividerPadding(context, attrs);
    }

    private void setDividerPadding(Context context, AttributeSet attrs) {
        TypedArray typedArray
                = context.obtainStyledAttributes(attrs, R.styleable.TogglePasswordVisibilityButton);
        try {
            dividerPadding = typedArray.getFloat(R.styleable.TogglePasswordVisibilityButton_divider_Padding, 0.0f);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            typedArray.recycle();
        }
    }
}
