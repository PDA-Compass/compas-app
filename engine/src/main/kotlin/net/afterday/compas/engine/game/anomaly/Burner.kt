package net.afterday.compas.engine.game.anomaly

import net.afterday.compas.engine.common.Time
import net.afterday.compas.engine.engine.system.damage.*
import net.afterday.compas.engine.engine.system.influence.anomaly.*

open class Burner : AnomalyHandler() {
    override val name = "Burner"
    override val letter = "B"
    override val code = "0A"

    override fun handle(anomaly: AnomalyEvent) {
        damageStream.onNext(
                DamageEvent(
                        anomaly.id,
                        DamageType.FIRE,
                        10.0f,
                        Time.now,
                        null
                ))
    }
}