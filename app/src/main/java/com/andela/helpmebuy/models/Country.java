package com.andela.helpmebuy.models;

import android.os.Parcel;
import android.os.Parcelable;

public class Country extends Location implements Parcelable{

    public Country() {
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(getId());
        dest.writeString(getName());
    }

    public static final Creator<Country> CREATOR = new Creator<Country>() {
        public Country createFromParcel(Parcel in) {
            Country country = new Country();

            country.setId(in.readString());
            country.setName(in.readString());

            return country;
        }

        public Country[] newArray(int size) {
            return new Country[size];
        }
    };
}
