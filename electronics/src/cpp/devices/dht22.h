/**
 * Access to a DHT22 sensor
 *
 * Philipp Jenke
 **/

#ifndef DHT22_H
#define DHT22_H

#include "../../../src/cpp/wiringPi/wiringPi/wiringPi.h"
#include "locking.h"

/**
 * Access to a DHT22 sensor
 */
class Dht22 {

private:
	int pinNumber;
	Locking* locking;
	unsigned short sizecvt(const int read);
	int readData(double& temperature, double& humidity);
	bool checkOk();

public:
	Dht22(int pinNumber);
	~Dht22();
	double getTemperature();
	double getHumidity();

};

#endif
