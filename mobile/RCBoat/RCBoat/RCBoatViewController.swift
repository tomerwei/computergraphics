//
//  ViewController.swift
//  RCBoat
//
//  Created by Philipp Jenke on 10.06.15.
//  Copyright © 2015 Philipp Jenke. All rights reserved.
//

import UIKit

class RCBoatViewController: UIViewController, Observer {

    @IBOutlet weak var sliderPropeller: UISlider!
    @IBOutlet weak var sliderRudder: UISlider!
    @IBOutlet weak var backSwitch: UISwitch!
    @IBOutlet weak var connectionStatusImage: UIImageView!
    @IBOutlet weak var labelVoltage: UILabel!
    
    let model = Model()
    
    override func viewDidLoad() {
        super.viewDidLoad()
        model.type = Model.RCType.BOAT
        model.addObserver(self)
        updateStatusImage()
    }

    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
    }

    /* Slider rudder moved */
    @IBAction func sliderRudderValueChanged(sender: UISlider) {
        model.boatRudderAngle = Int(sender.value)
    }
    
    /* Slider propeller moved */
    @IBAction func sliderPropellerValueChanged(sender: UISlider) {
        var speed = Int(sender.value)
        if backSwitch.on{
            speed = speed * -1
        }
        model.boatPropellerSpeed = speed
    }
    
    @IBAction func backSwitchValueChanged(sender: UISwitch) {
        sliderPropeller.value = 0
        model.boatPropellerSpeed = 0
    }
    
    /* From Observer protocol */
    func update() {
        updateStatusImage()
        dispatch_async(dispatch_get_main_queue()){
            if self.model.boatInputVoltage < 0.0 {
                self.labelVoltage.text = "-"
            } else {
                self.labelVoltage.text = "\(self.model.boatInputVoltage)V"
            }
        }
    }
    
    func updateStatusImage(){
        switch model.serverConnection {
        case Connection.CONNECTED:
            dispatch_async(dispatch_get_main_queue()){
                self.connectionStatusImage.image = UIImage(named:"green")
            }
        case Connection.SERVER_CONNECTED_BRICK_NOT_CONNECTED:
            dispatch_async(dispatch_get_main_queue()){
                self.connectionStatusImage.image = UIImage(named:"orange")
            }
        case Connection.NOT_CONNECTED:
            dispatch_async(dispatch_get_main_queue()){
                self.connectionStatusImage.image = UIImage(named:"red")
            }
            }
    }
}

