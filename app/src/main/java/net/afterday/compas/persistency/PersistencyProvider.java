package net.afterday.compas.persistency;

import net.afterday.compas.persistency.influences.InfluencesPersistency;
import net.afterday.compas.persistency.initialState.GameStatePersistency;
import net.afterday.compas.persistency.items.ItemsPersistency;
import net.afterday.compas.persistency.player.PlayerPersistency;

/**
 * Created by Justas Spakauskas on 2/10/2018.
 */

public interface PersistencyProvider
{
    InfluencesPersistency getInfluencesPersistency();
    ItemsPersistency getItemsPersistency();
    GameStatePersistency getInitialStatePersistency();
    PlayerPersistency getPlayerPersistency();
}
