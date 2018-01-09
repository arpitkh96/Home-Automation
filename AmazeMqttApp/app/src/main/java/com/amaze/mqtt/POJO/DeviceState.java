package com.amaze.mqtt.POJO;

import java.util.Objects;

/**
 * Created by arpitkh996 on 08-09-2016.
 */

public class DeviceState {
    String edeviceid;
    Integer state;
    String ardid;
    Integer pin;

    public Integer getPin() {
        return pin;
    }

    public void setPin(Integer pin) {
        this.pin = pin;
    }

    public String getDeviceId() {
        return edeviceid;
    }

    public void setDeviceId(String deviceId) {
        this.edeviceid = deviceId;
    }

    public Integer getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public String getArdid() {
        return ardid;
    }

    public void setArdid(String ardid) {
        this.ardid = ardid;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final DeviceState other = (DeviceState) obj;
        if (!(this.edeviceid.equals( other.edeviceid))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "DeviceState{" + "deviceId=" + edeviceid + ", state=" + state + '}';
    }


}
