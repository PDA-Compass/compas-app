package net.afterday.compas.engine.core

import net.afterday.compas.engine.core.gameState.Frame
import net.afterday.compas.engine.core.influences.InfluencesPack
import net.afterday.compas.engine.core.inventory.Inventory
import net.afterday.compas.engine.core.inventory.items.Item
import net.afterday.compas.engine.core.player.Player

interface Game {
    fun start(): Frame
    fun acceptInfluences(influences: InfluencesPack): Frame

    val player: Player
    val inventory: Inventory

    fun acceptCode(code: String): Boolean
    fun useItem(item: Item): Frame
}