package net.afterday.compas.core.influences;

import net.afterday.compas.core.events.Event;

/**
 * Created by Justas Spakauskas on 2/3/2018.
 */

public interface Influence extends Event
{
    int RADIATION = 0;
    int ANOMALY = 1;
    int MENTAL = 2;
    int BURER = 3;
    int CONTROLLER = 4;
    int HEALTH = 5;
    int ARTEFACT = 6;
    int MONOLITH = 7;
    int EMISSION = 8;

    double MIN = 0.1d;
    double MED = 1d;
    double MAX = 7d;
    double PEAK = 16d;

    int MAX_SATELLITES = 8;

    int INFLUENCE_COUNT = 9;
    double NULL = -99999999999.9999d;

    double ANOMALY_PEAK = 16;
    double BURER_PEAK = 16;
    double RADIATION_PEAK = 16;
    double CONTROLLER_PEAK = 16;
    double MENTAL_PEAK = 16;
    double MONOLITH_PEAK = 16;

    enum SOURCE {WIFI, BLUETOOTH, ALL};

    String getName();
    String getId();
    boolean affects(int what);
    boolean isDanger();
    double getStrength();
    int getTypeId();
}
