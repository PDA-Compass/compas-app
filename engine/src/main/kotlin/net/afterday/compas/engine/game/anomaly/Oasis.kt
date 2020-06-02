package net.afterday.compas.engine.game.anomaly

import net.afterday.compas.engine.engine.system.damage.DamageEvent
import net.afterday.compas.engine.engine.system.damage.DamageType
import net.afterday.compas.engine.engine.system.influence.anomaly.AnomalyEvent
import net.afterday.compas.engine.engine.system.influence.anomaly.AnomalyHandler

open class Oasis : AnomalyHandler() {
    override val name = "Oasis"
    override val letter = "H"
    override val code = "04"

    override fun handle(anomaly: AnomalyEvent) {
        damageStream.onNext(
                DamageEvent(
                        DamageType.HEALTH,
                        10,
                        null
                ))
    }
}