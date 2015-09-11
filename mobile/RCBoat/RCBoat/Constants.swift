//
//  Constants.swift
//  RCBoat
//
//  Created by Philipp Jenke on 11.06.15.
//  Copyright © 2015 Philipp Jenke. All rights reserved.
//

import Foundation

/* Possible sources for a change of the model */
enum Sender {
    case APP
    case SERVER
}

enum Connection {
    case CONNECTED
    case SERVER_CONNECTED_BRICK_NOT_CONNECTED
    case NOT_CONNECTED
}

//var HOST = "192.168.2.104" // MacBook Air
//var HOST = "192.168.2.114" // Dagobert
var HOST = "192.168.2.115" // Dagobert WLAN
//var HOST = "localhost"
var PORT = "8182"
var RCBOAT_IS_ALIVE_REQUEST = "http://" + HOST + ":" + PORT + "/rcboat/state/isAlive"
var RCBOAT_GET_VOLTAGE_REQUEST = "http://" + HOST + ":" + PORT + "/rcboat/state/getVoltage"
var RCBOAT_COMMANDS_REQUEST = "http://" + HOST + ":" + PORT + "/rcboat/commands/"
var RCPIBOT_COMMANDS_REQUEST = "http://" + HOST + ":" + PORT + "/rcpibot/commands/"
var IS_CONNECTED = "CONNECTION ESTABLISHED"
var SERVER_CONNECTED_BRICK_NOT_CONNECTED = "SERVER CONNECTED, BRICK NOT CONNECTED"

