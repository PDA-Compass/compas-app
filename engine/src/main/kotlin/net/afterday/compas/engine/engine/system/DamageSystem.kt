package net.afterday.compas.engine.engine.system

import io.reactivex.rxjava3.subjects.Subject
import net.afterday.compas.engine.core.EventBus
import net.afterday.compas.engine.core.player.PlayerPropsImpl
import net.afterday.compas.engine.engine.system.damage.DamageEvent
import net.afterday.compas.engine.engine.system.damage.DamageType
import net.afterday.compas.engine.engine.system.player.PlayerState

open class DamageSystem {
    data class DamageEffectEvent(
            var type: Byte,
            var value: Double
    )

    private val TAG = "Damage System"
    val damageStream: Subject<DamageEvent> = EventBus.damage()
    private val playerStream = EventBus.Player.stateStream;

    val map = LinkedHashMap<String, Long>()

    init {
        damageStream
                .subscribe {
                    var r = toDamage(it);
                    if (r != null) {
                        r = processItems(r)
                        effect(r)
                    }
                }
    }
    private fun toDamage(damage: DamageEvent): DamageEffectEvent? {
        if (map.containsKey(damage.id)){
            if (damage.value < 0.01f){
                map.remove(damage.id)
            }
            else {
                val time = (damage.at - map[damage.id]!!).toDouble(); //TODO: to sec
                map[damage.id] = damage.at
                return DamageEffectEvent(damage.type, (damage.value.toDouble() * (time/50000)))
            }
        }
        else
        {
            map[damage.id] = damage.at
        }
        return null;
    }

    private fun processItems(damage: DamageEffectEvent):DamageEffectEvent {
        return damage;
    }

    private fun effect(damage:DamageEffectEvent){
        //TODO: time
        if (damage.type == DamageType.RADIATION) {
            PlayerState.rad += damage.value
            playerStream.onNext(true)
        }
        if (damage.type == DamageType.FIRE){
            PlayerState.health -+ damage.value
            playerStream.onNext(true)
        }
    }
}