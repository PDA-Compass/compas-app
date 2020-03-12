package net.afterday.compas.engine.core.inventory

import net.afterday.compas.engine.core.inventory.items.Item
import net.afterday.compas.engine.core.inventory.items.ItemDescriptor
import net.afterday.compas.engine.core.player.PlayerProps
import net.afterday.compas.engine.core.serialization.Jsonable

interface Inventory : Jsonable
{
    fun addItem(item: ItemDescriptor, code: String):Item
    fun useItem(item: Item, playerProps: PlayerProps):PlayerProps
    fun dropItem(item:Item): Boolean

    val items: List<Item>
    val armors: List<Item>
    val boosters: List<Item>
    val devices: List<Item>

    fun setPlayerLevel(level: Int)
    val artifacts: DoubleArray

    fun hasHealthInstant(): Boolean
    fun hasRadiationInstant(): Boolean

}