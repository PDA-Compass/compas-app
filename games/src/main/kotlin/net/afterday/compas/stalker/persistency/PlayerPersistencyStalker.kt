package net.afterday.compas.stalker.persistency

import net.afterday.compas.engine.core.player.Player
import net.afterday.compas.engine.persistency.player.PlayerPersistency
import net.afterday.compas.engine.setup.SetupFraction
import net.afterday.compas.engine.setup.SetupPlayerCommand
import net.afterday.compas.stalker.setup.SetupFractionStalker
import net.afterday.compas.stalker.setup.SetupPlayerCommandStalker

class PlayerPersistencyStalker : PlayerPersistency {
    private val fraction: SetupFraction = SetupFractionStalker()
    private val command: SetupPlayerCommand = SetupPlayerCommandStalker()

    override fun getFractionByCode(code: String): Player.FRACTION? {
        return fraction.fractions[code];
    }

    override fun getCommandByCode(code: String): Player.COMMAND? {
        return command.command[code]
    }
}