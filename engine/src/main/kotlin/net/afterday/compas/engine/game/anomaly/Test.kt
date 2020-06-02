package net.afterday.compas.engine.game.anomaly

import net.afterday.compas.engine.engine.system.damage.DamageEvent
import net.afterday.compas.engine.engine.system.damage.DamageType
import net.afterday.compas.engine.engine.system.influence.anomaly.AnomalyEvent
import net.afterday.compas.engine.engine.system.influence.anomaly.AnomalyHandler

open class Test : AnomalyHandler() {
    override val name = "Test"
    override val letter = "K"
    override val code = "0A"

    override fun handle(anomaly: AnomalyEvent) {
        damageStream.onNext(
                DamageEvent(
                        DamageType.FIRE,
                        10,
                        null
                ))
    }
}