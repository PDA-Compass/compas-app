package net.afterday.compas.engine.game.anomaly

import net.afterday.compas.engine.common.Time
import net.afterday.compas.engine.engine.system.damage.*
import net.afterday.compas.engine.engine.system.influence.anomaly.*

open class Mental : AnomalyHandler() {
    override val name = "Mental"
    override val letter = "M"
    override val code = "0B"

    open fun mapSignal(signal: Float): Float {
        when {
            signal > -60.0 -> {
                return 100.0f
            }
            signal > -80.0 -> {
                return map(signal, -80, -60, 7, 15)
            }
            signal > -90.0 -> {
                return map(signal, -90, -80, 1, 7)
            }
            signal > -100.0 -> {
                return map(signal, -100, -80, 0, 1)
            }
            else -> return 0.0f
        }
    }

    override fun handle(anomaly: AnomalyEvent) {
        damageStream.onNext(
                DamageEvent(
                        anomaly.id,
                        DamageType.MENTAL,
                        mapSignal(anomaly.value.toFloat()),
                        Time.now,
                        null
                ))
    }
}