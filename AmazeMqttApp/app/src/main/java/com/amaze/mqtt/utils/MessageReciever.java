package com.amaze.mqtt.utils;

/**
 * Created by arpitkh996 on 06-09-2016.
 */

import android.os.Handler;
import android.os.Looper;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttAsyncClient;
import org.eclipse.paho.client.mqttv3.MqttCallbackExtended;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;


public class MessageReciever implements MqttCallbackExtended {

    String broker;
    String clientId;
    String subscribeTopic[];
    MqttAsyncClient client = null;
    MessageHandler mqttCallbackExtended;

    public MessageReciever(String broker, String clientId, String subscribeTopic[]) {
        this.broker = broker;
        this.clientId = clientId;
        this.subscribeTopic = subscribeTopic;
    }

    public void setMqttCallbackExtended(MessageHandler mqttCallbackExtended) {
        this.mqttCallbackExtended = mqttCallbackExtended;
    }

    public void subscribe() {

        try {
            if (client == null) {
                client = new MqttAsyncClient(broker, clientId, new MemoryPersistence());
            }

            ALog.d("Connecting");
            MqttConnectOptions connOpts = new MqttConnectOptions();
            connOpts.setKeepAliveInterval(10);
            connOpts.setCleanSession(true);
            connOpts.setAutomaticReconnect(true);
            client.setCallback(this);
            client.connect(connOpts);
            ALog.d("Connected");

        } catch (MqttException me) {
            me.printStackTrace();
        }
    }

    public void quit() {
        final String METHOD = "quit";
        try {
            client.disconnect();
            ALog.d(METHOD + " disconnected!");
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void connectionLost(Throwable thrwbl) {
        ALog.d("Connection lost ");
    }

    @Override
    public void messageArrived(final String string, final MqttMessage mm) throws Exception {
        ALog.d(string+"\t"+mm);
        if (mqttCallbackExtended != null)
            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    mqttCallbackExtended.onMessageRecieved(string, mm);
                }
            });
    }

    @Override
    public void deliveryComplete(IMqttDeliveryToken imdt) {

    }

    @Override
    public void connectComplete(boolean bReconnect, String host) {
        final String METHOD = "connectComplete";

        ALog.d(METHOD + subscribeTopic.toString() + " Connected to " + host + " Auto reconnect ? " + bReconnect);
        try {
            for(String s:subscribeTopic)
            client.subscribe(s, 2);
        } catch (MqttException ex) {
            ex.printStackTrace();
        }
    }

    public interface MessageHandler {
        void onMessageRecieved(String topic, MqttMessage mqttMessage);
    }
}
