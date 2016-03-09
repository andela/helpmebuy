package com.andela.helpmebuy.utilities;

import android.widget.Button;

import com.andela.helpmebuy.models.Travel;

public interface CurrentTravelListener {
    void getCurrentTravel(Button connectButton, Travel travel);
}
