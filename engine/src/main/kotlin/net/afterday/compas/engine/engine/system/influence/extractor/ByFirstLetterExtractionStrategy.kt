package net.afterday.compas.engine.engine.system.influence.extractor
import net.afterday.compas.engine.engine.system.influence.anomaly.AnomalyEvent
import net.afterday.compas.engine.sensors.SensorResult

class ByFirstLetterExtractionStrategy(private val letters: HashMap<String, Int>) {
    fun extract(value: SensorResult): AnomalyEvent?{
        val letter = value.name?.get(0).toString()
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