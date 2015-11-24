package com.andela.helpmebuy.models;

import android.os.Parcel;
import android.os.Parcelable;

public class Region extends AbsLocation implements Parcelable {

    private String countryId;

    public Region() {
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeString(getId());
        parcel.writeString(getName());
        parcel.writeSerializable(getCountryId());
    }

    public static final Parcelable.Creator<Region> CREATOR =
            new Parcelable.Creator<Region>() {
                public Region createFromParcel(Parcel parcel) {
                    Region region = new Region();

                    region.setId(parcel.readString());
                    region.setName(parcel.readString());
                    region.setCountryId(parcel.readString());

                    return region;
                }

                public Region[] newArray(int size){
                    return new Region[size];
                }
            };

    public String getCountryId() {
        return countryId;
    }

    public void setCountryId(String countryId) {
        this.countryId = countryId;
    }

}
