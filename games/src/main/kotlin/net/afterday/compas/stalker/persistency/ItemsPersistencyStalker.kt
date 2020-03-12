package net.afterday.compas.stalker.persistency

import net.afterday.compas.engine.core.inventory.items.ItemDescriptor
import net.afterday.compas.engine.persistency.items.ItemsPersistency
import net.afterday.compas.engine.setup.SetupItems
import net.afterday.compas.stalker.setup.SetupItemsStalker

class ItemsPersistencyStalker : ItemsPersistency {
    private val items: SetupItems = SetupItemsStalker()

    override fun getItemsAddeWithLevel(): Map<Int, List<ItemDescriptor>>? {
        return null;
    }

    override fun getItemsByCode(): Map<String, ItemDescriptor> {
        return items.items;
    }

    override fun getItemForCode(code: String): ItemDescriptor? {
        return items.items[code];
    }
}