/*
 *      dht22.c:
 *	Simple test program to test the wiringPi functions
 *	Based on the existing dht11.c
 *	Amended by technion@lolware.net
 */

#include <stdio.h>
#include <stdlib.h>
#include <stdint.h>
#include <sys/types.h>
#include <unistd.h>

#include "locking.h"
#include "dht22.h"

#define MAXTIMINGS 85
static int dht22_dat[5] = { 0, 0, 0, 0, 0 };

Dht22::Dht22(int p) {
	pinNumber = p;
	locking = new Locking();
}

Dht22::~Dht22() {
	delete locking;
}

unsigned short Dht22::sizecvt(const int read) {
	/* digitalRead() and friends from wiringpi are defined as returning a value
	 < 256. However, they are returned as int() types. This is a safety function */

	if (read > 255 || read < 0) {
		printf("Invalid data from wiringPi library\n");
		exit (EXIT_FAILURE);
	}
	return (unsigned short) read;
}

int Dht22::readData(double& temperature, double& humidity) {
	unsigned short laststate = HIGH;
	unsigned short counter = 0;
	unsigned short j = 0, i;

	temperature = -1000000;
	humidity = -1000000;

	dht22_dat[0] = dht22_dat[1] = dht22_dat[2] = dht22_dat[3] = dht22_dat[4] =
			0;

	// pull pin down for 18 milliseconds
	pinMode(pinNumber, OUTPUT);
	digitalWrite(pinNumber, HIGH);
	delay(10);
	digitalWrite(pinNumber, LOW);
	delay(18);
	// then pull it up for 40 microseconds
	digitalWrite(pinNumber, HIGH);
	delayMicroseconds(40);
	// prepare to read the pin
	pinMode(pinNumber, INPUT);

	// detect change and read data
	for (i = 0; i < MAXTIMINGS; i++) {
		counter = 0;
		while (sizecvt(digitalRead(pinNumber)) == laststate) {
			counter++;
			delayMicroseconds(1);
			if (counter == 255) {
				break;
			}
		}
		laststate = sizecvt(digitalRead(pinNumber));

		if (counter == 255)
			break;

		// ignore first 3 transitions
		if ((i >= 4) && (i % 2 == 0)) {
			// shove each bit into the storage unsigned shorts
			dht22_dat[j / 8] <<= 1;
			if (counter > 16)
				dht22_dat[j / 8] |= 1;
			j++;
		}
	}

	// check we read 40 bits (8bit x 5 ) + verify checksum in the last unsigned short
	// print it out if data is good
	if ((j >= 40)
			&& (dht22_dat[4]
					== ((dht22_dat[0] + dht22_dat[1] + dht22_dat[2]
							+ dht22_dat[3]) & 0xFF))) {
		humidity = (double) dht22_dat[0] * 256 + (double) dht22_dat[1];
		humidity /= 10;
		temperature = (double) (dht22_dat[2] & 0x7F) * 256
				+ (double) dht22_dat[3];
		temperature /= 10.0;
		if ((dht22_dat[2] & 0x80) != 0)
			temperature *= -1;
		return 1;
	} else {
		return 0;
	}
}

bool Dht22::checkOk() {
	if (wiringPiSetup() == -1) {
		return false;
	}
	if (setuid(getuid()) < 0) {
		//perror("Dropping privileges failed\n");
		return false;
	}
	return true;
}

double Dht22::getTemperature() {
	if (!checkOk()) {
		return -1;
	}
	int lockfd = locking->open_lockfile(LOCKFILE);
	double temperature, humidty;
	while (readData(temperature, humidty) == 0) {
		delay(1000); // wait 1sec to refresh
	}
	delay(1500);
	locking->close_lockfile(lockfd);
	return temperature;
}

double Dht22::getHumidity() {
	if (!checkOk()) {
		return -1;
	}
	int lockfd = locking->open_lockfile(LOCKFILE);
	double temperature, humidty;
	while (readData(temperature, humidty) == 0) {
		delay(1000); // wait 1sec to refresh
	}
	delay(1500);
	locking->close_lockfile(lockfd);
	return humidty;
}

