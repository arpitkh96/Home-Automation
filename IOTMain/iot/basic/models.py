from django.db import models
from datetime import datetime

class User(models.Model):
    email = models.TextField()
    name = models.TextField(primary_key=True)
    password = models.TextField()
class LoginDet(models.Model):
    name = models.ForeignKey(User, on_delete=models.CASCADE)
    deviceid=models.TextField(primary_key=True)


class Rasp(models.Model):
    userid = models.ForeignKey(User, on_delete=models.CASCADE)
    name = models.TextField()
    deviceid = models.TextField(primary_key=True)
    desc=models.TextField(null=True)
    def __str__(self):
        return self.deviceid.__str__();

class Ard(models.Model):
    rasp = models.ForeignKey(Rasp, on_delete=models.CASCADE, to_field=('deviceid'))
    ardid = models.TextField(primary_key=True)

    def __str__(self):
        return self.ardid.__str__();

class Edevice(models.Model):
    edeviceid = models.TextField(primary_key=True)
    ardid = models.ForeignKey(Ard, on_delete=models.CASCADE, to_field=('ardid'))
    pin = models.IntegerField()
    desc1 = models.TextField()
    desc2 = models.TextField()
    state = models.IntegerField()
    pstates = models.TextField()

class History(models.Model):
    edeviceid = models.TextField(null=False)
    previous_state = models.IntegerField()
    state = models.IntegerField()
    time=models.DateTimeField(default=datetime.now)


# Create your models here.
