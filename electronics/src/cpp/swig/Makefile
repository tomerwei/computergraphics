JAVA_OUT_DIR = ../../main/java/homeautomation/swig/
DHT22_WRAP = ../devices/dht22_wrap.cxx
DHT22DUMMY_WRAP = ../devices/dht22dummy_wrap.cxx
RF_LINK_TRANSMITTER_WRAP = ../devices/rf_link_transmitter_wrap.cxx
RF_LINK_TRANSMITTER_DUMMY_WRAP = ../devices/rf_link_transmitter_dummy_wrap.cxx

all: dht22 dht22dummy rf_link_transmitter rf_link_transmitter_dummy

dht22: dht22.swig
	swig -c++ -java -package homeautomation.swig -Wall -o $(DHT22_WRAP) -outdir $(JAVA_OUT_DIR) dht22.swig

dht22dummy: dht22dummy.swig
	swig -c++ -java -package homeautomation.swig -Wall -o $(DHT22DUMMY_WRAP) -outdir $(JAVA_OUT_DIR) dht22dummy.swig
	
rf_link_transmitter: rf_link_transmitter.swig
	swig -c++ -java -package homeautomation.swig -Wall -o $(RF_LINK_TRANSMITTER_WRAP) -outdir $(JAVA_OUT_DIR) rf_link_transmitter.swig
	
rf_link_transmitter_dummy: rf_link_transmitter_dummy.swig
	swig -c++ -java -package homeautomation.swig -Wall -o $(RF_LINK_TRANSMITTER_DUMMY_WRAP) -outdir $(JAVA_OUT_DIR) rf_link_transmitter_dummy.swig
	
clean: clean_dht22 clean_dht22dummy clean_rf_link_transmitter clean_rf_link_transmitter_dummy
	rm $(JAVA_OUT_DIR)*.java

clean_dht22:
	rm $(DHT22_WRAP)

clean_dht22dummy: 
	rm $(DHT22DUMMY_WRAP)

clean_rf_link_transmitter:
	rm $(RF_LINK_TRANSMITTER_WRAP)
	
clean_rf_link_transmitter_dummy:
	rm $(RF_LINK_TRANSMITTER_DUMMY_WRAP)