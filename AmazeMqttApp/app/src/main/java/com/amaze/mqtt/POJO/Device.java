package com.amaze.mqtt.POJO;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Device {

    @SerializedName("edeviceid")
    @Expose
    private String edeviceid;
    @SerializedName("ardid")
    @Expose
    private String ardid;
    @SerializedName("pin")
    @Expose
    private Integer pin;
    @SerializedName("desc1")
    @Expose
    private String desc1;
    @SerializedName("desc2")
    @Expose
    private String desc2;
    @SerializedName("state")
    @Expose
    private Integer state;
    @SerializedName("pstates")
    @Expose
    private String pstates;

    public String getEdeviceid() {
        return edeviceid;
    }

    public void setEdeviceid(String edeviceid) {
        this.edeviceid = edeviceid;
    }

    public String getArdid() {
        return ardid;
    }

    public void setArdid(String ardid) {
        this.ardid = ardid;
    }

    public Integer getPin() {
        return pin;
    }

    public void setPin(Integer pin) {
        this.pin = pin;
    }

    public String getDesc1() {
        return desc1;
    }

    public void setDesc1(String desc1) {
        this.desc1 = desc1;
    }

    public String getDesc2() {
        return desc2;
    }

    public void setDesc2(String desc2) {
        this.desc2 = desc2;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public String getPstates() {
        return pstates;
    }

    public void setPstates(String pstates) {
        this.pstates = pstates;
    }

}