package net.afterday.compas.engine.persistency.player

import net.afterday.compas.engine.core.player.Player

interface PlayerPersistency {
    fun getFractionByCode(code: String): Player.FRACTION?
    fun getCommandByCode(code: String): Player.COMMAND?
}