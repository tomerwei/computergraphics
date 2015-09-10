/**
 * Access to a RF Link transmitter
 *
 * Philipp Jenke
 **/

#ifndef RF_LINK_TRANSMITTER_H
#define RF_LINK_TRANSMITTER_H

#include "../../../src/cpp/wiringPi/wiringPi/wiringPi.h"
#include "RCSwitch.h"

/**
 * Access to a RF link transmitter.
 */
class RfLinkTransmitter {

private:
	int pinNumber;
	RCSwitch* mySwitch;

public:
	RfLinkTransmitter(int pinNumber);
	~RfLinkTransmitter();
	void setState(char* systemCode, int unitCode, bool state);
};

#endif
