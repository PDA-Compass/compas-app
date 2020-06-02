package net.afterday.compas.engine.game.anomaly

import net.afterday.compas.engine.engine.system.damage.*
import net.afterday.compas.engine.engine.system.influence.anomaly.*

open class Mental : AnomalyHandler() {
    override val name = "Mental"
    override val letter = "M"
    override val code = "0B"

    override fun handle(anomaly: AnomalyEvent) {
        damageStream.onNext(
                DamageEvent(
                        DamageType.MENTAL,
                        10,
                        null
                ))
    }
}