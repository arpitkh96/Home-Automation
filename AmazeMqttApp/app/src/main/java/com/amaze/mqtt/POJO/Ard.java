package com.amaze.mqtt.POJO;

import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Ard {

    @SerializedName("rasp")
    @Expose
    private String rasp;
    @SerializedName("ardid")
    @Expose
    private String ardid;
    @SerializedName("devices")
    @Expose
    private List<Device> devices = null;

    public String getRasp() {
        return rasp;
    }

    public void setRasp(String rasp) {
        this.rasp = rasp;
    }

    public String getArdid() {
        return ardid;
    }

    public void setArdid(String ardid) {
        this.ardid = ardid;
    }

    public List<Device> getDevices() {
        return devices;
    }

    public void setDevices(List<Device> devices) {
        this.devices = devices;
    }

}