/**
 * Prevents rapid use of application by implementing a file lock
 * Based on code by technion@lolware.net
 *
 * Philipp Jenke
 */

#ifndef LOCKING_H
#define LOCKING_H

#define LOCKFILE "/var/run/dht.lock"

class Locking {

public:
	int open_lockfile(const char *filename);
	void close_lockfile(int fd);
};

#endif
