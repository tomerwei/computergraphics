//
//  Observer.swift
//  RCBoat
//
//  Created by Philipp Jenke on 11.06.15.
//  Copyright © 2015 Philipp Jenke. All rights reserved.
//

import Foundation

protocol Observer {
    /* Observable has changed - update required */
    func update();
}