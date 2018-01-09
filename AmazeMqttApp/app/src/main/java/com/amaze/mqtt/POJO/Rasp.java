package com.amaze.mqtt.POJO;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by arpitkh996 on 07-09-2016.
 */

public class Rasp implements Parcelable{
    String deviceid;
    String name;
    String desc;

    public String getDeviceid() {
        return deviceid;
    }

    public void setDeviceid(String deviceid) {
        this.deviceid = deviceid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    protected Rasp(Parcel in) {
        deviceid = in.readString();

        name = in.readString();
        desc = in.readString();
    }

    public static final Creator<Rasp> CREATOR = new Creator<Rasp>() {
        @Override
        public Rasp createFromParcel(Parcel in) {
            return new Rasp(in);
        }

        @Override
        public Rasp[] newArray(int size) {
            return new Rasp[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(deviceid);
        dest.writeString(name);
        dest.writeString(desc);
    }
}

