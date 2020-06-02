package net.afterday.compas.engine.game.anomaly

import net.afterday.compas.engine.engine.system.damage.*
import net.afterday.compas.engine.engine.system.influence.anomaly.*

open class Radiation: AnomalyHandler() {
    override val name = "Radiation"
    override val letter = "R"
    override val code = "0C"

    override fun handle(anomaly: AnomalyEvent) {
        damageStream.onNext(
                DamageEvent(
                        DamageType.RADIATION,
                        10,
                        null
                ))
    }
}