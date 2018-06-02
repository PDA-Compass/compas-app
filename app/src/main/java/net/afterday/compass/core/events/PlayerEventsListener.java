package net.afterday.compass.core.events;

import net.afterday.compass.core.inventory.items.Events.ItemAdded;
import net.afterday.compass.core.player.Impacts;
import net.afterday.compass.core.player.Player;

/**
 * Created by spaka on 5/8/2018.
 */

public interface PlayerEventsListener
{
    void onPlayerStateChanged(Player.STATE oldState, Player.STATE newState);
    void onImpactsStateChanged(Impacts.STATE oldState, Impacts.STATE newState);
    void onItemAdded(ItemAdded itemAdded);
    void onPlayerLevelChanged(int level);
}
