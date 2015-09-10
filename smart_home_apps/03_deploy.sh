#!/bin/bash
echo "Copying homeautomation.jar ..."
cp target/homeautomation.jar /home/pi/homeautomation
echo "Copying required jars ..."
cp lib/*.jar /home/pi/homeautomation
echo "Copying C++ libraries ..."
cp lib/*.so /home/pi/homeautomation
echo "Copying config files ..."
cp -r configs /home/pi/homeautomation
echo "Copying webservice files ..."
cp -r raspiserver /home/pi/homeautomation