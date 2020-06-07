package net.afterday.compas.engine.game.anomaly

import net.afterday.compas.engine.common.Time
import net.afterday.compas.engine.engine.system.damage.DamageEvent
import net.afterday.compas.engine.engine.system.damage.DamageType
import net.afterday.compas.engine.engine.system.influence.anomaly.AnomalyEvent
import net.afterday.compas.engine.engine.system.influence.anomaly.AnomalyHandler

open class Oasis : AnomalyHandler() {
    override val name = "Oasis"
    override val letter = "R"
    override val code = "04"

    override fun handle(anomaly: AnomalyEvent) {
        damageStream.onNext(
                DamageEvent(
                        anomaly.id,
                        DamageType.HEALTH,
                        10.0f,
                        Time.now,
                        null
                ))
    }
}