package com.amaze.mqtt.utils;

/**
 * Created by arpitkh996 on 07-09-2016.
 */

public class URL_API {
    public static boolean offline=false;
    public static final String test="http://35.199.185.104:8000";
    public static final String local="http://192.168.1.38:8080";
    public static final String mqtt_broker="tcp://35.199.185.104:1883";
    public static final String subscribe_group="/devices/pub/";
    public static final String online_status_group="/online/";
    public static final String send_group="/devices/sub/";
    public static String baseUrl=test;

    public static final String LOGIN_API="/login";
    public static final String LOGOUT_API="/logout";
    public static final String CHECK_LOGIN_API="/isLoggedIn";
    public static final String GET_ELECTRONICS_API="/devices";

    public static String getMqtt_broker() {
        if(offline)
            return "tcp://192.168.1.38:1883";
        return mqtt_broker;
    }

    public static String getLoginApi() {
        if(offline)
            baseUrl=local;
        else baseUrl=test;
        return baseUrl+LOGIN_API;
    }

    public static String getLogoutApi() {
        if(offline)
            baseUrl=local;
        else baseUrl=test;
        return baseUrl+LOGOUT_API;
    }

    public static String getCheckLoginApi() {
        if(offline)
            baseUrl=local;
        else baseUrl=test;
        return baseUrl+CHECK_LOGIN_API;
    }

    public static String getGetElectronicsApi() {
        if(offline)
            baseUrl=local;
        else baseUrl=test;
        return baseUrl+GET_ELECTRONICS_API;
    }
}
