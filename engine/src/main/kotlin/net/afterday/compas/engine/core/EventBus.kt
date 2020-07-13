package net.afterday.compas.engine.core

import io.reactivex.rxjava3.subjects.BehaviorSubject
import io.reactivex.rxjava3.subjects.PublishSubject
import io.reactivex.rxjava3.subjects.Subject
import net.afterday.compas.engine.engine.system.damage.DamageEvent
import net.afterday.compas.engine.engine.system.influence.anomaly.AnomalyEvent

object EventBus{
    private var anomalyStream: Subject<AnomalyEvent> = BehaviorSubject.create()
    private var damageStream: Subject<DamageEvent> = BehaviorSubject.create()
    private val commandsStream: Subject<String> = PublishSubject.create()

    fun anomaly():Subject<AnomalyEvent>{
        return anomalyStream;
    }

    fun damage(): Subject<DamageEvent>{
        return damageStream
    }

    fun commands(): Subject<String>{
        return commandsStream
    }

    object Player {
        val stateStream: Subject<Boolean> = BehaviorSubject.create()
        private val statesStream: Subject<net.afterday.compas.engine.core.player.Player.STATE> = BehaviorSubject.create()
        private val fractionStream: Subject<net.afterday.compas.engine.core.player.Player.FRACTION> = BehaviorSubject.create()

        fun states(): Subject<net.afterday.compas.engine.core.player.Player.STATE> {
            return statesStream
        }

        fun fraction(): Subject<net.afterday.compas.engine.core.player.Player.FRACTION> {
            return fractionStream
        }
    }
}