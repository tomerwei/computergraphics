#!/bin/bash
echo "Creating wrapper for DHT22Dummy"
swig -c++ -java -package homeautomation.swig -Wall -o ../dht22/dht22_wrap.cxx -outdir ../../main/java/homeautomation/swig/ dht22.swig
echo "Creating wrapper for DHT22"
swig -c++ -java -package homeautomation.swig -Wall -o ../dht22/dht22dummy_wrap.cxx -outdir ../../main/java/homeautomation/swig/ dht22dummy.swig
 