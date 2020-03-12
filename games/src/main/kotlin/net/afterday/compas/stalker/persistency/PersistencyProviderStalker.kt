package net.afterday.compas.stalker.persistency

import net.afterday.compas.engine.persistency.PersistencyProvider
import net.afterday.compas.engine.persistency.influences.InfluencesPersistency
import net.afterday.compas.engine.persistency.initialState.GameStatePersistency
import net.afterday.compas.engine.persistency.items.ItemsPersistency
import net.afterday.compas.engine.persistency.player.PlayerPersistency

class PersistencyProviderStalker : PersistencyProvider {
    override val influencesPersistency: InfluencesPersistency = InfluencesPersistencyStalker()
    override val itemsPersistency: ItemsPersistency = ItemsPersistencyStalker()
    override val gameStatePersistency: GameStatePersistency
        get() = TODO("Not yet implemented")
    
    override val playerPersistency: PlayerPersistency = PlayerPersistencyStalker()
}