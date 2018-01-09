from django.shortcuts import render
from rest_framework.views import APIView
from rest_framework.response import Response
from .serializers import RaspSerializer,ArdSerializer,DevSerializer,ArdSerializer1
from .models import *
import json
class UserLogin(APIView):
    def post(self, request, *args, **kwargs):
        user = request.POST['user']
        pass1 = request.POST['pass']
        dev=request.POST['deviceid']
        res = {'rs': 1, 'message': "success"}

        users=User.objects.filter(name=user, password=pass1)
        i = len(users)
        res['rs'] = 1 if (i == 1) else 0
        res['message'] = 'success' if (i == 1) else 'fail'
        if i == 1:
            LoginDet(name=users[0], deviceid=dev).save()
            ot = Rasp.objects.filter(userid=user)
            data = RaspSerializer(ot, many=True)
            res['ot'] = data.data
        return Response(res)
class isLoggedIn(APIView):
    def post(self, request, *args, **kwargs):
        user = request.POST['user']
        dev = request.POST['deviceid']
        i = len(LoginDet.objects.filter(name=user, deviceid=dev))

        if(i>0):
            res = {'rs': 1, 'message': "Exists"}
            ot = Rasp.objects.filter(userid=user)
            data = RaspSerializer(ot, many=True)
            res['ot'] = data.data

        else:
            res = {'rs': 0, 'message': "Exists"}
        return Response(res)


class Logout(APIView):
    def post(self, request, *args, **kwargs):
        user = request.POST['user']
        dev = request.POST['deviceid']
        dets=LoginDet.objects.filter(name=user, deviceid=dev)
        i = len(dets)

        if(i>0):
            for j in dets:
                j.delete()
        res = {'rs': 1, 'message': "Exists"}
        return Response(res)
class SignUp(APIView):
    def post(self, request, *args, **kwargs):
        user = request.GET.get('user')
        pass1 = request.GET.get('pass')
        email = request.GET.get('email')
        i = len(User.objects.filter(name=user))

        res = {'rs': 1, 'message': "success"}
        if(i>0):
            res = {'rs': 0, 'message': "Exists"}
        else:
            User(name=user,email=email,password=pass1).save()
        return Response(res)

class GetDev(APIView):
    def post(self, request, *args, **kwargs):
        rasp = request.POST['rasp']
        i =Ard.objects.filter(rasp=rasp)

        res = {'rs': 1, 'message': "success"}
        data = ArdSerializer(i,many=True)
        res['ot'] = data.data
        #serializers.serialize("json",k)
        return Response(res)  # Create your views here.

class UpdateStates(APIView):
    def post(self, request, *args, **kwargs):
        data = request.GET.get('data')
        j=json.loads(data)
        for k in j:
          print(k)
          l=DevSerializer(data=k)
          if(l.is_valid()):
              l.save()
        res = {'rs': 1, 'message': "success"}
        return Response(res)  # Create your views here.
