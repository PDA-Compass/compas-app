package net.afterday.compas.settings;

/**
 * Created by spaka on 7/22/2018.
 */

public class Defaults
{
    public static boolean getDefaultBool(String key)
    {
        switch (key)
        {
            case Constants.VIBRATION: return true;
            case Constants.COMPASS: return true;
        }
        return false;
    }

    public static int getDefaultInt(String key)
    {
        switch (key)
        {
            case Constants.ORIENTATION: return Constants.ORIENTATION_LANDSCAPE;
        }
        return -1;
    }
}
