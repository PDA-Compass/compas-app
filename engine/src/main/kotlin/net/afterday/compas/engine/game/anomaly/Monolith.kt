package net.afterday.compas.engine.game.anomaly

import net.afterday.compas.engine.common.Time
import net.afterday.compas.engine.engine.system.damage.DamageEvent
import net.afterday.compas.engine.engine.system.damage.DamageType
import net.afterday.compas.engine.engine.system.influence.anomaly.AnomalyEvent
import net.afterday.compas.engine.engine.system.influence.anomaly.AnomalyHandler

open class Monolith: AnomalyHandler() {
    override val name = "Monolith"
    override val letter = "O"
    override val code = "0C"

    override fun handle(anomaly: AnomalyEvent) {
        damageStream.onNext(
                DamageEvent(
                        anomaly.id,
                        DamageType.MONOLITH,
                        10.0f,
                        Time.now,
                        null
                ))
    }
}