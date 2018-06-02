package net.afterday.compass.persistency;

import net.afterday.compass.persistency.influences.InfluencesPersistency;
import net.afterday.compass.persistency.initialState.GameStatePersistency;
import net.afterday.compass.persistency.items.ItemsPersistency;

/**
 * Created by Justas Spakauskas on 2/10/2018.
 */

public interface PersistencyProvider
{
    InfluencesPersistency getInfluencesPersistency();
    ItemsPersistency getItemsPersistency();
    GameStatePersistency getInitialStatePersistency();
}
