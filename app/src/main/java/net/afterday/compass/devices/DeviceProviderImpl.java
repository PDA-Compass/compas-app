package net.afterday.compass.devices;

import android.content.Context;

import net.afterday.compass.devices.sound.Sound;

/**
 * Created by spaka on 4/18/2018.
 */

public class DeviceProviderImpl implements DeviceProvider
{
    private Context ctx;
    private Sound mSound;
    public DeviceProviderImpl(Context ctx)
    {
        this.ctx = ctx;
    }

    @Override
    public Sound getSoundPlayer()
    {
        if(mSound == null)
        {
            mSound = new Sound(ctx);
        }
        return mSound;
    }
}
