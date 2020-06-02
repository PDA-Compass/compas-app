package net.afterday.compas.engine.game.anomaly

import net.afterday.compas.engine.engine.system.damage.*
import net.afterday.compas.engine.engine.system.influence.anomaly.*

open class Vortex : AnomalyHandler() {
    override val name = "Vortex"
    override val letter = "V"
    override val code = "01"

    override fun handle(anomaly: AnomalyEvent) {
        damageStream.onNext(
                DamageEvent(
                        DamageType.PHISICAL,
                        10,
                        null
                ))
    }
}