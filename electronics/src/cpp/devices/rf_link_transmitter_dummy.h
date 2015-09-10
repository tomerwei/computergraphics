/**
 * Access to a RF Link transmitter
 *
 * Philipp Jenke
 **/

#ifndef RF_LINK_TRANSMITTER_DUMMY_H
#define RF_LINK_TRANSMITTER_DUMMY_H

#include "../../../src/cpp/wiringPi/wiringPi/wiringPi.h"

/**
 * Access to a RF link transmitter (dummy).
 */
class RfLinkTransmitterDummy {

private:
	int pinNumber;

public:
	RfLinkTransmitterDummy(int pinNumber);
	~RfLinkTransmitterDummy();
	void setState(char* systemCode, int unitCode, bool state);

};

#endif
