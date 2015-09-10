#!/bin/bash
pid=`ps aux | grep homeautomation | awk '{print $2}'`
kill -9 $pid
