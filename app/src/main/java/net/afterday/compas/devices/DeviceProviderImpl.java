package net.afterday.compas.devices;

import android.content.Context;

import net.afterday.compas.devices.sound.Sound;
import net.afterday.compas.devices.vibro.Vibro;
import net.afterday.compas.devices.vibro.VibroImpl;

/**
 * Created by spaka on 4/18/2018.
 */

public class DeviceProviderImpl implements DeviceProvider
{
    private Context ctx;
    private Sound sound;
    private Vibro vibro;
    public DeviceProviderImpl(Context ctx)
    {
        this.ctx = ctx;
    }

    @Override
    public Sound getSoundPlayer()
    {
        if(sound == null)
        {
            sound = new Sound(ctx);
        }
        return sound;
    }

    @Override
    public Vibro getVibrator()
    {
        if(vibro == null)
        {
            vibro = new VibroImpl(ctx);
        }
        return vibro;
    }
}
