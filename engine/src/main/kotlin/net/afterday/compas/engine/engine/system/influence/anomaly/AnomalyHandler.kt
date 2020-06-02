package net.afterday.compas.engine.engine.system.influence.anomaly

import net.afterday.compas.engine.core.EventBus

abstract class AnomalyHandler {
    abstract val name: String
    abstract val letter: String
    abstract val code: String
    abstract fun handle(anomaly: AnomalyEvent)

    val damageStream = EventBus.damage()
}