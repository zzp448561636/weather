package com.example.zeng.weather.data;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Zeng on 2016/7/31.
 */
public class CityInfo implements Parcelable {
    private int cityId;
    private String cityName;
    private int cityOrder;

    public CityInfo(int cityId,String cityName,int cityOrder){
        this.cityId = cityId;
        this.cityName = cityName;
        this.cityOrder = cityOrder;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    /**
     * Parcel中的数据是按顺序写入和读取的，即先被写入的就会先被读取出来
     * @param parcel
     * @param i
     */
    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(cityId);
        parcel.writeString(cityName);
        parcel.writeInt(cityOrder);
    }

    public int getCityId() {
        return cityId;
    }

    public String getCityName() {
        return cityName;
    }

    public int getCityOrder() {
        return cityOrder;
    }

    public static final Parcelable.Creator<CityInfo> CREATOR = new Parcelable.Creator<CityInfo>() {
        @Override
        public CityInfo createFromParcel(Parcel parcel) {
            int cityId = parcel.readInt();
            String cityName = parcel.readString();
            int cityOrder = parcel.readInt();
            CityInfo info = new CityInfo(cityId,cityName,cityOrder);
            return info;
        }

        @Override
        public CityInfo[] newArray(int i) {
            return new CityInfo[i];
        }
    };
}
