#!/bin/bash
echo "Copying required jars ..."
cp build/dist/*.jar /home/pi/homeautomation
echo "Copying C++ libraries ..."
cp electronics/lib/*.so /home/pi/homeautomation
echo "Copying config files ..."
cp -r configs /home/pi/homeautomation
echo "Copying webservice files ..."
cp -r raspiserver /home/pi/homeautomation
