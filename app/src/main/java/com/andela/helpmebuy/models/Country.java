package com.andela.helpmebuy.models;

import android.os.Parcel;
import android.os.Parcelable;

public class Country extends Location implements Parcelable{
    private int data;
    public Country(Parcel in) {
        data = in.readInt();
    }

    public static final Creator<Country> CREATOR = new Creator<Country>() {
        @Override
        public Country createFromParcel(Parcel in) {
            Country country = new Country();
            country.setId(in.readString());
            country.setName(in.readString());

            return country;
        }

        @Override
        public Country[] newArray(int size) {
            return new Country[size];
        }
    };

    public Country() {

    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(getId());
        dest.writeString(getName());
    }
}
