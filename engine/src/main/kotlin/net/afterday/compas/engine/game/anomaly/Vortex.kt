package net.afterday.compas.engine.game.anomaly

import net.afterday.compas.engine.engine.system.influence.anomaly.AnomalyEvent
import net.afterday.compas.engine.engine.system.influence.anomaly.AnomalyHandler

open class Vortex : AnomalyHandler() {
    override val name = "Vortex"
    override val letter = "M"
    override val code = "01"

    override fun handle(anomaly: AnomalyEvent) {
        TODO("Not yet implemented")
    }
}