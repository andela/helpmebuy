package com.andela.helpmebuy.models;

import android.os.Parcel;
import android.os.Parcelable;

public class City extends AbsLocation implements Parcelable {

    private String regionId;

    private String countryId;

    public City() {
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeString(getId());
        parcel.writeString(getName());
        parcel.writeString(getRegionId());
        parcel.writeString(getCountryId());
    }

    public static final Parcelable.Creator<City> CREATOR =
            new Parcelable.Creator<City>() {
                public City createFromParcel(Parcel parcel) {
                    City city = new City();

                    city.setId(parcel.readString());
                    city.setName(parcel.readString());
                    city.setRegionId(parcel.readString());
                    city.setCountryId(parcel.readString());
                    return city;
                }

                public City[] newArray(int size) {
                    return new City[size];
                }
            };

    public String getRegionId() {
        return regionId;
    }

    public void setRegionId(String regionId) {
        this.regionId = regionId;
    }

    public String getCountryId() {
        return countryId;
    }

    public void setCountryId(String countryId) {
        this.countryId = countryId;
    }
}
