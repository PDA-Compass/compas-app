package net.afterday.compas.app.sensors;

import android.content.Context;
import io.reactivex.rxjava3.core.Observable;
import net.afterday.compas.engine.sensors.SensorResult;
import net.afterday.compas.engine.sensors.SensorsProvider;
import net.afterday.compas.engine.sensors.Battery.Battery;
import net.afterday.compas.app.sensors.Battery.BatteryImpl;
import net.afterday.compas.engine.sensors.Bluetooth.Bluetooth;
import net.afterday.compas.app.sensors.Bluetooth.BluetoothImpl;
import net.afterday.compas.engine.sensors.Gps.Gps;
import net.afterday.compas.app.sensors.Gps.GpsImpl;
import net.afterday.compas.engine.sensors.WiFi.WiFi;
import net.afterday.compas.app.sensors.WiFi.WifiImpl;

public class SensorsProviderImpl implements SensorsProvider
{
    private Context context;
    private Bluetooth bluetooth;
    private WiFi wifi;

    public SensorsProviderImpl(Context context)
    {
        this.context = context;
        wifi = new WifiImpl(context);
        bluetooth = new BluetoothImpl(context);
        new GpsImpl(context);
    }

    @Override
    public WiFi getWifiSensor()
    {
        return wifi;
    }

    @Override
    public Battery getBatterySensor()
    {
        return new BatteryImpl(context);
    }

    @Override
    public Bluetooth getBluetoothSensor()
    {
        return bluetooth;
    }

    @Override
    public Gps getGpsSensor()
    {
        return null;
    }

}
