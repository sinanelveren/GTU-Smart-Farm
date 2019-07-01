#!/usr/bin/env python

#author Sinan Elveren, Gebze Technical University
#Project - CSE495, Graduation Project - Raspberry PI Based Smart Farm 

import firebase_admin
from firebase_admin import credentials
from firebase_admin import db

from datetime import datetime
import time
from time import sleep


#from gpiozero import InputDevice
#no_rain = InputDevice(18)

#for humidit
import RPi.GPIO as GPIO
chHumidity = 21
GPIO.setmode(GPIO.BCM)
GPIO.setup(chHumidity, GPIO.IN)


#for rain
chRain = 18
GPIO.setup(chRain, GPIO.IN)


# Fetch the service account key JSON file contents
cred = credentials.Certificate('akillitarimgtu-firebase-adminsdk-43ges-1ca93ca4b6.json')
# Initialize the app with a service account, granting admin privileges
firebase_admin.initialize_app(cred, {
    'databaseURL': 'https://akillitarimgtu.firebaseio.com/'
})


def gettimenow():
	now = datetime.now()
	mm = str(now.month)
	dd = str(now.day)
	yyyy = str(now.year)
	hour = str(now.hour)
	mi = str(now.minute)
	ss = str(now.second)
	
	return mm + "." + dd + "." + yyyy + "-" + hour + ":" + mi + ":" + ss + " -> "

 
def callbackHumidity(chHumidity):
	global humidityStatus 
	sleep(1)
        if GPIO.input(chHumidity):
		humidityStatus = False
                print gettimenow() + "Water not Detected!"
		
        else:
		humidityStatus = True
                print gettimenow() + "Water Detected!"
		
	infieldRef.update({
		'humidity': humidityStatus
	})	
	dateRef.update({'lastUpdate' : gettimenow() })


 
def callbackRain(chRain):
	global rainStatus
	sleep(1)
        if GPIO.input(chRain):
		rainStatus = False
                print gettimenow() + "There is no rain"
        else:
		rainStatus = True
                print gettimenow() + "It's rainy"
		
	infieldRef.update({
		'rain': rainStatus
	})
	dateRef.update({'lastUpdate' : gettimenow() })		

 
 
GPIO.add_event_detect(chHumidity, GPIO.BOTH, bouncetime=300)  # let us know when the pin goes HIGH or LOW
GPIO.add_event_callback(chHumidity, callbackHumidity)  # assign function to GPIO PIN, Run function on change
 
GPIO.add_event_detect(chRain, GPIO.BOTH, bouncetime=300)  # let us know when the pin goes HIGH or LOW
GPIO.add_event_callback(chRain, callbackRain)  # assign function to GPIO PIN, Run function on change
 



#get data from firebase - set reference
print gettimenow() + "Connecting to database."
ref = db.reference('_message')
dateRef = db.reference('_date')
print(ref.get())
print gettimenow() + "Connected to database."


dateRef.update({'lastUpdate' : gettimenow() })
infieldRef = db.reference('infield')



#Check infield status
print gettimenow() + "Checking infield status"

if not GPIO.input(chHumidity):
	humidityStatus = True
	print gettimenow() + "Water Detected!"		
else:
	humidityStatus = False


if not GPIO.input(chRain):
	rainStatus = True
	print gettimenow() + "It's rainy"		
else:
	rainStatus = False


infieldRef.update({
	'humidity': humidityStatus
})
infieldRef.update({
	'rain': rainStatus
})

rainCount = 0
valveCount = 0
humidityCount = 0

sleep(5)

while True:
	if rainStatus:
		rainCount += 1
		valveCount = 0
		#print "--rain"
		if rainCount == 5:
			infieldRef.update({
			   'valve': 'off'
			})
		
			
	if not rainStatus:
		rainCount = 0
		#print "--No rain"
		
	
	
	if humidityStatus:
		#print("--Water detected")
		humidityCount += 1	
		valveCount = 0
		
		if humidityCount == 1:
			infieldRef.update({
				'valve': 'off'
			})
			
		
	if not humidityStatus:
		#print("--Water not detected")	
		humidityCount = 0
		
			

	#check and update valve status
	valveCount += 1
	if valveCount == 5:
		infieldRef.update({
			'valve': 'on'
		})
		dateRef.update({'lastUpdate' : gettimenow() })

		
		
	sleep(2)
    
    



##
#infieldRef.update({
#   'humidity': '%70',
#   'tempeture': 0,
#   'rain': 'true',
#   'valve' : 'off'	
#})

#infieldRef.update({
#	'rain' : 'true'
#})

##


