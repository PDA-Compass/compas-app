package net.afterday.compas.util;

public class Convert
{
    public static float RGB_GREY = -1f;
    public static float RGB_BLUE = -2f;

    public static double rad(double signal)
    {
        if(signal > -40d) {
            return map(signal, -40d, 0d, 100d, 100000d);
        }
        else if(signal > -50d) {
            return map(signal, -50d, -40d, 15d, 100d);
        }
        else if(signal > -60d) {
            return map(signal, -60d, -50d, 7d, 15d);
        }
        else if(signal > -80d) {
            return map(signal, -80d, -60d, 1d, 7d);
        }
        else if(signal > -100d) {
            return map(signal, -100d, -80d, 0d, 1d);
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
        else if(signal > -80d) {
            return map(signal, -80d, -60d, 7d, 15d);
        }
        else if(signal > -90d) {
            return map(signal, -90d, -80d, 1d, 7d);
        }
        else if(signal > -100d) {
            return map(signal, -100d, -80d, 0d, 1d);
        }

        return 0d;
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

        if(java.lang.Math.abs(signal) <= rate) {
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

    public static int[] numberToRGB(double number)
    {
        int[] rgb = {255, 0, 0, 0};

        // Grey
        if(number == -1d) {
            rgb[1] = 65;
            rgb[2] = 65;
            rgb[3] = 65;
        }

        // Blue
        else if(number == -2d) {
            rgb[1] = 0;
            rgb[2] = 255;
            rgb[3] = 255;
        }
        else if(number >= 0d && number < 50d) {
            rgb[1] = 255;
            rgb[2] = (int) map(number, 0d, 50d, 0d, 255d);
            rgb[3] = 0;
        }
        else if(number >= 50d && number < 100d) {
            rgb[1] = (int) map(number, 50d, 100d, 255d, 0d);
            rgb[2] = 255;
            rgb[3] = 0;
        }
        else if(number >= 100d) {
            rgb[1] = 0;
            rgb[2] = 255;
            rgb[3] = 0;
        }

        return rgb;
    }
}
