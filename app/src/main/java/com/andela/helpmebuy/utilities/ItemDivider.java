package com.andela.helpmebuy.utilities;


import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.andela.helpmebuy.R;
import com.andela.helpmebuy.views.CustomRecyclerView;

public class ItemDivider extends RecyclerView.ItemDecoration {

    private Context context;

    public ItemDivider(Context context) {
        this.context = context;
    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        Paint paint = new Paint();
        paint.setColor(ContextCompat.getColor(context, R.color.divider_color));
        paint.setStrokeWidth(2.0f);

        CustomRecyclerView recyclerView = (CustomRecyclerView)parent;

        float padding = recyclerView.getDividerPadding();

        float startX = parent.getLeft() + padding;

        for (int i = 0, count = parent.getChildCount(); i < count; ++i) {
            View child = parent.getChildAt(i);

            float startY = child.getBottom();
            float stopX = child.getRight();

            c.drawLine(startX, startY, stopX, startY, paint);
        }
    }
}
