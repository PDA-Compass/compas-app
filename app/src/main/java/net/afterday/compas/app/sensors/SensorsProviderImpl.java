package net.afterday.compas.app.sensors;

import android.content.Context;
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
    private static SensorsProvider instance;
    private Context context;
    private SensorsProviderImpl(Context context)
    {
        this.context = context;
    }

    public static SensorsProvider initialize(Context context)
    {
        if(instance == null)
        {
            instance = new SensorsProviderImpl(context);
        }
        return instance;
    }

    public static SensorsProvider instance()
    {
        if(instance == null)
        {
            throw new IllegalStateException("Sensors provider not initialized");
        }
        return instance;
    }

    @Override
    public WiFi getWifiSensor()
    {
        return new WifiImpl(context);
    }

    @Override
    public Battery getBatterySensor()
    {
        return new BatteryImpl(context);
    }

    @Override
    public Bluetooth getBluetoothSensor()
    {
        return new BluetoothImpl(context);
    }

    @Override
    public Gps getGpsSensor()
    {
        return new GpsImpl(context);
    }

}
