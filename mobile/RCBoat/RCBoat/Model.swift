//
//  Model.swift
//  RCBoat
//
//  Created by Philipp Jenke on 10.06.15.
//  Copyright © 2015 Philipp Jenke. All rights reserved.
//

import Foundation

/* Represents the settings of the boat */
class Model {
    
    enum RCType {
        case UNDEFINED
        case BOAT
        case BOT
    }
    
    var type = RCType.UNDEFINED
    
    
    /* [-45 ... 45] */
    var boatRudderAngle = 0 {
        didSet {
            sendBoatCommandsToServer()
        }
    }
    
    /* [-100 ... 100] */
    var boatPropellerSpeed = 0 {
        didSet {
            sendBoatCommandsToServer()
        }
    }
    
    /* [-100 ... 100] */
    var piBotVelocity = 0 {
        didSet {
//            sendPiBotCommandsToServer()
        }
    }
    
    /* [-100 ... 100] */
    var piBotRotation = 0 {
        didSet {
//            sendPiBotCommandsToServer()
        }
    }
    
    /* Current stack voltage */
    var boatInputVoltage = -1.0 {
        didSet{
            //print(inputVoltage)
            notifyObservers()
        }
    }
   
    /* Flag indicating that the connection to the server is ok, will be updated regularly */
    var serverConnection : Connection = Connection.NOT_CONNECTED {
        didSet{
            notifyObservers()
        }
    }
    
    /* Timer interval (to check server connection) in seconds */
    let timerIntervalInSeconds = 0.25
    
    
    /* List of observers which need to be update about a change of the model */
    var observers = [Observer]()
    
    /* This flag is true if the RCBoat server is currently running on the raspberry pi */
    var rCBoatServerRunning = true
    
    init(){
        let ti = NSTimeInterval(Double(timerIntervalInSeconds) * 1)
        NSTimer.scheduledTimerWithTimeInterval(ti, target: self, selector: Selector("timerTick"), userInfo: nil, repeats: true)
    }
    
    /* Inform observers that the model changed */
    func notifyObservers(){
        for observer in observers {
            observer.update()
        }
    }
    
    /* Add new observer */
    func addObserver(observer:Observer ){
        observers.append(observer)
    }
    
    /* Handle timer tick */
    @objc func timerTick(){
        //checkServerStatus();
        if type == RCType.BOT {
            sendPiBotCommandsToServer()
        }
    }
    
    /* Send rudder and properller state to server -> GET */
    func sendBoatCommandsToServer(){
        let url = RCBOAT_COMMANDS_REQUEST + "rudder=\(boatRudderAngle),propeller=\(boatPropellerSpeed)"
        HttpRequest.post(url, errorHandler:{() -> Void in print("Error during post", terminator: "")});
    }
    
    /* Send rudder and properller state to server -> GET */
    func sendPiBotCommandsToServer(){
        let url = RCPIBOT_COMMANDS_REQUEST + "velocity=\(piBotVelocity),rotation=\(piBotRotation)"
        print(url, terminator: "")
        HttpRequest.post(url, errorHandler:{() -> Void in print("Error during post", terminator: "")});
    }
    
    /* Check the server status and update the status variable -> POST */
    func checkServerStatus(){
        
        // Check is alive
        var url = RCBOAT_IS_ALIVE_REQUEST
        HttpRequest.get(url, resultHandler: {
            (result:NSData?) -> Void in
            if let validResult = result {
                let resultText = HttpRequest.data2String(validResult);
                if resultText == IS_CONNECTED {
                        self.serverConnection = Connection.CONNECTED
                } else if  resultText == SERVER_CONNECTED_BRICK_NOT_CONNECTED {
                    self.serverConnection = Connection.SERVER_CONNECTED_BRICK_NOT_CONNECTED
                } else {
                 self.serverConnection = Connection.NOT_CONNECTED
                }
            } else {
                self.serverConnection = Connection.NOT_CONNECTED
            }
            }, errorHandler:{()->Void in
                self.serverConnection = Connection.NOT_CONNECTED
                //print("nothing: " + url )
        })
        
        // Check voltage
        url = RCBOAT_GET_VOLTAGE_REQUEST
        HttpRequest.get(url, resultHandler: {
            (result:NSData?) -> Void in
            if let validResult = result {
                let resultText = HttpRequest.data2String(validResult)//{
                var value = (resultText as NSString).doubleValue;
                value = Double(round(10*value)/10)
                self.boatInputVoltage = value
            } else {
                self.boatInputVoltage = -1
            }
            }, errorHandler:{()->Void in
                self.boatInputVoltage = -1
                 })
    }
}
