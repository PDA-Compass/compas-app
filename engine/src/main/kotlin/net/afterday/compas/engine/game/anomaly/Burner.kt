package net.afterday.compas.engine.game.anomaly

import net.afterday.compas.engine.engine.system.influence.anomaly.AnomalyEvent
import net.afterday.compas.engine.engine.system.influence.anomaly.AnomalyHandler

open class Burner : AnomalyHandler() {
    override fun getLetter(): String {
        return "K"
    }

    override fun handle(anomaly: AnomalyEvent) {
        TODO("Not yet implemented")
    }
}