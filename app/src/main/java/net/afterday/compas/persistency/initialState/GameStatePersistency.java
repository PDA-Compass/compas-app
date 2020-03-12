package net.afterday.compas.persistency.initialState;

import net.afterday.compas.core.gameState.GameState;

/**
 * Created by Justas Spakauskas on 2/10/2018.
 */

public interface GameStatePersistency
{
    GameState loadState();
    void storeState(GameState gameState);
}
