package net.afterday.compas.settings;

/**
 * Created by spaka on 7/22/2018.
 */

public interface SettingsListener
{
    public void onSettingChanged(String key, String value);
}
