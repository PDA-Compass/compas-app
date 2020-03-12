package net.afterday.compas.engine.persistency;

import net.afterday.compas.engine.persistency.influences.InfluencesPersistency;
import net.afterday.compas.engine.persistency.initialState.GameStatePersistency;
import net.afterday.compas.engine.persistency.items.ItemsPersistency;
import net.afterday.compas.engine.persistency.player.PlayerPersistency;

interface PersistencyProvider
{
    val influencesPersistency: InfluencesPersistency
    val itemsPersistency: ItemsPersistency
    val gameStatePersistency: GameStatePersistency
    val playerPersistency: PlayerPersistency
}
