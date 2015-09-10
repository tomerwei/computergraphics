#include "dht22dummy.h"

Dht22Dummy::Dht22Dummy(int pinNumber)
{
}

double Dht22Dummy::getTemperature()
{
	return 23.0;
}

double Dht22Dummy::getHumidity()
{
	return 42.0;
}
