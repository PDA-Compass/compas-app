package net.afterday.compas.engine.engine.system.influence.extractor

import net.afterday.compas.engine.engine.system.influence.anomaly.AnomalyEvent
import net.afterday.compas.engine.sensors.SensorResult

class ByMacExtractionStrategy(private val letters: HashMap<String, Int>) {
    fun extract(value: SensorResult): AnomalyEvent?{
        val letter = value.id?.substring(0, 2)
        if (letters.containsKey(letter))
        {
            return AnomalyEvent(
                    value.id,
                    (letters[letter]!!),
                    value.value,
                    null
            )
        }
        return null
    }
}