#include <iostream>
#include "dht22.h"


int main() {
	Dht22* dht22 = new Dht22(7);
	std::cout << "Temperature: " << dht22->getTemperature() << std::endl;
	std::cout << "Humidity: " << dht22->getHumidity() << std::endl;
	delete dht22;
	return 0;
}
