package net.afterday.compas.engine.engine.system.influence.extractor
import net.afterday.compas.engine.common.Time
import net.afterday.compas.engine.engine.system.influence.anomaly.AnomalyEvent
import net.afterday.compas.engine.sensors.SensorResult
import java.util.*

class ByFirstLetterExtractionStrategy(private val letters: HashMap<String, Int>) {
    private var calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"))

    fun extract(value: SensorResult): AnomalyEvent?{
        val letter = value.name?.get(0).toString()
        if (letters.containsKey(letter))
        {
            return AnomalyEvent(
                    value.name!!,
                    (letters[letter]!!),
                    value.value,
                    null,
                    Time.now
            )
        }
        return null
    }
}