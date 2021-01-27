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
    int SPRINGBOARD = 9;
    int FUNNEL = 10;
    int CAROUSEL = 11;
    int ELEVATOR = 12;
    int FRYING = 13;
    int ELECTRA = 14;
    int MEATGRINDER = 15;
    int KISSEL = 16;
    int SODA = 17;
    int ACIDFOG = 18;
    int BURNINGFLUFF = 19;
    int RUSTYHAIR = 20;
    int SPATIALBUBBLE = 21;

    double MIN = 0.1d;
    double MED = 1d;
    double MAX = 7d;
    double PEAK = 16d;

    int MAX_SATELLITES = 8;

    int INFLUENCE_COUNT = 9;
    double NULL = -99999999999.9999d;

    double ANOMALY_PEAK = 16;
    double SPRINGBOARD_PEAK = 16;
    double FUNNEL_PEAK = 16;
    double CAROUSEL_PEAK = 16;
    double ELEVATOR_PEAK = 16;
    double FRYING_PEAK = 16;
    double ELECTRA_PEAK = 16;
    double MEATGRINDER_PEAK = 16;
    double KISSEL_PEAK = 16;
    double SODA_PEAK = 16;
    double ACIDFOG_PEAK = 16;
    double BURNINGFLUFF_PEAK = 16;
    double RUSTYHAIR_PEAK = 16;
    double SPATIALBUBBLE_PEAK = 16;
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
