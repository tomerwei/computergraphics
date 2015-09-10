CC = g++
CFLAGS=-c -Wall

UNAME_S := $(shell uname -s)
ifeq ($(UNAME_S),Linux)
        INCLUDE = $(JAVA_HOME)/include/ -I$(JAVA_HOME)/include/linux
endif
ifeq ($(UNAME_S),Darwin)
        INCLUDE = $(JAVA_HOME)/include/ -I$(JAVA_HOME)/include/darwin
endif

LIBRARY_PATH = /usr/lib/
SOURCES = locking.cxx dht22.cxx dht22_wrap.cxx rf_link_transmitter.cxx rf_link_transmitter_wrap.cxx RCSwitch.cxx
SOURCES_DUMMY = dht22dummy.cxx dht22dummy_wrap.cxx rf_link_transmitter_dummy.cxx rf_link_transmitter_dummy_wrap.cxx
OBJECTS = $(SOURCES:.cxx=.o)
OBJECTS_DUMMY = $(SOURCES_DUMMY:.cxx=.o)
TARGET_LIB_PATH = ../../../lib/
LIB = $(TARGET_LIB_PATH)devices.so
LIB_DUMMY = $(TARGET_LIB_PATH)devices_dummy.so

ifeq ($(UNAME_S),Linux)
        all: $(LIB_DUMMY) $(LIB)
endif
ifeq ($(UNAME_S),Darwin)
        all: $(LIB_DUMMY)
endif

clean: clean_objects clean_libs
	
$(LIB_DUMMY): $(OBJECTS_DUMMY)
	$(CC) -I$(INCLUDE) -shared $(OBJECTS_DUMMY) -o $(LIB_DUMMY)

$(LIB): $(OBJECTS)
	$(CC) -shared -L$(LIBRARY_PATH) -lwiringPi -I$(INCLUDE) $(OBJECTS) -o $(LIB)
	
%.o: %.cxx
	$(CC) -I$(INCLUDE) $(CFLAGS) $< -o $@
	
send: $(OBJECTS)
	$(CC) -L$(LIBRARY_PATH) -lwiringPi -I$(INCLUDE) $(OBJECTS) -o send	
	
test_on: $(OBJECTS)
	$(CC) -L$(LIBRARY_PATH) -lwiringPi -I$(INCLUDE) $(OBJECTS) -o test_on

clean_objects: 
	rm *.o
	
clean_libs: 
	rm $(TARGET_LIB_PATH)*.so
