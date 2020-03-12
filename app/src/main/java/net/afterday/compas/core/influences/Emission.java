package net.afterday.compas.core.influences;

import java.util.Calendar;

/**
 * Created by spaka on 6/7/2018.
 */

public interface Emission
{
    Calendar getStartTime();
    int notifyBefore();
    int duration();
    boolean isFake();
}
