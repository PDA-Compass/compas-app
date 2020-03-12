package net.afterday.compas.core.userActions;

import net.afterday.compas.core.events.Event;

/**
 * Created by Justas Spakauskas on 2/4/2018.
 */

public interface UserAction extends Event
{
    String getActionType();
}
