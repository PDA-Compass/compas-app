package net.afterday.compas.engine.core

import net.afterday.compas.engine.core.gameState.Frame
import net.afterday.compas.engine.core.gameState.FrameImpl
import net.afterday.compas.engine.core.influences.InfluencesPack
import net.afterday.compas.engine.core.inventory.Inventory
import net.afterday.compas.engine.core.inventory.InventoryImpl
import net.afterday.compas.engine.core.inventory.items.Item
import net.afterday.compas.engine.core.player.Player
import net.afterday.compas.engine.core.player.Player.COMMAND
import net.afterday.compas.engine.core.player.PlayerImpl
import net.afterday.compas.engine.core.serialization.Serializer
import net.afterday.compas.engine.persistency.PersistencyProvider

class GameImpl(private val persistencyProvider: PersistencyProvider, serializer: Serializer?) : Game {
    private val mPlayer: PlayerImpl = PlayerImpl(InventoryImpl(persistencyProvider.itemsPersistency, serializer), serializer)
    override fun start(): Frame {
        return FrameImpl(mPlayer.playerProps, mPlayer.equipment)
    }

    override fun acceptInfluences(influences: InfluencesPack): Frame {
        return mPlayer.acceptInfluences(influences)
    }

    override val player: Player
        get() = mPlayer

    override val inventory: Inventory
        get() = mPlayer.inventory

    override fun acceptCode(code: String): Boolean {
        val itemDesc = persistencyProvider.itemsPersistency.getItemForCode(code)
        if (itemDesc != null) {
            return mPlayer.addItem(itemDesc, code)
        }

        val fraction = persistencyProvider.playerPersistency.getFractionByCode(code)
        if (fraction != null) {
            mPlayer.setFraction(fraction)
            return true
        }
        val command = persistencyProvider.playerPersistency.getCommandByCode(code)
        return if (command != null) {
            when (command) {
                COMMAND.KILL -> {
                    mPlayer.setState(Player.STATE.DEAD_BURER)
                    true
                }
                COMMAND.REVIVE -> {
                    mPlayer.reborn()
                    true
                }
            }
        } else false
    }

    override fun useItem(item: Item): Frame {
        return mPlayer.useItem(item)
    }
}