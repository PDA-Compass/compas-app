package net.afterday.compas.engine.engine.system

import io.reactivex.rxjava3.subjects.Subject
import net.afterday.compas.engine.core.EventBus
import net.afterday.compas.engine.engine.system.damage.DamageEvent

open class DamageSystem {
    private val TAG = "Damage System"
    val damageStream: Subject<DamageEvent> = EventBus.damage()
}