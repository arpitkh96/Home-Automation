package com.amaze.mqtt.utils;

import android.content.Context;
import android.provider.Settings;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

/**
 * Created by arpitkh996 on 06-09-2016.
 */

public class Utils {

    public static String getdeviceId(Context context) {
        return Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
    }

    public static void sendMqttMessage(String broker,String content,String topic,int qos,boolean ret){
        String clientId = "android";
        try {
            MqttClient sampleClient = new MqttClient(broker, clientId, new MemoryPersistence());
            MqttConnectOptions connOpts = new MqttConnectOptions();
            connOpts.setCleanSession(true);
            System.out.println("paho-client connecting to broker: " + broker);
            sampleClient.connect(connOpts);
            System.out.println("paho-client connected to broker"+topic);
            System.out.println("paho-client publishing message: " + content);
            MqttMessage message = new MqttMessage(content.getBytes());
            message.setQos(qos);
            message.setRetained(ret);
            sampleClient.publish(topic, message);
            System.out.println("paho-client message published");
            sampleClient.disconnect();
            System.out.println("paho-client disconnected");
        } catch (MqttException me) {
            me.printStackTrace();
        }
    }
}
