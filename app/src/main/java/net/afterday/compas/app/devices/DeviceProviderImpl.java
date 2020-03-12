package net.afterday.compas.app.devices;

import android.content.Context;

import net.afterday.compas.app.devices.sound.SoundImpl;
import net.afterday.compas.engine.devices.sound.Sound;
import net.afterday.compas.engine.devices.vibro.Vibro;
import net.afterday.compas.app.devices.vibro.VibroImpl;
import net.afterday.compas.engine.devices.DeviceProvider;

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
            sound = new SoundImpl(ctx);
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
