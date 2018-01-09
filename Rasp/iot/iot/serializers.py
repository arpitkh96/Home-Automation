from .models import  Edevice,Rasp,User,Ard
from rest_framework import serializers

class UserSerializer(serializers.ModelSerializer):
    class Meta:
        model = User
        fields = ('email',  'name', 'password')

class DevSerializer(serializers.ModelSerializer):
    class Meta:
        model = Edevice
        fields = ('edeviceid', 'ardid', 'pin', 'desc1', 'desc2', 'state', 'pstates')

class RaspSerializer(serializers.ModelSerializer):
    class Meta:
        model = Rasp
        fields = ('userid', 'name', 'deviceid','desc')


class ArdSerializer(serializers.ModelSerializer):
    class Meta:
        model = Ard
        fields = ('rasp', 'ardid')



class ArdSerializer1(serializers.ModelSerializer):
    devices = DevSerializer(many=True, read_only=True, source='edevice_set')


    class Meta:
        model = Ard
        fields = ('rasp', 'ardid', 'devices')


    def create(self, validated_data):
        devices_data = validated_data.pop('devices')
        album = ArdSerializer.objects.create(**validated_data)
        for device_data in devices_data:
            Edevice.objects.create(Ard=album, **device_data)
        return album
