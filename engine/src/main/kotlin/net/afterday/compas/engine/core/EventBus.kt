package net.afterday.compas.engine.core

import io.reactivex.rxjava3.subjects.BehaviorSubject
import io.reactivex.rxjava3.subjects.Subject
import net.afterday.compas.engine.engine.system.damage.DamageEvent
import net.afterday.compas.engine.engine.system.influence.anomaly.AnomalyEvent

object EventBus{
    private var anomalyStream: Subject<AnomalyEvent> = BehaviorSubject.create()
    private var damageStream: Subject<DamageEvent> = BehaviorSubject.create()

    fun send(event: AnomalyEvent){
        anomalyStream.onNext(event)
    }
    fun send(event: DamageEvent){
        damageStream.onNext(event)
    }

    fun anomaly():Subject<AnomalyEvent>{
        return anomalyStream;
    }

    fun damage(): Subject<DamageEvent>{
        return damageStream
    }
}