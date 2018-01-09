package com.amaze.mqtt.actvities;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.v4.widget.ContentLoadingProgressBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatCheckBox;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Toast;

import com.amaze.mqtt.R;
import com.amaze.mqtt.utils.ALog;
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

public class LoginActivity extends AppCompatActivity implements VolleyInterface {
    ContentLoadingProgressBar progressBar;
    AppCompatButton loginButton;
    TextInputEditText user,pass;
    View main;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.login);
        main=findViewById(R.id.main);
        progressBar = (ContentLoadingProgressBar) findViewById(R.id.progressBar);
        user= (TextInputEditText) findViewById(R.id.username);
        pass = (TextInputEditText) findViewById(R.id.password);
        loginButton = (AppCompatButton) findViewById(R.id.login_button);
        progressBar.hide();
        AppCompatCheckBox compatCheckBox=(AppCompatCheckBox)findViewById(R.id.offline_mode);
        compatCheckBox.setChecked(URL_API.offline);
        compatCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                URL_API.offline=isChecked;
                ALog.d("offline",URL_API.offline+"");
                PreferenceManager.getDefaultSharedPreferences(LoginActivity.this).edit().putBoolean("offline",URL_API.offline).commit();
            }
        });
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               String username = user.getText().toString();
                String password = pass.getText().toString();
                PreferenceManager.getDefaultSharedPreferences(LoginActivity.this).edit().putString("username", username).commit();
                String deviceId = Utils.getdeviceId(LoginActivity.this);
                HashMap<String, String> hashMap = new HashMap<String, String>();
                hashMap.put(Constants.userId, username);
                hashMap.put(Constants.password, password);
                hashMap.put(Constants.deviceId, deviceId);
                progressBar.show();
                VolleyHelper.postRequestVolley(LoginActivity.this, URL_API.getLoginApi(), hashMap, RequestCodes.LOGIN_REQ);
            }
        });
    }

    @Override
    public void requestStarted(int requestCode) {

        loginButton.setEnabled(false);
        progressBar.show();
    }

    @Override
    public void requestCompleted(int requestCode, String response) {
        ALog.d("completed" + "respomse" + response);
        progressBar.hide();
        loginButton.setEnabled(true);
        if (requestCode == RequestCodes.LOGIN_REQ) {
            try {
                JSONObject jsonObject = new JSONObject(response);
                if (jsonObject.getInt("rs") == 1) {
                    Intent intent = new Intent(this, ChooseDevice.class);
                    intent.putExtra("json",response);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        startActivity(intent,
                                ActivityOptions.makeSceneTransitionAnimation(this).toBundle());
                    } else startActivity(intent);
                }
                else Snackbar.make(main,jsonObject.getString("message"),Snackbar.LENGTH_SHORT).show();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void requestEndedWithError(int requestCode, VolleyError error) {
        error.printStackTrace();
        progressBar.hide();
        loginButton.setEnabled(true);
    }
}
