package net.afterday.compas.settings;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.net.sip.SipSession;
import android.util.Log;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by spaka on 7/21/2018.
 */
public class Settings
{
    private static Settings instance;
    private List<WeakReference<SettingsListener>> listeners;
    private SharedPreferences prefs;
    private SharedPreferences.OnSharedPreferenceChangeListener innerListener;
    private Context context;
    private Resources res;

    private Settings(Context context)
    {
        this.context = context;
        listeners = new ArrayList<>();
        prefs = context.getSharedPreferences(Constants.SETTINGS, Context.MODE_PRIVATE);
        res = context.getResources();
    }

    public static Settings instance()
    {
        if (instance == null)
        {
            throw new IllegalStateException("Settings must be initialized.");
        }
        return instance;
    }

    public static Settings instance(Context context)
    {
        if(instance == null)
        {
            instance = new Settings(context);
        }
        return instance;
    }

    public boolean getBoolSetting(String key)
    {
        return prefs.getBoolean(key, Defaults.getDefaultBool(key));
    }

    public void setBoolSetting(String key, boolean val)
    {
        prefs.edit().putBoolean(key, val).apply();
        notifySettingChanged(key, String.valueOf(val));
    }

    public int getIntSetting(String key)
    {
        return prefs.getInt(key, Defaults.getDefaultInt(key));
    }

    public void setIntSetting(String key, int val)
    {
        prefs.edit().putInt(key, val).apply();
        notifySettingChanged(key, Integer.toString(val));
    }

    private void notifySettingChanged(String key, String val)
    {
        Log.e("SETTINGS", "notifySettingChanged: " + Thread.currentThread().getName());
        Iterator<WeakReference<SettingsListener>> iter = listeners.iterator();
        while(iter.hasNext())
        {
            SettingsListener l = iter.next().get();
            if(l == null)
            {
                iter.remove();
                continue;
            }
            l.onSettingChanged(key, val);
        }
    }

    public void addSettingsListener(SettingsListener listener)
    {
        listeners.add(new WeakReference<SettingsListener>(listener));
    }
}
