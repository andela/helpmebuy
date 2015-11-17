package com.andela.helpmebuy.models;

import android.os.Parcel;
import android.os.Parcelable;

public class Country extends Location implements Parcelable{
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

    }
}
