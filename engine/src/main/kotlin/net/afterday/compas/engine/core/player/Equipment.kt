package net.afterday.compas.engine.core.player

import net.afterday.compas.engine.core.inventory.items.Item

public interface Equipment {
    val armor: Item?
    val device: Item?
    val booster: Item?

    fun useItem(item: Item): Boolean
    fun consumeArmor(delta: Long)
    fun consumeBooster(delta: Long)
    fun consumeDevice(delta: Long)

    val armorPercents: Double
    val boosterPercents: Double
    val devicePercents: Double
}