#ifndef DHT22DUMMY_H
#define DHT22DUMMY_H

class Dht22Dummy{
public:
	Dht22Dummy(int pinNumber);
	double getTemperature();
	double getHumidity();
};

#endif
