# -*- coding: utf-8 -*-
import RPi.GPIO as GPIO
import time

CAR_RED=13
CAR_YELLOW=19
CAR_GREEN=26

PEDESTRIAN_RED=20
PEDESTRIAN_GREEN=21

PEDESTRIAN_SIGNAL=12

SIGNAL_BTN=5
SENSOR=18

# Tauko CAR_RED ja PEDESTRIAN_GREEN, sekä PEDESTRIAN_RED ja CAR_YELLOW välillä
TIME_INTERMISSION=2
# Kuinka pitkään jalankulkijoiden valo on vihreä
TIME_PEDESTRIAN_GREEN=5
# Kuinka kauan odotetaan liikkuvia autoja ennen vaihtamista
TIME_WAIT_FOR_CARS=10


GPIO.setmode(GPIO.BCM)
GPIO.setwarnings(False)

GPIO.setup(CAR_RED, GPIO.OUT)
GPIO.setup(CAR_YELLOW, GPIO.OUT)
GPIO.setup(CAR_GREEN, GPIO.OUT)

GPIO.setup(PEDESTRIAN_RED, GPIO.OUT)
GPIO.setup(PEDESTRIAN_GREEN, GPIO.OUT)

GPIO.setup(PEDESTRIAN_SIGNAL, GPIO.OUT)

GPIO.setup(SIGNAL_BTN, GPIO.IN)
GPIO.setup(SENSOR, GPIO.IN)

# Valojen oletustila
def lights_default():
    GPIO.output(CAR_RED, 0)
    GPIO.output(CAR_YELLOW,0)
    GPIO.output(PEDESTRIAN_GREEN, 0)
    GPIO.output(PEDESTRIAN_SIGNAL,0)

    GPIO.output(CAR_GREEN, 1) 
    GPIO.output(PEDESTRIAN_RED, 1)

# Liikennevalojen vaihtuminen
def switch_lights():
    time.sleep(1)
    GPIO.output(CAR_GREEN, 0)
    GPIO.output(CAR_YELLOW, 1)
    time.sleep(1)
    GPIO.output(CAR_YELLOW, 0)
    GPIO.output(CAR_RED, 1)
    time.sleep(TIME_INTERMISSION)

    GPIO.output(PEDESTRIAN_SIGNAL,0)
    GPIO.output(PEDESTRIAN_RED, 0)
    GPIO.output(PEDESTRIAN_GREEN, 1)
    time.sleep(TIME_PEDESTRIAN_GREEN)
    GPIO.output(PEDESTRIAN_GREEN, 0)
    GPIO.output(PEDESTRIAN_RED, 1)
    time.sleep(TIME_INTERMISSION)
    
    GPIO.output(CAR_YELLOW, 1)
    time.sleep(1)
    GPIO.output(CAR_YELLOW, 0)
    GPIO.output(CAR_RED, 0)
    lights_default()

# Jalankulkijoiden signaalinappulan tapahtumakäsittelijä
def signal_pressed():
    GPIO.output(PEDESTRIAN_SIGNAL,1)
    max_wait_time = time.time() + TIME_WAIT_FOR_CARS
    while time.time() < max_wait_time:
        if GPIO.input(SENSOR) == 1:
            time.sleep(1)
        else:
            break
    switch_lights()

# Laitetaan valot oletustilaan ja odotetaan signaalinappulan painamista
try:
    lights_default()
    while True:
        if GPIO.input(SIGNAL_BTN) == 1:
            signal_pressed()
        time.sleep(0.1)
except KeyboardInterrupt:
    GPIO.cleanup()
except Exception as e:
    print("Something went wrong: " + e)
    GPIO.cleanup()


GPIO.cleanup()