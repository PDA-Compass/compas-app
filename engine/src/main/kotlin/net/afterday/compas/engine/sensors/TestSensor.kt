package net.afterday.compas.engine.sensors

import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.subjects.PublishSubject
import io.reactivex.rxjava3.subjects.Subject
import net.afterday.compas.engine.common.Time

class TestSensor: Sensor<SensorResult> {
    class A(val id:String, val name:String, val stream: Subject<SensorResult>) {
        operator fun invoke(value: Long){
            stream.onNext(SensorResult(id, name, value, Time.now))
        }
    }

    private val stream: Subject<SensorResult> = PublishSubject.create()
    private val rad = A("22", "R100", stream)
    private val men = A("33", "M100", stream)

    private fun sleep(sec: Long){
        Thread.sleep(sec * 1000)
    }

    override fun start() {
        Thread {
            while(true) {
                sleep(10)
                rad(-60)
                sleep(10)
                rad(-80)
                sleep(10)
                rad(-60)
                sleep(10)
                rad(-80)
                //men(-100)
            }
        }.start()
    }

    override fun stop() {
        TODO("Not yet implemented")
    }

    override fun getSensorResultsStream(): Observable<SensorResult> {
        return stream;
    }

}