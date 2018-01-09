package com.amaze.mqtt.actvities;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.speech.RecognizerIntent;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.ContentLoadingProgressBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.amaze.mqtt.POJO.Ard;
import com.amaze.mqtt.POJO.Device;
import com.amaze.mqtt.POJO.Rasp;
import com.amaze.mqtt.POJO.DeviceState;
import com.amaze.mqtt.R;
import com.amaze.mqtt.adapters.ElectronicsAdapter;
import com.amaze.mqtt.utils.ALog;
import com.amaze.mqtt.utils.Constants;
import com.amaze.mqtt.utils.MessageReciever;
import com.amaze.mqtt.utils.RequestCodes;
import com.amaze.mqtt.utils.URL_API;
import com.amaze.mqtt.utils.Utils;
import com.amaze.mqtt.utils.VolleyHelper;
import com.amaze.mqtt.utils.VolleyInterface;
import com.amaze.mqtt.views.EmptyRecyclerView;
import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;

import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements VolleyInterface, MessageReciever.MessageHandler {
    Rasp deviceDetails;
    EmptyRecyclerView emptyRecyclerView;
    ElectronicsAdapter electronicsAdapter;
    ArrayList<Device> electronicArrayList;
    ArrayList<DeviceState> deviceStates;
    String subscribeTopic;
    MessageReciever messageReciever;
    String clientId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        deviceDetails = getIntent().getParcelableExtra("deviceDetails");
        if (deviceDetails == null) finish();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        findViewById(R.id.fab).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               promptSpeechInput();
            }
        });
        setSupportActionBar(toolbar);
        clientId = Utils.getdeviceId(this);
        subscribeTopic = URL_API.subscribe_group + deviceDetails.getDeviceid();
        electronicArrayList = new ArrayList<>();
        emptyRecyclerView = (EmptyRecyclerView) findViewById(R.id.recyclerView);
        emptyRecyclerView.setEmptyView(findViewById(R.id.emptyView), "No devices available", (TextView) findViewById(R.id.nodatatextview), (ContentLoadingProgressBar) findViewById(R.id.progressBar));
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        emptyRecyclerView.setLayoutManager(linearLayoutManager);
        electronicsAdapter = new ElectronicsAdapter(this, electronicArrayList);
        emptyRecyclerView.setAdapter(electronicsAdapter);
        electronicsAdapter.setEditClickListener(new ElectronicsAdapter.OnClickListener() {
            @Override
            public void onClick(Device deviceDetails) {
                showStateDialog(deviceDetails);
            }
        });
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put(Constants.userId, PreferenceManager.getDefaultSharedPreferences(this).getString("username", null));
        hashMap.put(Constants.deviceId, Utils.getdeviceId(this));
        hashMap.put("rasp", deviceDetails.getDeviceid());
        VolleyHelper.postRequestVolley(this, URL_API.getGetElectronicsApi(), hashMap, RequestCodes.GET_ELECTRONICS);
        messageReciever = new MessageReciever(URL_API.getMqtt_broker(), clientId, new String[]{subscribeTopic, URL_API.online_status_group + deviceDetails.getDeviceid()});
        messageReciever.setMqttCallbackExtended(this);
        messageReciever.subscribe();
    }
    /**
     * Showing google speech input dialog
     * */
    private void promptSpeechInput() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT,"Say something in format device on/off");
        try {
            startActivityForResult(intent, 1);
        } catch (ActivityNotFoundException a) {
            Toast.makeText(getApplicationContext(),
                    ("Not supported"),
                    Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Receiving speech input
     * */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case 1: {
                if (resultCode == RESULT_OK && null != data) {

                    ArrayList<String> result = data
                            .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    Toast.makeText(this,result.get(0),Toast.LENGTH_SHORT).show();
                    String []a=result.get(0).split(" ");
                    boolean done=false;
                    if(a.length==2){
                        a[0]=a[0].toLowerCase();
                        a[1]=a[1].toLowerCase();
                        if(a[1].length()>2){
                            a[1]=a[1].substring(0,2);
                        }
                    if(electronicArrayList!=null) {
                        for (Device device : electronicArrayList) {
                            if (device.getDesc2().toLowerCase().equals(a[0])) {
                                if (a[1].equals("on")){
                                    done = true;
                                    setState(device,1);
                                }else if (a[1].equals("of")){
                                    done = true;
                                    setState(device,0);
                                }
                                break;
                            }
                        }
                    }
                    }
                    if(!done){
                            Toast.makeText(this,"Incorrect command",Toast.LENGTH_SHORT).show();
                        }

                }
                break;
            }

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        menu.findItem(R.id.offline).setChecked(URL_API.offline);
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_logout) {
            HashMap<String, String> hashMap = new HashMap<>();
            hashMap.put(Constants.userId, PreferenceManager.getDefaultSharedPreferences(this).getString("username", null));
            hashMap.put(Constants.deviceId, Utils.getdeviceId(this));
            VolleyHelper.postRequestVolley(this, URL_API.getLogoutApi(), hashMap, RequestCodes.LOGOUT);
            return true;
        }else if(id==R.id.offline){
            PreferenceManager.getDefaultSharedPreferences(this).edit().putBoolean("offline",!URL_API.offline).commit();
            finish();
            URL_API.offline=!URL_API.offline;

            startActivity(new Intent(this,SplashActivity.class));
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void requestStarted(int requestCode) {
        emptyRecyclerView.setRunningtask(true);
    }

    @Override
    public void requestCompleted(int requestCode, String response) {
        emptyRecyclerView.setRunningtask(false);
        ALog.d(response);
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(response);
            if (requestCode == RequestCodes.GET_ELECTRONICS) {

                if (jsonObject.getInt("rs") == 1) {
                    ArrayList <Ard> ardList = new Gson().fromJson(jsonObject.get("ot").toString(), new TypeToken<ArrayList<Ard>>() {
                    }.getType());
                    electronicArrayList=new ArrayList<>();
                    for(Ard ard:ardList)
                        electronicArrayList.addAll(ard.getDevices());
                    electronicsAdapter.setDeviceDetailsArrayList(electronicArrayList);
                    electronicsAdapter.notifyDataSetChanged();
                }else
                    Snackbar.make(emptyRecyclerView,jsonObject.getString("message"),Snackbar.LENGTH_SHORT).show();
            }
            else if(requestCode==RequestCodes.LOGOUT){
                if (jsonObject.getInt("rs") == 1) {
                startActivity(new Intent(this,LoginActivity.class));
                }
                else
                    Snackbar.make(emptyRecyclerView,jsonObject.getString("message"),Snackbar.LENGTH_SHORT).show();

                }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void requestEndedWithError(int requestCode, VolleyError error) {
        emptyRecyclerView.setRunningtask(false);
    }

    @Override
    protected void onStop() {
        super.onStop();

        if (messageReciever != null)
            messageReciever.quit();
    }

    void showStateDialog(final Device electronic) {
        final String[] states = electronic.getPstates().split(",");
        MaterialDialog.Builder m = new MaterialDialog.Builder(this);
        m.items(states);
        m.title("Set state");
        m.itemsCallback(new MaterialDialog.ListCallback() {
            @Override
            public void onSelection(MaterialDialog dialog, View view, int which, CharSequence text) {
                int i=Integer.parseInt(states[which]);
                setState(electronic,i);
            }
        }).build().show();
    }
    void setState(Device electronic,int state){
        DeviceState deviceState = new DeviceState();
        deviceState.setDeviceId(electronic.getEdeviceid());
        deviceState.setState(state);
        deviceState.setArdid(electronic.getArdid());
        deviceState.setPin(electronic.getPin());
        ArrayList<DeviceState> arrayList=new ArrayList<DeviceState>();
        arrayList.add(deviceState);
        JsonObject jsonObject=new JsonObject();
        jsonObject.addProperty("code",3);
        jsonObject.add("data",new Gson().toJsonTree(arrayList));
        Utils.sendMqttMessage(URL_API.getMqtt_broker(), jsonObject.toString(), URL_API.send_group + deviceDetails.getDeviceid(), 0, false);
    }
    @Override
    public void onMessageRecieved(String topic, MqttMessage mm) {
        if (topic.startsWith("/online")) {
            //if (mm.toString().equals("false")) {
                Snackbar.make(findViewById(R.id.main), "Device connected "+mm.toString(), Snackbar.LENGTH_SHORT).show();
            //}
        } else
            try {
                JSONObject jsonObject=new JSONObject(mm.toString());
                deviceStates = new Gson().fromJson(jsonObject.get("data").toString(), new TypeToken<ArrayList<DeviceState>>() {
                }.getType());
                electronicsAdapter.updateState(deviceStates);
                electronicsAdapter.notifyDataSetChanged();
            } catch (Exception e) {
                e.printStackTrace();
            }
    }
}
