import paho.mqtt.client as mqtt
from basic.serializers import RaspSerializer, ArdSerializer, ArdSerializer1, DevSerializer,UserSerializer
from basic.models import User, Rasp, Ard, Edevice, History
import json
import traceback


# The callback for when the client receives a CONNACK response from the server.
def on_connect(client, userdata, flags, rc):
    print("Connected with result code " + str(rc))

    # Subscribing in on_connect() means that if we lose the connection and
    # reconnect then subscriptions will be renewed.
    client.subscribe("/devices/pub/#")


# The callback for when a PUBLISH message is received from the server.
def on_message(client, userdata, msg):
    print(msg.topic + " " + str(msg.payload))
    JSON = json.loads(msg.payload.decode('utf-8'))
    try:
        code = JSON['code']
        if (code == 0):
            #l = DevSerializer(data=JSON['data'], many=True)
            #print(l.is_valid())
            #if (l.is_valid()):
                #l = list(l.validated_data)
                l=JSON['data']
                for a in l:
                    print(a)
                    device = Edevice.objects.get(edeviceid=a['edeviceid'])
                    h = History(edeviceid=a['edeviceid'], previous_state=device.state, state=a['state'])
                    device.state = a['state']
                    device.save()
                    h.save()
        elif code == 1:
            id = JSON['id']
            a = id #msg.topic.split("/")[-1]
            rasps=[Rasp.objects.get(deviceid=a)]
            print(rasps[0].userid)
            users=[rasps[0].userid]
            ardus = Ard.objects.filter(rasp=a)
            edevices = []
            for a in ardus:
                edevices += (list(Edevice.objects.filter(ardid=a.ardid)))
            edevices = DevSerializer(edevices, many=True)
            jkson = json.dumps({"code":4,'users':UserSerializer(users,many=True).data,'rasps':RaspSerializer(rasps,many=True).data,"ards":ArdSerializer1(ardus,many=True).data,"dev":edevices.data})
            publish(client, "/devices/sub/" + id, jkson)
    except Exception as e:
        a=1
        print(e)
        traceback.print_exc()


def publish(client, topic, msg):
    client.publish(topic, msg)


def run():
    client = mqtt.Client()
    client.on_connect = on_connect
    client.on_message = on_message

    client.connect("35.199.185.104", 1883, 60)

    # Blocking call that processes network traffic, dispatches callbacks and
    # handles reconnecting.
    # Other loop*() functions are available that give a threaded interface and a
    # manual interface.
    client.loop_forever()
