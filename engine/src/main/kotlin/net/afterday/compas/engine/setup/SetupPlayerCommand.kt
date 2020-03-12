package net.afterday.compas.engine.setup

import net.afterday.compas.engine.core.player.Player

abstract class SetupPlayerCommand {
    val command: HashMap<String, Player.COMMAND> = HashMap()
    init {
        this.setup()
    }

    abstract fun setup(): Unit
}