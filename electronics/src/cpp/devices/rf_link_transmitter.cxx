#include <stdio.h>
#include <iostream>
#include <stdlib.h>
#include <stdint.h>
#include <sys/types.h>
#include <unistd.h>

#include "rf_link_transmitter.h"

RfLinkTransmitter::RfLinkTransmitter(int p) {
	if (wiringPiSetup () == -1) {
		std::cout << "Failed to initialize wiringPi." << std::endl; 
		return;
	} else {
		std::cout << "Successfully initialized wiringPi" << std::endl;
	} 
	pinNumber = p;
	mySwitch = new RCSwitch();
	mySwitch->enableTransmit(pinNumber);
}

RfLinkTransmitter::~RfLinkTransmitter() {
	delete mySwitch;
	mySwitch = NULL;
}

void RfLinkTransmitter::setState(char* systemCode, int unitCode, bool state) {

	//std::cout << "RfLinkTransmitter::setState(): " << systemCode << " "
	//		<< unitCode << " " << state << std::endl;

	if (state) {
		//std::cout << "RfLinkTransmitter::setState(): on" << std::endl;

		mySwitch->switchOn(systemCode, unitCode);
	} else {

	//	std::cout << "RfLinkTransmitter::setState(): on" << std::endl;

		mySwitch->switchOff(systemCode, unitCode);
	}

	//std::cout << "RfLinkTransmitter::setState(): done." << std::endl;
}

int main()
{
	RfLinkTransmitter* rflt = new RfLinkTransmitter(0);
	rflt->setState("11111", 1, true);
	delete rflt;
}
