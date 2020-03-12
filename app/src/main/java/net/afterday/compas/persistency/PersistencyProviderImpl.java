package net.afterday.compas.persistency;

import net.afterday.compas.persistency.hardcoded.HGameStatePersistency;
import net.afterday.compas.persistency.hardcoded.HInfluencesPersistency;
import net.afterday.compas.persistency.hardcoded.HItemsPersistency;
import net.afterday.compas.persistency.hardcoded.HPlayerPersistency;
import net.afterday.compas.persistency.influences.InfluencesPersistency;
import net.afterday.compas.persistency.initialState.GameStatePersistency;
import net.afterday.compas.persistency.items.ItemsPersistency;
import net.afterday.compas.persistency.player.PlayerPersistency;

/**
 * Created by Justas Spakauskas on 1/28/2018.
 */

public class PersistencyProviderImpl implements PersistencyProvider
{
    private final ItemsPersistency itemsPersistency = new HItemsPersistency();
    private final GameStatePersistency gameStatePersistency = new HGameStatePersistency();
    private final InfluencesPersistency influencesPersistency = new HInfluencesPersistency();
    private final PlayerPersistency playerPersistency = new HPlayerPersistency();
    @Override
    public InfluencesPersistency getInfluencesPersistency()
    {
        return influencesPersistency;
    }

    @Override
    public ItemsPersistency getItemsPersistency()
    {
        return itemsPersistency;
    }

    @Override
    public GameStatePersistency getInitialStatePersistency()
    {
        return gameStatePersistency;
    }

    @Override
    public PlayerPersistency getPlayerPersistency()
    {
        return playerPersistency;
    }
}
