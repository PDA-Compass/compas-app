package net.afterday.compas.engine.game.anomaly

import net.afterday.compas.engine.engine.system.influence.anomaly.AnomalyEvent
import net.afterday.compas.engine.engine.system.influence.anomaly.AnomalyHandler

open class Radiation: AnomalyHandler() {
    override val name = "Radiation"
    override val letter = "R"
    override val code = "02"

    override fun handle(anomaly: AnomalyEvent) {
        TODO("Not yet implemented")
    }
}