#include <stdio.h>
#include <iostream>
#include <stdlib.h>
#include <stdint.h>
#include <sys/types.h>
#include <unistd.h>

#include "rf_link_transmitter_dummy.h"

RfLinkTransmitterDummy::RfLinkTransmitterDummy(int p) {
	pinNumber = p;
}

RfLinkTransmitterDummy::~RfLinkTransmitterDummy() {
}

void RfLinkTransmitterDummy::setState(char* systemCode, int unitCode,
		bool state) {
	std::cout << "Setting (" << systemCode << ", " << unitCode << ") to "
			<< state << std::endl;
}

