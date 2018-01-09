package com.amaze.mqtt.actvities;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v4.widget.ContentLoadingProgressBar;
import android.support.v7.app.AppCompatActivity;

import com.amaze.mqtt.R;
import com.amaze.mqtt.utils.Constants;
import com.amaze.mqtt.utils.RequestCodes;
import com.amaze.mqtt.utils.URL_API;
import com.amaze.mqtt.utils.Utils;
import com.amaze.mqtt.utils.VolleyHelper;
import com.amaze.mqtt.utils.VolleyInterface;
import com.android.volley.VolleyError;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class SplashActivity extends AppCompatActivity implements VolleyInterface {
    String username;
    ContentLoadingProgressBar contentLoadingProgressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_activity);
        contentLoadingProgressBar=(ContentLoadingProgressBar)findViewById(R.id.progressBar);
        contentLoadingProgressBar.show();
        username=PreferenceManager.getDefaultSharedPreferences(this).getString("username",null);
        if(username==null){
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    startLoginActivity();
                }
            }, 1000);
        }else {
            String deviceId = Utils.getdeviceId(this);
            HashMap<String, String> hashMap = new HashMap<String, String>();
            hashMap.put(Constants.userId, username);
            hashMap.put(Constants.deviceId, deviceId);
            VolleyHelper.postRequestVolley(this, URL_API.getCheckLoginApi(), hashMap, RequestCodes.CHECK_LOGIN);

        }

    }

    void startLoginActivity() {

        Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
        // create the transition animation - the images in the layouts
        // of both activities are defined with android:transitionName="robot"
        if (Build.VERSION.SDK_INT >= 21) {
            ActivityOptions options = ActivityOptions
                    .makeSceneTransitionAnimation(SplashActivity.this, findViewById(R.id.title), "title");
            // start the new activity
            startActivity(intent, options.toBundle());
        } else {
            startActivity(intent);
        }
    }

    @Override
    public void requestStarted(int requestCode) {

    }

    @Override
    public void requestCompleted(int requestCode, String response) {
        try {
            JSONObject jsonObject=new JSONObject(response);
            if(jsonObject.getInt("rs")==1){
                Intent intent = new Intent(this, ChooseDevice.class);
                intent.putExtra("json",response);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    startActivity(intent,
                            ActivityOptions.makeSceneTransitionAnimation(this).toBundle());
                } else startActivity(intent);
            }
            else startLoginActivity();
        } catch (JSONException e) {
            e.printStackTrace();
            startLoginActivity();
        }
    }

    @Override
    public void requestEndedWithError(int requestCode, VolleyError error) {
    startLoginActivity();
    }
}
