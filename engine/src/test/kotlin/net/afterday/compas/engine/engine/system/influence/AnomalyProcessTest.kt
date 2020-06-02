package net.afterday.compas.engine.engine.system.influence

import io.reactivex.rxjava3.subjects.BehaviorSubject
import io.reactivex.rxjava3.subjects.Subject
import net.afterday.compas.engine.Services
import net.afterday.compas.engine.core.EventBus
import net.afterday.compas.engine.engine.system.InfluenceSystem
import net.afterday.compas.engine.engine.system.influence.anomaly.AnomalyEvent
import net.afterday.compas.engine.persistency.PersistencyProvider
import net.afterday.compas.engine.sensors.Bluetooth.Bluetooth
import net.afterday.compas.engine.sensors.SensorResult
import net.afterday.compas.engine.sensors.SensorsProvider
import net.afterday.compas.engine.sensors.WiFi.WiFi
import org.mockito.Mockito
import org.testng.annotations.BeforeMethod
import org.testng.annotations.Test
import java.util.concurrent.TimeUnit


open class AnomalyProcessTest {
    private var bluetoothStream: Subject<SensorResult>? = null
    private var influenceSystem: InfluenceSystem? = null
    private var wifiStream: Subject<SensorResult>? = null

    @BeforeMethod fun before(){
        val playerLevel = BehaviorSubject.create<Integer>()
        val sensorsProvider = Mockito.mock(SensorsProvider::class.java)
        val persistencyProvider = Mockito.mock(PersistencyProvider::class.java)
        val gameRunning = BehaviorSubject.create<Long>()

        Services.sensorsProvider = sensorsProvider
        Services.persistencyProvider = persistencyProvider

        val bluetooth = Mockito.mock(Bluetooth::class.java)
        Mockito.`when`(sensorsProvider.bluetoothSensor).thenReturn(bluetooth)
        bluetoothStream = BehaviorSubject.create()
        Mockito.`when`(bluetooth.sensorResultsStream).thenReturn(bluetoothStream)

        val wifi = Mockito.mock(WiFi::class.java)
        Mockito.`when`(sensorsProvider.wifiSensor).thenReturn(wifi)
        wifiStream = BehaviorSubject.create()
        Mockito.`when`(wifi.sensorResultsStream).thenReturn(wifiStream)

        influenceSystem = InfluenceSystem(playerLevel, gameRunning)
        influenceSystem!!.anomalyStream.onNext(AnomalyEvent("", 50,0, null, 10))
    }

    @Test fun `anomaly bluethooth tests`() {
        val subscriber = EventBus.anomaly().test()
        subscriber.assertValue{it.id == "" }

        bluetoothStream!!.onNext(SensorResult("e9:f4:b9:95:21:80", "B10", 45, 10) )
        bluetoothStream!!.onNext(SensorResult("e9:f4:b9:95:21:80", "V10", 46, 10) )

        subscriber.awaitDone(2, TimeUnit.SECONDS)
        subscriber.assertValueAt(1) {it.type == 0 }
        subscriber.assertValueAt(2) {it.type == 1 }
    }

    @Test fun `anomaly wifi tests`() {
        val subscriber = influenceSystem!!.anomalyStream.test()
        subscriber.assertValue{it.id == "" }

        wifiStream!!.onNext(SensorResult("e9:f4:b9:95:21:80", "B10", 45, 10) )
        wifiStream!!.onNext(SensorResult("e9:f4:b9:95:21:80", "V10", 46, 10) )

        subscriber.awaitDone(2, TimeUnit.SECONDS)
        subscriber.assertValueAt(1) {it.type == 0 }
        subscriber.assertValueAt(2) {it.type == 1 }
    }

    @Test fun `anomaly mix wifi and bluetooth tests`() {
        val subscriber = influenceSystem!!.anomalyStream.test()
        subscriber.assertValue{it.id == "" }

        wifiStream!!.onNext(SensorResult("e9:f4:b9:95:21:80", "B10", 45, 10) )
        bluetoothStream!!.onNext(SensorResult("e9:f4:b9:95:21:81", "V10", 45, 10))

        subscriber.awaitDone(2, TimeUnit.SECONDS)
        subscriber.assertValueAt(1) {it.type == 0 }
        subscriber.assertValueAt(2) {it.type == 1 }
    }
}