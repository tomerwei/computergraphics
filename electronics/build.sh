#!/bin/bash
cd electronics
echo "*** Creating SWIG wrapper ***"
cd src/cpp/swig/
make clean
make
cd ../../../
echo "*** Building WiringPi ***"
cd src/cpp/wiringPi/
sh build
cd ../../../
echo "*** Building devices lib ***"
cd src/cpp/devices/
make clean
make
cd ../../../
cd ..
