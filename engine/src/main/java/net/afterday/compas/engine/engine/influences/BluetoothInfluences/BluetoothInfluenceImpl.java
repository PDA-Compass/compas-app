package net.afterday.compas.engine.engine.influences.BluetoothInfluences;

import net.afterday.compas.engine.core.log.Log;
import net.afterday.compas.engine.core.influences.Influence;
import net.afterday.compas.engine.core.influences.Influences;
import net.afterday.compas.engine.sensors.Bluetooth.BluetoothScanResult;
import net.afterday.compas.engine.util.Convert;

public class BluetoothInfluenceImpl implements BluetoothInfluence
{
    private static final String TAG = "BluetoothInfl";
    private int strength;
    public BluetoothInfluenceImpl(BluetoothScanResult bluetoothScanResult)
    {
        Log.d(TAG, "***********************");
        this.strength = bluetoothScanResult.getStrength();
    }

    @Override
    public long getTimestamp()
    {
        return 0;
    }

    @Override
    public String getName()
    {
        return Influences.ARTEFACT;
    }

    @Override
    public String getId()
    {
        return null;
    }

    @Override
    public boolean affects(int what)
    {
        return false;
    }

    @Override
    public boolean isDanger()
    {
        return false;
    }

    @Override
    public double getStrength()
    {
        //Log.d(TAG, this.strength + " " + Thread.currentThread().getName() + " " + Convert.map((float)this.strength, 0, -100, 1, 100));
        return Convert.map((float)this.strength, -100, 0, 1, 100);
    }

    @Override
    public int getTypeId()
    {
        return Influence.ARTEFACT;
    }

    @Override
    public String toString()
    {
        return "BluetoothInfluence - name: " + getName() + "; strength: " + getStrength();
    }
}
