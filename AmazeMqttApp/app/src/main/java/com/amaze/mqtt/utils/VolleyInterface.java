package com.amaze.mqtt.utils;

import com.android.volley.VolleyError;

/**
 * Created by Neeraj on 05-12-2015.
 */
public interface VolleyInterface {

    void requestStarted(int requestCode);

    void requestCompleted(int requestCode, String response);

    void requestEndedWithError(int requestCode, VolleyError error);

}
