package net.afterday.compas.sensors.Bluetooth;

import net.afterday.compas.sensors.Sensor;
import net.afterday.compas.sensors.SensorResult;

import java.util.List;

import io.reactivex.Observable;

/**
 * Created by Justas Spakauskas on 4/2/2018.
 */

public interface Bluetooth extends Sensor<List<SensorResult>>
{
    Observable<Double> getDetectorStream();
    void setLevel(int level);
}
