package net.afterday.compas.engine.game.anomaly

import net.afterday.compas.engine.engine.system.influence.anomaly.AnomalyEvent
import net.afterday.compas.engine.engine.system.influence.anomaly.AnomalyHandler

open class Burner : AnomalyHandler() {
    override val name = "Burner"
    override val letter = "H"
    override val code = "0A"

    override fun handle(anomaly: AnomalyEvent) {
        TODO("Not yet implemented")
    }
}