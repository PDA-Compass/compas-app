package net.afterday.compas.engine.engine.system.influence.anomaly

abstract class AnomalyHandler {
    abstract fun getLetter(): String
    abstract fun handle(anomaly: AnomalyEvent)
}