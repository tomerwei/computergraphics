//
//  RCPiBotViewController.swift
//  RCBoat
//
//  Created by Philipp Jenke on 30.08.15.
//  Copyright © 2015 Philipp Jenke. All rights reserved.
//
import UIKit

class RCPiBotViewController: UIViewController, Observer {
    
    @IBOutlet weak var velocitySlider: UISlider!
    @IBOutlet weak var rotationSlider: UISlider!
    @IBOutlet weak var reverseDirectionSlider: UISwitch!
    @IBOutlet weak var labelServer: UILabel!
    
    let model = Model()
    
    override func viewDidLoad() {
        super.viewDidLoad()
        model.type = Model.RCType.BOT
        model.addObserver(self)
        updateStatusImage()
        labelServer.text = "Server: " + HOST
    }
    
    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
    }
    
    /* From Observer protocol */
    func update() {
    }
    
    func updateStatusImage(){
    }
    
    
    @IBAction func velocityChanged(sender: UISlider) {
        var speed = Int(sender.value)
        if reverseDirectionSlider.on{
            speed = speed * -1
        }
        model.piBotVelocity = speed
    }

    @IBAction func rotationChanged(sender: UISlider) {
        let rotation = Int(sender.value)
        model.piBotRotation = rotation
    }
    
    @IBAction func directionChanged(sender: UISwitch) {
        velocitySlider.value = 0
        model.piBotVelocity = 0
    }
    
    @IBAction func buttonStopClicked(sender: UIButton) {
        velocitySlider.value = 0
        model.piBotVelocity = 0
    }
}
