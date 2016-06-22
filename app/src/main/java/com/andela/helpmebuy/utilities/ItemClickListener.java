package com.andela.helpmebuy.utilities;

import android.view.View;

/**
 * Created by andeladev on 08/06/2016.
 */
public interface ItemClickListener {
    void rejectItem(View view, int position);
    void acceptItem(View view, int position);
}
