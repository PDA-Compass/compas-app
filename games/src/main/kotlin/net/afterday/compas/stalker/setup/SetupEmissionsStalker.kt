package net.afterday.compas.stalker.setup

import net.afterday.compas.engine.setup.SetupEmissions

class SetupEmissionsStalker : SetupEmissions() {
    override fun setup() {
        at(1,10,10,1) {
            notifyBefore = 2
            fake = true
        }

        //New Year Emission
        at(12,31,23,59) {
            notifyBefore = 9
            duration = 2
        }
    }
}