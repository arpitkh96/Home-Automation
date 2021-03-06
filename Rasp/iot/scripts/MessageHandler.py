import json

import paho.mqtt.client as mqtt

from iot.serializers import DevSerializer,UserSerializer,RaspSerializer,ArdSerializer
from iot.models import Edevice, History,User,Rasp,Ard
from collections import defaultdict
import traceback
#import thread
client = None
client1 = None
id = "arpitkh96"
first = False


# The callback for when the client receives a CONNACK response from the server.
def on_connect(client, userdata, flags, rc):
    print("Connected with result code " + str(rc))
    # Subscribing in on_connect() means that if we lose the connection and
    # reconnect then subscriptions will be renewed.
    print('connect',rc,userdata)
    if userdata == 1:
        #client.loop_start()
        client.subscribe("/devices/sub/" + id)
        global first
        if not first:
            first = True
            publish(client, "/devices/pub/" + id, "{\"code\":1,\"id\":\"" + id + "\"}")

    else:
        client.subscribe([("/devices/pub/#",0),("/devices/sub/"+id,0)])
def on_disconnect(client):
    print('disconnected')
    client.reconnect()

# The callback for when a PUBLISH message is received from the server.
def on_message(client2, userdata, msg):
    print(msg.topic + " " + str(msg.payload))
    JSON = json.loads(msg.payload.decode('utf-8'))
    try:
        code = JSON['code']
        global client
        if (code == 4):
            User.objects.all().delete()
            l = JSON['users']
            for k in l:
                l = UserSerializer(data=k)
                print(l.is_valid())
                if (l.is_valid()):
                    l.save()
            Rasp.objects.all().delete()
            l1 = JSON['rasps']
            for k in l1:
                l = RaspSerializer(data=k)
                print(l.is_valid())
                if (l.is_valid()):
                    l.save()
            Ard.objects.all().delete()
            l = JSON['ards']
            for k in l:
                l = ArdSerializer(data=k)
                if (l.is_valid()):
                    l.save()
            Edevice.objects.all().delete()
            l = JSON['dev']
            for k in l:
                l = DevSerializer(data=k)
                if (l.is_valid()):
                    l.save()
            a = Edevice.objects.all()
            k = defaultdict(list)
            for t in a:
                k[t.ardid].append(t)
            for p, v in k.items():
                pins = []
                for device in v:
                    pins.append(device.pin)
                msg = {"type": 0}
                msg["pin"] = pins
                publish(client, "/devices/sub/" + p.ardid, json.dumps(msg), retain=True)

        elif code == 3:
            for a in JSON['data']:
                msg = {"type": 1}
                msg["pin"] = a['pin']
                msg["state"] = a['state']
                publish(client, "/devices/sub/" + a['ardid'], json.dumps(msg))


        elif (code == 1):  # setup req
            a = Edevice.objects.filter(ardid=JSON['ardid'])
            pins = []
            for x in a:
                pins.append(x.pin)
            msg = {"type": 0}
            msg["pin"] = a['pin']
            publish(client2, "/devices/sub/" + JSON['ardid'], json.dumps(msg))
        elif code == 0:
            global client1
            topic = msg.topic.split("/")[-1]
            list1 = []
            for (i, y) in zip(JSON["pins"], JSON["data"]):
                print(topic, i)
                a = Edevice.objects.get(ardid=topic, pin=i)
                h = History(edeviceid=a.edeviceid, previous_state=a.state, state = y)
                h.save()
                a.state = y
                a.save()
                list1.append(a)
            res = {'code': 0, 'data': DevSerializer(list1, many=True).data}
            if(client._state==1):
                publish(client1, "/devices/pub/" + id, json.dumps(res))
            publish(client2,"/devices/pub/"+id,json.dumps(res))
    except Exception as e:
        print(e.__str__())
        traceback.print_exc()


def publish(client, topic, msg, retain=False):
    print("Publishing ", topic, msg)
    client.publish(topic, msg, retain=retain)

#def mqtt_connect():
def run():
    # userdata 1 for internet 0 for local
    global client, client1
    client1 = mqtt.Client()
    client1.user_data_set(1)
    client1.on_connect = on_connect
    client1.on_message = on_message
    client1.on_disconnect=on_disconnect
    client1.connect_async("35.199.185.104", 1883, 60)
    client1.loop_start()
    client = mqtt.Client()
    client.user_data_set(0)

    client.on_connect = on_connect
    client.on_message = on_message
    client.connect("localhost", 1883, 60)
    #client1.loop_start()
    # Blocking call that processes network traffic, dispatches callbacks and
    # handles reconnecting.
    # Other loop*() functions are available that give a threaded interface and a
    # manual interface.
    client.loop_forever()

