package net.afterday.compass.engine.influences.WifiInfluences;

import net.afterday.compass.core.influences.Influence;

/**
 * Created by Justas Spakauskas on 2/4/2018.
 */

public class WifiConverter
{

    public static double convert(int type, int signal)
    {
        switch (type)
        {
            //case Influence.RADIATION: return Math.min(rad(signal), 16);
            case Influence.RADIATION: return rad(signal);
            case Influence.HEALTH: return rad(signal);
            case Influence.ANOMALY: return rad(signal);
            case Influence.BURER: return rad(signal);
            case Influence.CONTROLLER: return rad(signal);
            case Influence.MENTAL: return rad(signal);
            case Influence.ARTEFACT: return art(signal);
        }
        return 0f;
    }

    public static double rad(double signal)
    {
        ////Log.d("Convert RAD!---------", signal + "");
        if(signal > -30d) {
            return map(signal, -30d, 0d, 15.5d, 16d);
        }
        else if(signal > -40d) {
            return map(signal, -40d, -30d, 15d, 15.5d);
        }
        else if(signal > -50d) {
            return map(signal, -50d, -40d, 7d, 15d);
        }
        else if(signal > -70d) {
            return map(signal, -70d, -50d, 1d, 7d);
        }
        else if(signal > -100d) {
            return map(signal, -100d, -70d, 0d, 1d);
        }

        return 0f;
    }

    public static double ano(double signal)
    {
        if(signal > -60d) {
            return 100d;
        }
        else if(signal > -70d) {
            return map(signal, -70d, -60d, 7d, 15d);
        }
        else if(signal > -80d) {
            return map(signal, -80d, -70d, 1d, 7d);
        }
        else if(signal > -90d) {
            return map(signal, -90d, -80d, 0d, 1d);
        }

        return 0d;
    }

    public static double men(double signal)
{
    if(signal > -60d) {
        return 100d;
    }
    else if(signal > -70d) {
        return map(signal, -70d, -60d, 7d, 15d);
    }
    else if(signal > -80d) {
        return map(signal, -80d, -70d, 1d, 7d);
    }
    else if(signal > -90d) {
        return map(signal, -90d, -80d, 0d, 1d);
    }

    return 0d;
}
    public static double art(double signal)
    {
        if(signal > -40d) {
            return map(signal, -40d, 0d, 100d, 10000d);
        }
        else if(signal > -50d) {
            return map(signal, -50d, -40d, 50d, 100d);
        }
        else if(signal > -60d) {
            return map(signal, -60d, -50d, 15d, 50d);
        }
        else if(signal > -80d) {
            return map(signal, -80d, -60d, 7d, 15d);
        }
        else if(signal > -100d) {
            return map(signal, -100d, -80d, 0d, 7d);
        }

        return 0f;
    }

    public static double bur(double signal)
    {
        return ano(signal);
    }

    public static double con(double signal)
    {
        return men(signal);
    }

    public static double healing(float signal, float rate)
    {
        if(Math.abs(signal) <= rate) {
            return 100d;
        }

        return 0d;
    }

    public static double map(double value, double sFrom, double sTo, double dFrom, double dTo)
    {
        // Y = (X-A)/(B-A) * (D-C) + C
        return (value - sFrom) / (sTo - sFrom) * (dTo - dFrom) + dFrom;
    }

    public static float map(float value, float sFrom, float sTo, float dFrom, float dTo)
    {
        // Y = (X-A)/(B-A) * (D-C) + C
        return (value - sFrom) / (sTo - sFrom) * (dTo - dFrom) + dFrom;
    }
}