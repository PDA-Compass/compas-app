package net.afterday.compas.engine.game.anomaly

import net.afterday.compas.engine.common.Time
import net.afterday.compas.engine.engine.influences.WifiInfluences.WifiConverter
import net.afterday.compas.engine.engine.system.damage.DamageEvent
import net.afterday.compas.engine.engine.system.damage.DamageType
import net.afterday.compas.engine.engine.system.influence.anomaly.AnomalyEvent
import net.afterday.compas.engine.engine.system.influence.anomaly.AnomalyHandler
import kotlin.math.roundToInt

open class Radiation: AnomalyHandler() {
    override val name = "Radiation"
    override val letter = "R"
    override val code = "24"

    private fun mapSignal(signal: Int) : Float {
        when {
            signal > -45 -> {
                return map(signal.toFloat(), -45, 0, 100, 1000)
            }
            signal > -50 -> {
                return map(signal.toFloat(), -50, -45, 16, 50)
            }
            signal > -55 -> {
                return map(signal.toFloat(), -55, -50, 15, 16)
            }
            signal > -60 -> {
                return map(signal.toFloat(), -60, -55, 7, 15)
            }
            signal > -70 -> {
                return map(signal.toFloat(), -70, -60, 1, 7)
            }
            signal > -100 -> {
                return map(signal.toFloat(), -100, -70, 0, 1)
            }
            else -> return 0.0f;
        }
    }

    override fun handle(anomaly: AnomalyEvent) {
        damageStream.onNext(
                DamageEvent(
                        anomaly.id,
                        DamageType.RADIATION,
                        mapSignal(anomaly.value),
                        Time.now,
                        null
                ))
    }
}