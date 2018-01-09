package com.amaze.mqtt.actvities;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Build;
import android.preference.PreferenceManager;
import android.support.v4.widget.ContentLoadingProgressBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.widget.TextView;

import com.amaze.mqtt.POJO.Rasp;
import com.amaze.mqtt.R;
import com.amaze.mqtt.adapters.ChooseDeviceAdapter;
import com.amaze.mqtt.utils.Constants;
import com.amaze.mqtt.views.DividerItemDecoration;
import com.amaze.mqtt.views.EmptyRecyclerView;
import com.amaze.mqtt.utils.Utils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class ChooseDevice extends AppCompatActivity  {
    EmptyRecyclerView emptyRecyclerView;
    ChooseDeviceAdapter deviceAdapter;
    ArrayList<Rasp> deviceDetailsArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_device);
        HashMap<String, String> hashMap = new HashMap<String, String>();
        deviceDetailsArrayList = new ArrayList<>();
        hashMap.put(Constants.userId, PreferenceManager.getDefaultSharedPreferences(ChooseDevice.this).getString("username", null));
        hashMap.put(Constants.phoneId, Utils.getdeviceId(this));
        emptyRecyclerView = (EmptyRecyclerView) findViewById(R.id.recyclerView);
        emptyRecyclerView.setEmptyView(findViewById(R.id.emptyView), "No devices available", (TextView) findViewById(R.id.nodatatextview), (ContentLoadingProgressBar) findViewById(R.id.progressBar));
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        emptyRecyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL_LIST, true, true, R.drawable.divider));
        emptyRecyclerView.setLayoutManager(linearLayoutManager);
        deviceAdapter = new ChooseDeviceAdapter(this, deviceDetailsArrayList);
        emptyRecyclerView.setAdapter(deviceAdapter);
        deviceAdapter.setEditClickListener(new ChooseDeviceAdapter.OnClickListener() {
            @Override
            public void onClick(Rasp deviceDetails) {
            startMainActivity(deviceDetails);

            }
        });
        JSONObject jsonObject= null;
        try {
            jsonObject = new JSONObject(getIntent().getStringExtra("json"));

            deviceDetailsArrayList = new Gson().fromJson(jsonObject.get("ot").toString(), new TypeToken<ArrayList<Rasp>>() {
            }.getType());
            deviceAdapter.setDeviceDetailsArrayList(deviceDetailsArrayList);
            deviceAdapter.notifyDataSetChanged();

        } catch (JSONException e) {
            e.printStackTrace();
        }}

    void startMainActivity(Rasp deviceDetails) {

        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("deviceDetails",deviceDetails);
        // create the transition animation - the images in the layouts
        // of both activities are defined with android:transitionName="robot"
        if (Build.VERSION.SDK_INT >= 21) {
            ActivityOptions options = ActivityOptions
                    .makeSceneTransitionAnimation(this);
            // start the new activity
            startActivity(intent, options.toBundle());
        } else {
            startActivity(intent);
        }
        finish();
    }
}
