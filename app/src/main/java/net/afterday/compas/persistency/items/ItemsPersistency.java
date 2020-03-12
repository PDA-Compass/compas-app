package net.afterday.compas.persistency.items;

import java.util.List;
import java.util.Map;

/**
 * Created by Justas Spakauskas on 2/10/2018.
 */

public interface ItemsPersistency
{
    Map<String, ItemDescriptor> getItemsByCode();
    ItemDescriptor getItemForCode(String code);
    Map<Integer, List<ItemDescriptor>> getItemsAddeWithLevel();
}