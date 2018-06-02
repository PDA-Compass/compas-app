package net.afterday.compass.core.inventory.items.Events;

import net.afterday.compass.core.inventory.items.Item;
import net.afterday.compass.core.player.XpChanged;

/**
 * Created by spaka on 5/22/2018.
 */

public interface ItemAdded extends XpChanged
{
    Item getItem();
}
