package net.afterday.compass.core.userActions;

import net.afterday.compass.core.events.Event;

/**
 * Created by Justas Spakauskas on 2/4/2018.
 */

public interface UserAction extends Event
{
    String getActionType();
}
