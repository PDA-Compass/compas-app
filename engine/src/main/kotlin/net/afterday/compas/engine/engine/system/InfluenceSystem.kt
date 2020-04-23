package net.afterday.compas.engine.engine.system

import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.subjects.BehaviorSubject
import io.reactivex.rxjava3.subjects.Subject
import net.afterday.compas.engine.Services
import net.afterday.compas.engine.core.EventBus
import net.afterday.compas.engine.core.log.Audit
import net.afterday.compas.engine.core.log.Log
import net.afterday.compas.engine.engine.actions.Action
import net.afterday.compas.engine.engine.actions.ActionsExecutor
import net.afterday.compas.engine.engine.events.EmissionEventBus
import net.afterday.compas.engine.engine.system.influence.anomaly.AnomalyEvent
import net.afterday.compas.engine.engine.system.influence.AnomalyProcess
import net.afterday.compas.engine.persistency.PersistencyProvider
import net.afterday.compas.engine.sensors.SensorsProvider
import java.util.*
import java.util.concurrent.TimeUnit

open class InfluenceSystem public constructor(playerLevel: Subject<Integer>,
                                              gameRunning: Observable<Long>) {
    private val TAG = "Influence System"

    private val acceptsInfluences: Subject<Boolean> = BehaviorSubject.create()
    private val influencesRunning: Subject<Boolean> = BehaviorSubject.createDefault(false)

    private var currentEmission:CompositeDisposable? = CompositeDisposable()

    private val executor: ActionsExecutor = ActionsExecutor.instance(gameRunning)
    private var sensorsProvider: SensorsProvider = Services.sensorsProvider
    private var persistencyProvider: PersistencyProvider = Services.persistencyProvider
    private val gameRunning: Observable<Long> = gameRunning

    val anomalyStream: Subject<AnomalyEvent> = EventBus.anomaly();

    private fun initSensor() {
        val anomalyProcess = AnomalyProcess(anomalyStream)

        //Bluetooth
        val bluetoothSensor = sensorsProvider.bluetoothSensor
        bluetoothSensor
                .sensorResultsStream
                .filter { anomalyProcess.filter(it) }
                .subscribe {
                    anomalyProcess.process(it)
                }
        bluetoothSensor.start()

        //WiFi
        val wifiSensor = sensorsProvider.wifiSensor
        wifiSensor
                .sensorResultsStream
                .filter {anomalyProcess.filter(it)}
                .subscribe {
                    anomalyProcess.process(it)
                }
        wifiSensor.start()
    }

    private fun initAnomaly(){
        anomalyStream.subscribe {
        }
    }

    init {
        initSensor()
        initAnomaly()

        //influenceProvider = InfluenceProviderImpl(sensorsProvider, persistencyProvider.influencesPersistency, gameRunning)
        //influencesStream = influenceProvider.influenceStream

        // on/off influence
        /*Observables.combineLatest(influencesRunning, playerLevel){
            i, level -> Pair(i,level)
        }.subscribe{
            if (it.first) {
                influenceProvider.start(1)
            } else {
                influenceProvider.stop(it.second as Int)
            }
        }

        setupEmissions()
*/    }

    fun isStarted(value:Boolean) {
        acceptsInfluences.onNext(value)
        influencesRunning.onNext(value)
    }

    private  fun notifyFakeEmission() {
        Log.e(TAG, "EMISSION FAKE")
        Audit.d("R.string.message_emission_fake")
        EmissionEventBus.instance().fakeEmission()
    }

    private fun notifyEmission(emissionStartAfter: Int) {
        Log.e(TAG, "EMISSION WILL START")
        Audit.e("R.string.message_emission_approaching")
        EmissionEventBus.instance().emissionWillStart(emissionStartAfter)
    }

    private fun startEmission(endAfter: Int) {
        Log.e(TAG, "EMISSION STARTED")
        Audit.e("R.string.message_emission_started")
        EmissionEventBus.instance().setEmissionActive(true)
        if (currentEmission == null) {
            currentEmission = CompositeDisposable()
        }
        currentEmission!!.add(Observable.timer(endAfter.toLong(), TimeUnit.MINUTES).take(1).subscribe { t: Long? -> emissionEnded() })
    }

    public fun emissionEnded() {
        if (currentEmission != null && !currentEmission!!.isDisposed) {
            currentEmission!!.dispose()
            currentEmission = null
        }
        Audit.d("R.string.message_emission_ended")
        //influenceProvider.stopEmission()
        EmissionEventBus.instance().setEmissionActive(false)
    }

    private fun calToStr(c: Calendar): String {
        return "Year: " + c[Calendar.YEAR] + " Month: " + c[Calendar.MONTH] + " Day: " + c[Calendar.DAY_OF_MONTH] + " Hour: " + c[Calendar.HOUR_OF_DAY] + " Min: " + c[Calendar.MINUTE] + " Sec: " + c[Calendar.SECOND] + " Milis: " + c.timeInMillis
    }

    private  fun setupEmissions() {
        val d: Disposable = CompositeDisposable()
        val emissions = persistencyProvider.influencesPersistency.emissions
        val now = System.currentTimeMillis()
        for (e in emissions) {
            val strtAt = e.startTime
            if (strtAt.timeInMillis + e.duration * 1000 * 60 < now || e.fake && strtAt.timeInMillis < now) {
                continue
            }
            if (now < strtAt.timeInMillis - e.notifyBefore * 1000 * 60) {
                executor.postAction(object : Action {
                    override fun startTime(): Long {
                        return strtAt.timeInMillis - e.notifyBefore * 1000 * 60
                    }

                    override fun execute() {
                        notifyEmission(e.notifyBefore)
                    }

                    override fun toString(): String {
                        return "Notify emission action: " + calToStr(strtAt)
                    }
                })
            }
            executor.postAction(object : Action {
                override fun startTime(): Long {
                    return e.startTime.timeInMillis
                }

                override fun execute() {
                    if (e.fake) {
                        notifyFakeEmission()
                        return
                    }
                    if (strtAt.timeInMillis < now) {
                        startEmission((e.duration - (now - strtAt.timeInMillis) / 1000 / 60).toInt())
                    } else {
                        startEmission(e.duration)
                    }
                }

                override fun toString(): String {
                    return "Start emission action: " + calToStr(strtAt)
                }
            })
        }
    }
}