package net.afterday.compas.engine.engine.system.influence

import io.reactivex.rxjava3.subjects.Subject
import net.afterday.compas.engine.engine.system.influence.anomaly.AnomalyContainer
import net.afterday.compas.engine.engine.system.influence.anomaly.AnomalyEvent
import net.afterday.compas.engine.engine.system.influence.extractor.ByFirstLetterExtractionStrategy
import net.afterday.compas.engine.engine.system.influence.extractor.ByMacExtractionStrategy
import net.afterday.compas.engine.game.anomaly.Burner
import net.afterday.compas.engine.game.anomaly.Vortex
import net.afterday.compas.engine.sensors.SensorResult

class AnomalyProcess(stream: Subject<AnomalyEvent>) : Process {
    val firstLetterExtraction: ByFirstLetterExtractionStrategy
    var macExtractionStrategy: ByMacExtractionStrategy

    val anomalyStream: Subject<AnomalyEvent> = stream
    val anomalyContainer: AnomalyContainer = AnomalyContainer()

    init {
        anomalyContainer.registerHandler(Burner())
        anomalyContainer.registerHandler(Vortex())

        firstLetterExtraction = ByFirstLetterExtractionStrategy(anomalyContainer.getLetterMap())
        macExtractionStrategy = ByMacExtractionStrategy(anomalyContainer.getCodeMap())

        /*anomalyStream.subscribe {
            anomalyProcess(it)
        }*/
    }

    override fun filter(value: SensorResult): Boolean {
        return true
    }

    override fun process(value: SensorResult) {
        var event: AnomalyEvent? = null;

        if (value.name != null && value.name != "") {
            event = firstLetterExtraction.extract(value)
        }
        /*if (event == null) {
            event = macExtractionStrategy.extract(value)
        }*/

        if (event != null) {
            anomalyStream.onNext(event)
        }
    }

    override fun anomalyProcess(value: AnomalyEvent) {
        if (value.type == 0) {

        }
        //TODO("Not yet implemented")
    }


}