#!/bin/bash
echo "Copying required jars ..."
cp build/dist/*.jar /home/pi/homeautomation
echo "Copying C++ libraries ..."
cp electronics/lib/*.so /home/pi/homeautomation
echo "Copying config files ..."
cp -r smart_home_apps/configs /home/pi/homeautomation
echo "Copying webservice files ..."
cp -r smart_home_apps/raspiserver /home/pi/homeautomation
