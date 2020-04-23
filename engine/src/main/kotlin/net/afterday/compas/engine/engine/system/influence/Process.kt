package net.afterday.compas.engine.engine.system.influence

import net.afterday.compas.engine.engine.system.influence.anomaly.AnomalyEvent
import net.afterday.compas.engine.sensors.SensorResult

interface Process {
    fun filter(value: SensorResult): Boolean
    fun process(value: SensorResult)
    fun anomalyProcess(value: AnomalyEvent)
}