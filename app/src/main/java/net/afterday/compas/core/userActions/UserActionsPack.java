package net.afterday.compas.core.userActions;

import net.afterday.compas.core.events.EventsPack;
import net.afterday.compas.core.inventory.items.Events.AddItem;
import net.afterday.compas.core.inventory.items.Events.DropItem;
import net.afterday.compas.core.inventory.items.Events.UseItem;

import java.util.List;

/**
 * Created by Justas Spakauskas on 3/4/2018.
 */

public interface UserActionsPack extends EventsPack
{
    boolean hasAddItemEvents();
    boolean hasUseItemEvents();
    boolean hasDropItemEvents();

    List<AddItem> getAddItemEvents();
    List<UseItem> getUseItemEvents();
    List<DropItem> getDropItemEvents();
}
