package net.afterday.compas.engine.setup

import net.afterday.compas.engine.core.player.Player

abstract class SetupFraction {
    val fractions: HashMap<String, Player.FRACTION> = HashMap()
    init {
        this.setup()
    }

    abstract fun setup(): Unit
}