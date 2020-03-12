package net.afterday.compas.core.userActions;

import net.afterday.compas.core.inventory.items.Item;

/**
 * Created by Justas Spakauskas on 2/4/2018.
 */

public class AddItemAction implements UserAction
{
    private Item item;
    private long timestamp;
    public AddItemAction(Item item)
    {
        this.item = item;
        timestamp = System.currentTimeMillis();
    }

    @Override
    public String getActionType()
    {
        return null;
    }

    @Override
    public long getTimestamp()
    {
        return 0;
    }

    public Item getItem()
    {
        return item;
    }
}
