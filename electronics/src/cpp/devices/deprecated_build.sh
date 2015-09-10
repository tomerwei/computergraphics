#!/bin/bash
echo "*** Building DHT22Dummy ***"
g++ -c dht22dummy.cpp
g++ -I$JAVA_HOME/include/ -I$JAVA_HOME/include/darwin -c dht22dummy_wrap.cxx
g++ -I$JAVA_HOME/include/ -I$JAVA_HOME/include/darwin -shared dht22dummy.cpp dht22dummy_wrap.cxx -o ../../../lib/dht22dummy.so
echo "*** Building DHT22 ***"
g++ -c locking.cpp
g++ -c main.cpp
g++ -c dht22.cpp
g++ -I$JAVA_HOME/include/ -I$JAVA_HOME/include/darwin -c dht22_wrap.cxx
g++ -shared -L/usr/lib/ -lwiringPi -I$JAVA_HOME/include/ -I$JAVA_HOME/include/darwin main.cpp locking.cpp dht22.cpp dht22_wrap.cxx -o ../../../lib/dht22.so