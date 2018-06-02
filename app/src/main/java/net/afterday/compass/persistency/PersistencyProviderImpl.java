package net.afterday.compass.persistency;

import net.afterday.compass.persistency.hardcoded.HGameStatePersistency;
import net.afterday.compass.persistency.hardcoded.HInfluencesPersistency;
import net.afterday.compass.persistency.hardcoded.HItemsPersistency;
import net.afterday.compass.persistency.influences.InfluencesPersistency;
import net.afterday.compass.persistency.initialState.GameStatePersistency;
import net.afterday.compass.persistency.items.ItemsPersistency;

/**
 * Created by Justas Spakauskas on 1/28/2018.
 */

public class PersistencyProviderImpl implements PersistencyProvider
{
    private final ItemsPersistency itemsPersistency = new HItemsPersistency();
    @Override
    public InfluencesPersistency getInfluencesPersistency()
    {
        return new HInfluencesPersistency();
    }

    @Override
    public ItemsPersistency getItemsPersistency()
    {
        return itemsPersistency;
    }

    @Override
    public GameStatePersistency getInitialStatePersistency()
    {
        return new HGameStatePersistency();
    }
}
